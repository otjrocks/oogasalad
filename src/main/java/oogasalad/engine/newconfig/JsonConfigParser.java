package oogasalad.engine.newconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oogasalad.engine.ConstantsManager;
import oogasalad.engine.ConstantsManagerException;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.newconfig.api.ConfigParser;
import oogasalad.engine.newconfig.model.ControlType;
import oogasalad.engine.newconfig.model.EntityProperties;
import oogasalad.engine.newconfig.model.Level;
import oogasalad.engine.newconfig.model.Metadata;
import oogasalad.engine.newconfig.model.Settings;
import oogasalad.engine.utility.FileUtility;

/**
 * The {@code JsonConfigParser} class is responsible for parsing game configuration files in JSON
 * format and converting them into {@link GameConfig} objects. It uses the Jackson library to handle
 * JSON parsing and mapping.
 *
 * <p>This class implements the {@link ConfigParser} interface and provides methods to
 * load configuration data from a file, merge settings, and extract folder paths.
 *
 * <p>Example usage:
 * <pre>
 * JsonConfigParser parser = new JsonConfigParser();
 * GameConfig config = parser.loadFromFile("path/to/config.json");
 * </pre>
 *
 * @author Jessica Chen
 */
public class JsonConfigParser implements ConfigParser {

  private final ObjectMapper mapper;

  /**
   * Constructs a new JsonConfigParser instance and initializes the ObjectMapper used for parsing
   * JSON configurations.
   */
  public JsonConfigParser() {
    this.mapper = new ObjectMapper();
  }

  /**
   * Loads a game configuration from the specified file path.
   *
   * @param filepath the path to the configuration file to be loaded
   * @return a GameConfig object representing the loaded configuration
   * @throws ConfigException if there is an error during the loading process
   */
  public GameConfig loadFromFile(String filepath) throws ConfigException {
    GameConfig gameConfig = loadGameConfig(filepath);

    String folderPath = gameConfig.gameFolderPath();
    Map<String, EntityConfig> entityMap = constructEntities(folderPath);

    return gameConfig;
  }

  // ---- Methods for loading Game Config ----

  private GameConfig loadGameConfig(String filepath) throws ConfigException {
    try {
      JsonNode root = mapper.readTree(new File(filepath));

      // Parse each section manually
      Metadata metadata = mapper.treeToValue(root.get("metadata"), Metadata.class);
      Settings defaultSettings = mapper.treeToValue(root.get("defaultSettings"), Settings.class);

      List<Level> levels = new ArrayList<>();
      for (JsonNode levelNode : root.get("levels")) {
        JsonNode settingsNode = levelNode.get("settings");
        Settings levelSettings = settingsNode != null
            ? mapper.treeToValue(settingsNode, Settings.class)
            : null;
        String levelMap = levelNode.get("levelMap").asText();
        Settings merged = mergeSettings(defaultSettings, levelSettings);
        levels.add(new Level(merged, levelMap));
      }

      return new GameConfig(metadata, defaultSettings, levels, getFolderPath(filepath));

    } catch (IOException e) {
      throw new ConfigException("Failed to parse config file: " + filepath, e);
    }
  }

  private Settings mergeSettings(Settings defaults, Settings override) {
    if (override == null) {
      return defaults;
    }

    return new Settings(
        override.gameSpeed() != null ? override.gameSpeed() : defaults.gameSpeed(),
        override.startingLives() != null ? override.startingLives() : defaults.startingLives(),
        override.initialScore() != null ? override.initialScore() : defaults.initialScore(),
        override.scoreStrategy() != null ? override.scoreStrategy() : defaults.scoreStrategy(),
        override.winCondition() != null ? override.winCondition() : defaults.winCondition()
    );
  }

  private String getFolderPath(String filepath) throws ConfigException {
    Pattern pattern = Pattern.compile("^(.*[\\\\/])");
    Matcher matcher = pattern.matcher(filepath);

    if (matcher.find()) {
      return matcher.group(1);
    }

    throw new ConfigException("Failed to getFolderPath: " + filepath);
  }

