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
import oogasalad.engine.model.MapInfo;
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
import oogasalad.engine.records.newconfig.model.ParsedLevel;
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
  private Map<String, EntityType> entityTypeMap = new HashMap<>();

  private static final String JSON_IDENTIFIER = ".json";

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
    // Step 1: Load primary game config JSON (e.g., gameConfig.json)
    gameConfig = loadGameConfig(filepath);

    // Step 2: Load entities from the entity folder
    entityMap = constructEntities(gameConfig.gameFolderPath());

    // Step 3: Extract basic metadata
    MetaData metaData = extractMetaData(gameConfig);

    // Step 4: Build all entity types
    List<EntityType> entityTypes = new ArrayList<>();
    createEntityTypes(entityTypes);

    // Step 5: Parse level entity placements + map info
    List<List<EntityPlacement>> levels = new ArrayList<>();
    List<MapInfo> mapInfos = new ArrayList<>();
    String levelFolderPath;

    try {
      levelFolderPath =
          gameConfig.gameFolderPath() + ConstantsManager.getMessage("Config", "MAPS_FOLDER");
    } catch (ConstantsManagerException e) {
      throw new ConfigException("Error in getting maps folder from constants:", e);
    }

    for (Level level : gameConfig.levels()) {
      ParsedLevel parsed = loadLevelConfig(levelFolderPath + level.levelMap() + JSON_IDENTIFIER);
      levels.add(parsed.placements());
      mapInfos.add(parsed.mapInfo());
    }

    // Step 6: Create game settings with merged defaults and level-specific map info
    GameSettings settings = createGameSettings(gameConfig, mapInfos);

    // Step 7: Parse collision rules and win condition
    List<CollisionRule> collisionRules = convertToCollisionRules(gameConfig);
    String winCondition = gameConfig.settings().winCondition();

    // Step 8: Tiles currently unused — placeholder
    List<Tiles> tiles = new ArrayList<>();

    // Step 9: Return the full config model using the first level only for now
    return new ConfigModel(metaData, settings, entityTypes, levels.getFirst(), collisionRules,
        winCondition, tiles);
  }

  private ParsedLevel loadLevelConfig(String filepath) throws ConfigException {
    try {
      JsonNode root = mapper.readTree(new File(filepath));
      MapInfo mapInfo = mapper.treeToValue(root.get("mapInfo"), MapInfo.class);

      Map<Integer, EntityType> idToEntityType = buildEntityMappings(root.get("entityMappings"));
      Map<Integer, String> idToEntityName = buildEntityNames(root.get("entityMappings"));

      List<EntityPlacement> placements = parseLayout(root.get("layout"), idToEntityType,
          idToEntityName);
      return new ParsedLevel(placements, mapInfo);

    } catch (IOException e) {
      throw new ConfigException("Error in loading level config", e);
    }
  }

  private Map<Integer, EntityType> buildEntityMappings(JsonNode mappings) throws ConfigException {
    Map<Integer, EntityType> idToType = new HashMap<>();
    for (JsonNode node : mappings) {
      int id = node.get("id").asInt();
      String name = node.get("entity").asText();
      EntityType type = entityTypeMap.get(name);
      if (type == null) {
        throw new ConfigException("EntityType for '" + name + "' not found.");
      }
      idToType.put(id, type);
    }
    return idToType;
  }

  private Map<Integer, String> buildEntityNames(JsonNode mappings) {
    Map<Integer, String> idToName = new HashMap<>();
    for (JsonNode node : mappings) {
      idToName.put(node.get("id").asInt(), node.get("entity").asText());
    }
    return idToName;
  }

  private List<EntityPlacement> parseLayout(
      JsonNode layout,
      Map<Integer, EntityType> idToType,
      Map<Integer, String> idToName
  ) {
    List<EntityPlacement> placements = new ArrayList<>();

    for (int y = 0; y < layout.size(); y++) {
      String[] rowEntries = layout.get(y).asText().trim().split("\\s+");
      for (int x = 0; x < rowEntries.length; x++) {
        String entry = rowEntries[x];
        if (!entry.equals("0")) {
          placements.add(parseEntityPlacement(entry, x, y, idToType, idToName));
        }
      }
    }

    return placements;
  }

  private EntityPlacement parseEntityPlacement(
      String entry,
      int x,
      int y,
      Map<Integer, EntityType> idToType,
      Map<Integer, String> idToName
  ) {
    String[] parts = entry.split("\\.");
    int entityId = Integer.parseInt(parts[0]);
    int modeIndex = (parts.length > 1) ? Integer.parseInt(parts[1]) : 0;

    EntityType type = idToType.get(entityId);
    String entityName = idToName.get(entityId);
    String modeName = resolveMode(entityMap.get(entityName), List.of(modeIndex));

    return new EntityPlacement(type, x, y, modeName);
  }

  // Methods to convert from multiple config files to a singular config model

  private MetaData extractMetaData(GameConfig gameConfig) {
    return new MetaData(
        gameConfig.metadata().gameTitle(),
        gameConfig.metadata().author(),
        gameConfig.metadata().gameDescription());
  }

  private GameSettings createGameSettings(GameConfig gameConfig, List<MapInfo> mapInfos) {
    Settings baseSettings = gameConfig.settings();

    return new GameSettings(
        baseSettings.gameSpeed(),
        baseSettings.startingLives(),
        baseSettings.initialScore(),
        "wrap",
        28,
        32
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


  private List<EntityType> createEntityTypes(List<EntityType> entityTypes) {
    entityTypeMap.clear(); // Clear existing mappings if needed

    for (Map.Entry<String, EntityConfig> entry : entityMap.entrySet()) {
      String key = entry.getKey();
      EntityConfig entity = entry.getValue();
      Map<String, ModeConfig> modes = new HashMap<>();

      // create modes
      for (ModeConfig mode : entity.modes()) {
        ModeConfig modeConfig = new ModeConfig(mode.name(), mode.entityProperties(), mode.image());
        modes.put(mode.name(), modeConfig);
      }

      EntityType entityType = getEntityType(entity, modes);
      entityTypes.add(entityType);
      entityTypeMap.put(key, entityType);
    }

    return entityTypes;
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

  private List<Level> parseLevels(JsonNode root, Settings defaultSettings) {
    List<Level> levels = new ArrayList<>();
    for (JsonNode levelNode : root.get("levels")) {
      String levelMap = levelNode.get("levelMap").asText();
      levels.add(new Level(levelMap));
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
      EntityConfig entityConfig = loadEntityConfig(entityFolderPath + entity + JSON_IDENTIFIER);
      entitiesMap.put(entity, entityConfig); // map the entity file name to the entityConfig
    }

    return entitiesMap;
  }

  EntityConfig loadEntityConfig(String filepath) throws ConfigException {
    try {
      JsonNode root = mapper.readTree(new File(filepath));

      JsonNode entityTypeNode = root.get("entityType");
      EntityProperties defaultProps = parseEntityProperties(entityTypeNode, filepath);
      List<ModeConfig> modes = parseModes(root.get("modes"),
          defaultProps, filepath);

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

  private List<ModeConfig> parseModes(JsonNode modesNode,
      EntityProperties defaultProps,
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
    return FileUtility.getFileNamesInDirectory(folderPath, JSON_IDENTIFIER);
  }

}
