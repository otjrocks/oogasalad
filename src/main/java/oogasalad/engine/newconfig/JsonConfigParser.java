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
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.api.ConfigParser;
import oogasalad.engine.model.CollisionRule;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameSettings;
import oogasalad.engine.model.MetaData;
import oogasalad.engine.model.Tiles;
import oogasalad.engine.newconfig.model.ControlType;
import oogasalad.engine.newconfig.model.EntityProperties;
import oogasalad.engine.newconfig.model.Level;
import oogasalad.engine.newconfig.model.Metadata;
import oogasalad.engine.newconfig.model.Settings;
import oogasalad.engine.utility.FileUtility;

/**
 * The {@code JsonConfigParser} class is responsible for parsing game
 * configuration files in JSON
 * format and converting them into {@link GameConfig} objects. It uses the
 * Jackson library to handle
 * JSON parsing and mapping.
 *
 * <p>
 * This class implements the {@link ConfigParser} interface and provides methods
 * to
 * load configuration data from a file, merge settings, and extract folder
 * paths.
 *
 * <p>
 * Example usage:
 * 
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
   * Constructs a new JsonConfigParser instance and initializes the ObjectMapper
   * used for parsing
   * JSON configurations.
   */
  public JsonConfigParser() {
    this.mapper = new ObjectMapper();
  }

  /**
   * Loads a {@link ConfigModel} from a JSON file at the specified file path.
   *
   * <p>
   * After deserializing the file, this method also resolves the entity types of
   * each
   * {@link EntityPlacement} by matching their type strings to defined
   * {@link EntityType}s.
   *
   * @param filepath the path to the JSON configuration file to be loaded
   * @return the fully populated {@link ConfigModel} object
   * @throws ConfigException if the file is missing or cannot be parsed correctly
   */
  public ConfigModel loadFromFile(String filepath) throws ConfigException {
    GameConfig gameConfig = loadGameConfig(filepath);

    MetaData metaData = extractMetaData(gameConfig);
    GameSettings settings = createGameSettings(gameConfig);

    List<EntityType> entityTypes = new ArrayList<>();
    List<EntityPlacement> entityPlacements = new ArrayList<>();
    createEntityTypes(gameConfig, entityTypes, entityPlacements);

    List<CollisionRule> collisionRules = convertToCollisionRules(gameConfig);
    String winCondition = gameConfig.settings().winCondition();
    List<Tiles> tiles = new ArrayList<>();

    return new ConfigModel(metaData, settings, entityTypes, entityPlacements, collisionRules,
        winCondition, tiles);
  }

  private MetaData extractMetaData(GameConfig gameConfig) {
    return new MetaData(
        gameConfig.metadata().gameTitle(),
        gameConfig.metadata().author(),
        gameConfig.metadata().gameDescription());
  }

  private GameSettings createGameSettings(GameConfig gameConfig) {
    return new GameSettings(
        gameConfig.settings().gameSpeed(),
        gameConfig.settings().startingLives(),
        gameConfig.settings().initialScore(),
        "", 0, 0 // TODO: Replace with actual parsed map data
    );
  }

  private List<CollisionRule> convertToCollisionRules(GameConfig gameConfig) {
    List<CollisionRule> collisionRules = new ArrayList<>();

    for (CollisionConfig collision : gameConfig.collisions()) {
      // TODO: for now I just hardcoded to any, since these are still with string
      // modes
      collisionRules.add(
          new CollisionRule(collision.entityA(), "Any", collision.entityB(), "Any",
              collision.eventsA(), collision.eventsB()));
    }
    return collisionRules;
  }

  private void createEntityTypes(GameConfig gameConfig, List<EntityType> entityTypes,
      List<EntityPlacement> entityPlacements) throws ConfigException {

    Map<String, EntityConfig> entityMap = constructEntities(gameConfig.gameFolderPath());

    for (EntityConfig entity : entityMap.values()) {
      Map<String, oogasalad.engine.config.ModeConfig> modes = new HashMap<>();

      // create modes
      for (ModeConfig mode : entity.modes()) {
        oogasalad.engine.config.ModeConfig modeConfig = new oogasalad.engine.config.ModeConfig(
            mode.entityProperties().movementSpeed(), mode.image().imagePath());
        modes.put(mode.name(), modeConfig);
      }

      Map<String, Object> strategyConfig = new HashMap<>();
      if (entity.entityProperties().controlType().controlTypeConfig() != null) {
        strategyConfig.put("targetType",
            entity.entityProperties().controlType().controlTypeConfig().targetType());
        strategyConfig.put("tilesAhead",
            entity.entityProperties().controlType().controlTypeConfig().tilesAhead());
      }

      // TODO: effects is currently hardcoded because I think we getting rid of it
      EntityType entityType = new EntityType(entity.name(),
          entity.entityProperties().controlType().controlType(), "",
          modes, entity.entityProperties().blocks(), strategyConfig);
      entityTypes.add(entityType);

      EntityPlacement entityPlacement = new EntityPlacement(entityType, 0, 0,
          entity.modes().getFirst().name());
      entityPlacement.setType(entity.name());

      entityPlacements.add(entityPlacement);
    }
  }

  // ---- Methods for loading Game Config ----

  GameConfig loadGameConfig(String filepath) throws ConfigException {
    try {
      JsonNode root = mapper.readTree(new File(filepath));
  
      Metadata metadata = parseMetadata(root);
      Settings defaultSettings = parseDefaultSettings(root);
      List<Level> levels = parseLevels(root, defaultSettings);
      List<CollisionConfig> collisions = parseCollisions(root);
  
      return new GameConfig(metadata, defaultSettings, levels, collisions, getFolderPath(filepath));
  
    } catch (IOException e) {
      throw new ConfigException("Failed to parse config file: " + filepath, e);
    }
  }
  
  private Metadata parseMetadata(JsonNode root) throws JsonProcessingException {
    return mapper.treeToValue(root.get("metadata"), Metadata.class);
  }
  
  private Settings parseDefaultSettings(JsonNode root) throws JsonProcessingException {
    return mapper.treeToValue(root.get("defaultSettings"), Settings.class);
  }
  
  private List<Level> parseLevels(JsonNode root, Settings defaultSettings) throws JsonProcessingException {
    List<Level> levels = new ArrayList<>();
    for (JsonNode levelNode : root.get("levels")) {
      Settings levelSettings = null;
      JsonNode settingsNode = levelNode.get("settings");
      if (settingsNode != null) {
        levelSettings = mapper.treeToValue(settingsNode, Settings.class);
      }
      String levelMap = levelNode.get("levelMap").asText();
      Settings merged = mergeSettings(defaultSettings, levelSettings);
      levels.add(new Level(merged, levelMap));
    }
    return levels;
  }
  
  private List<CollisionConfig> parseCollisions(JsonNode root) throws JsonProcessingException {
    List<CollisionConfig> collisions = new ArrayList<>();
    JsonNode collisionsNode = root.get("collisions");
    if (collisionsNode != null && collisionsNode.isArray()) {
      for (JsonNode collisionNode : collisionsNode) {
        collisions.add(mapper.treeToValue(collisionNode, CollisionConfig.class));
      }
    }
    return collisions;
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
        override.winCondition() != null ? override.winCondition() : defaults.winCondition());
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
      entitiesMap.put(entity, entityConfig); // map the entity file name to the entityConfig
    }

    return entitiesMap;
  }

  EntityConfig loadEntityConfig(String filepath) throws ConfigException {
    try {
      JsonNode root = mapper.readTree(new File(filepath));

      JsonNode entityTypeNode = root.get("entityType");
      EntityProperties defaultProps = parseEntityProperties(entityTypeNode, filepath);
      List<ModeConfig> modes = parseModes(root.get("modes"), defaultProps, filepath);

      return new EntityConfig(
          defaultProps.name(),
          defaultProps,
          modes);

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

  private EntityProperties mergeProperties(String modeName, EntityProperties defaultProps,
      JsonNode modeNode)
      throws JsonProcessingException {
    final String CONTROL_TYPE = "controlType";
    final String MOVEMENT_SPEED = "movementSpeed";
    final String BLOCKS = "blocks";

    ControlType controlType = modeNode.has(CONTROL_TYPE)
        ? mapper.treeToValue(modeNode.get(CONTROL_TYPE), ControlType.class)
        : defaultProps.controlType();

    Double movementSpeed = modeNode.has(MOVEMENT_SPEED)
        ? modeNode.get(MOVEMENT_SPEED).asDouble()
        : defaultProps.movementSpeed();

    List<String> blocks = modeNode.has(BLOCKS)
        ? mapper.convertValue(modeNode.get(BLOCKS),
            mapper.getTypeFactory().constructCollectionType(List.class, String.class))
        : defaultProps.blocks();

    return new EntityProperties(
        modeName,
        controlType,
        movementSpeed,
        blocks);
  }

  private static List<String> getAvailableEntities(String folderPath) {
    return FileUtility.getFileNamesInDirectory(folderPath, ".json");
  }

}