  // ---- Methods for loading Entities ----

  private Map<String, EntityConfig> constructEntities(String folderPath) throws ConfigException {
    String entityFolderPath;

    try {
      entityFolderPath = folderPath + ConstantsManager.getMessage("Config", "ENTITIES_FOLDER");
    } catch (ConstantsManagerException e) {
      throw new ConfigException("Error in getting entities folder from constants:", e);
    }

    List<String> entities = getAvailableEntities(entityFolderPath);
    Map<String, EntityConfig> entitiesMap = new HashMap<>();

    for (String entity : entities) {
      EntityConfig entityConfig = loadEntityConfig(entityFolderPath + entity + ".json");
      entitiesMap.put(entity, entityConfig);  // map the entity file name to the entityConfig
    }

    return entitiesMap;
  }

  EntityConfig loadEntityConfig(String filepath) throws ConfigException {
    try {
      JsonNode root = mapper.readTree(new File(filepath));

      JsonNode entityTypeNode = root.get("entityType");
      EntityProperties defaultProps = parseEntityProperties(entityTypeNode, filepath);
      List<String> blocks = parseBlocks(entityTypeNode);
      List<ModeConfig> modes = parseModes(root.get("modes"), defaultProps, filepath);

      double initialX = 0;
      double initialY = 0;

      return new EntityConfig(
          defaultProps.name(),
          initialX,
          initialY,
          blocks,
          defaultProps,
          modes
      );

    } catch (IOException e) {
      throw new ConfigException("Failed to parse config file: " + filepath, e);
    }
  }

  private EntityProperties parseEntityProperties(JsonNode entityTypeNode, String filepath)
      throws ConfigException {
    try {
      return mapper.treeToValue(entityTypeNode, EntityProperties.class);
    } catch (JsonProcessingException e) {
      throw new ConfigException("Failed to process entityType in json: " + filepath, e);
    }
  }

  private List<String> parseBlocks(JsonNode entityTypeNode) {
    List<String> blocks = new ArrayList<>();
    if (entityTypeNode.has("blocks")) {
      for (JsonNode block : entityTypeNode.get("blocks")) {
        blocks.add(block.asText());
      }
    }
    return blocks;
  }

  private List<ModeConfig> parseModes(JsonNode modesNode, EntityProperties defaultProps,
      String filepath)
      throws ConfigException {
    try {
      List<ModeConfig> modes = new ArrayList<>();
      for (JsonNode modeNode : modesNode) {
        String name = modeNode.get("name").asText();
        EntityProperties overrideProps = mergeProperties(name, defaultProps, modeNode);
        ImageConfig image = mapper.treeToValue(modeNode.get("image"), ImageConfig.class);
        modes.add(new ModeConfig(name, overrideProps, image));
      }
      return modes;
    } catch (JsonProcessingException e) {
      throw new ConfigException("Failed to process modes in json: " + filepath, e);
    }
  }

  private EntityProperties mergeProperties(String modeName, EntityProperties defaultProps, JsonNode modeNode)
      throws JsonProcessingException {
    final String CONTROL_TYPE = "controlType";
    final String MOVEMENT_SPEED = "movementSpeed";

    ControlType controlType = modeNode.has(CONTROL_TYPE)
        ? mapper.treeToValue(modeNode.get(CONTROL_TYPE), ControlType.class)
        : defaultProps.controlType();

    Double movementSpeed = modeNode.has(MOVEMENT_SPEED)
        ? modeNode.get(MOVEMENT_SPEED).asDouble()
        : defaultProps.movementSpeed();

    return new EntityProperties(
        modeName,
        controlType,
        movementSpeed
    );
  }


  private static List<String> getAvailableEntities(String folderPath) {
    return FileUtility.getFileNamesInDirectory(folderPath, ".json");
  }

}
