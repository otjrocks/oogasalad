package oogasalad.engine.config;

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
import oogasalad.engine.config.api.ConfigParser;
import oogasalad.engine.model.CollisionRule;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameSettings;
import oogasalad.engine.model.MetaData;
import oogasalad.engine.model.Tiles;
import oogasalad.engine.records.newconfig.CollisionConfig;
import oogasalad.engine.records.newconfig.EntityConfig;
import oogasalad.engine.records.newconfig.GameConfig;
import oogasalad.engine.records.newconfig.ImageConfig;
import oogasalad.engine.records.newconfig.model.ControlType;
import oogasalad.engine.records.newconfig.model.EntityProperties;
import oogasalad.engine.records.newconfig.model.Level;
import oogasalad.engine.records.newconfig.model.Metadata;
import oogasalad.engine.records.newconfig.model.Settings;
import oogasalad.engine.utility.FileUtility;

/**
 * The {@code JsonConfigParser} class is responsible for parsing game configuration files in JSON
 * format and converting them into {@link GameConfig} objects. It uses the Jackson library to handle
 * JSON parsing and mapping.
 *
 * <p>
 * This class implements the {@link ConfigParser} interface and provides methods to load
 * configuration data from a file, merge settings, and extract folder paths.
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
  private GameConfig gameConfig;
  private Map<String, EntityConfig> entityMap;

  /**
   * Constructs a new JsonConfigParser instance and initializes the ObjectMapper used for parsing
   * JSON configurations.
   */
  public JsonConfigParser() {
    this.mapper = new ObjectMapper();
  }

  /**
   * Loads a {@link ConfigModel} from a JSON file at the specified file path.
   *
   * <p>
   * After deserializing the file, this method also resolves the entity types of each
   * {@link EntityPlacement} by matching their type strings to defined {@link EntityType}s.
   *
   * @param filepath the path to the JSON configuration file to be loaded
   * @return the fully populated {@link ConfigModel} object
   * @throws ConfigException if the file is missing or cannot be parsed correctly
   */
  public ConfigModel loadFromFile(String filepath) throws ConfigException {
    // parse all the stuff from new configuation
    gameConfig = loadGameConfig(filepath);
    entityMap = constructEntities(gameConfig.gameFolderPath());

    // map all the new information to the old model
    MetaData metaData = extractMetaData(gameConfig);
    GameSettings settings = createGameSettings(gameConfig);

    List<EntityType> entityTypes = new ArrayList<>();
    createEntityTypes(entityTypes);

    // TODO: loading the map create the entityPlacements for each level
    // esentially take each parsed thing, create a new entityPlacement, use the fact like with collisions
    // that you can use entityMaps to get any entity specific info such as the mode name for the corresponding mode
    // if no ._ specified just do the first mode by default
    // TODO: currently sicne we only have one map, can stay with just one entityPlacements, in the
    // future might want to make this a list of lists
    List<EntityPlacement> entityPlacements = new ArrayList<>();

    List<CollisionRule> collisionRules = convertToCollisionRules(gameConfig);
    String winCondition = gameConfig.settings().winCondition();
    List<Tiles> tiles = new ArrayList<>();

    return new ConfigModel(metaData, settings, entityTypes, entityPlacements, collisionRules,
        winCondition, tiles);
  }

  // Methods to convert from multiple config files to a singular config model

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
        "", 1, 1 // TODO: Replace with actual parsed map data
    );
  }

  private List<CollisionRule> convertToCollisionRules(GameConfig gameConfig) {
    List<CollisionRule> collisionRules = new ArrayList<>();

    for (CollisionConfig collision : gameConfig.collisions()) {
      EntityConfig entityA = entityMap.get(collision.entityA());
      EntityConfig entityB = entityMap.get(collision.entityB());

      String modeA = resolveMode(entityA, collision.modeA());
      String modeB = resolveMode(entityB, collision.modeB());

      collisionRules.add(new CollisionRule(
          entityA.name(), modeA, entityB.name(), modeB,
          collision.eventsA(), collision.eventsB()));
    }

    return collisionRules;
  }

  private String resolveMode(EntityConfig entity, List<Integer> modeList) {
    if (modeList == null || modeList.isEmpty()) {
      return "Any";   // default behave if no specified mode
    }
    // TODO: taking first for now
    return entity.modes().get(modeList.getFirst()).name();
  }


  private void createEntityTypes(List<EntityType> entityTypes) {

    for (EntityConfig entity : entityMap.values()) {
      Map<String, oogasalad.engine.config.ModeConfig> modes = new HashMap<>();

      // create modes
      for (oogasalad.engine.records.newconfig.ModeConfig mode : entity.modes()) {
        oogasalad.engine.config.ModeConfig modeConfig = new oogasalad.engine.config.ModeConfig(
            mode.entityProperties().movementSpeed(), mode.image().imagePath());
        modes.put(mode.name(), modeConfig);
      }

      EntityType entityType = getEntityType(entity, modes);
      entityTypes.add(entityType);
    }
  }

  private static EntityType getEntityType(EntityConfig entity, Map<String, ModeConfig> modes) {
    Map<String, Object> strategyConfig = new HashMap<>();
    if (entity.entityProperties().controlType().controlTypeConfig() != null) {
      strategyConfig.put("targetType",
          entity.entityProperties().controlType().controlTypeConfig().targetType());
      strategyConfig.put("tilesAhead",
          entity.entityProperties().controlType().controlTypeConfig().tilesAhead());
    }

    // TODO: remove effects
    EntityType entityType = new EntityType(entity.name(),
        entity.entityProperties().controlType().controlType(), "",
        modes, entity.entityProperties().blocks(), strategyConfig);
    return entityType;
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

  private List<Level> parseLevels(JsonNode root, Settings defaultSettings)
      throws JsonProcessingException {
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
      List<oogasalad.engine.records.newconfig.ModeConfig> modes = parseModes(root.get("modes"), defaultProps, filepath);

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

  private List<oogasalad.engine.records.newconfig.ModeConfig> parseModes(JsonNode modesNode, EntityProperties defaultProps,
      String filepath)
      throws ConfigException {
    try {
      List<oogasalad.engine.records.newconfig.ModeConfig> modes = new ArrayList<>();
      for (JsonNode modeNode : modesNode) {
        String name = modeNode.get("name").asText();
        EntityProperties overrideProps = mergeProperties(name, defaultProps, modeNode);
        ImageConfig image = mapper.treeToValue(modeNode.get("image"), ImageConfig.class);
        modes.add(new oogasalad.engine.records.newconfig.ModeConfig(name, overrideProps, image));
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
