package oogasalad.engine.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.model.CollisionRule;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;

import java.lang.reflect.Method;
import java.util.*;
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.strategies.gameoutcome.EntityBasedOutcomeStrategy;
import oogasalad.engine.records.config.model.CollisionEvent;
import oogasalad.engine.records.config.model.Settings;
import oogasalad.engine.records.config.model.wincondition.EntityBasedCondition;
import oogasalad.engine.records.config.model.wincondition.SurviveForTimeCondition;
import oogasalad.engine.records.config.model.wincondition.WinCondition;

/**
 * Utility class for converting the internal AuthoringModel data structures into serializable JSON
 * configuration files using Jackson's ObjectMapper.
 * <p>
 * This builder supports generating: - The top-level game configuration file (gameConfig.json) -
 * Per-level layout files (levelX.json) - Per-entity configuration files (e.g., blueghost.json)
 * <p>
 * All methods assume that the structure of the AuthoringModel is valid and complete.
 *
 * @author Will He
 */
public class JsonConfigBuilder {

  /**
   * Builds the top-level game configuration (gameConfig.json) from the model.
   *
   * @param model  the authoring model representing the game's data
   * @param mapper the Jackson ObjectMapper instance
   * @return a JSON ObjectNode representing the game configuration
   */
  public ObjectNode buildGameConfig(AuthoringModel model, ObjectMapper mapper) {
    ObjectNode root = mapper.createObjectNode();

    addMetadata(model, root);
    addDefaultSettings(model, root, mapper);
    addLevels(model, root, mapper);
    addCollisionRules(model, root, mapper);

    return root;
  }

  /**
   * Adds metadata to the game configuration
   */
  private void addMetadata(AuthoringModel model, ObjectNode root) {
    ObjectNode metadata = root.putObject("metadata");
    metadata.put("gameTitle", model.getGameTitle());
    metadata.put("author", model.getAuthor());
    metadata.put("gameDescription", model.getGameDescription());
  }

  /**
   * Adds default settings including win conditions to the game configuration
   */
  private void addDefaultSettings(AuthoringModel model, ObjectNode root, ObjectMapper mapper) {
    ObjectNode defaultSettings = root.putObject("defaultSettings");
    Settings settings = model.getDefaultSettings();

    // Add basic settings
    defaultSettings.put("gameSpeed", settings.gameSpeed());
    defaultSettings.put("startingLives", settings.startingLives());
    defaultSettings.put("initialScore", settings.initialScore());

    // Add score strategy
    String scoreStrategy = settings.scoreStrategy();
    defaultSettings.put("scoreStrategy", scoreStrategy != null ? scoreStrategy : "Cumulative");

    // Add win condition
    ObjectNode winCondition = buildWinCondition(settings.winCondition(), mapper);
    defaultSettings.set("winCondition", winCondition);
  }

  /**
   * Builds the win condition node based on the win condition object
   */
  private ObjectNode buildWinCondition(WinCondition winConditionObj, ObjectMapper mapper) {
    ObjectNode winCondition = mapper.createObjectNode();

    if (winConditionObj == null) {
      return createDefaultWinCondition(winCondition);
    }

    String className = winConditionObj.getClass().getSimpleName();

    if (className.contains("EntityBased")) {
      return buildEntityBasedWinCondition(winConditionObj, winCondition);
    }

    if (className.contains("SurviveForTime")) {
      return buildSurviveForTimeWinCondition(winConditionObj, winCondition);
    }

    return createDefaultWinCondition(winCondition);
  }

  /**
   * Creates a default win condition
   */
  private ObjectNode createDefaultWinCondition(ObjectNode winCondition) {
    winCondition.put("type", "EntityBased");
    winCondition.put("entityType", "dot");
    return winCondition;
  }

  /**
   * Builds an entity-based win condition
   */
  private ObjectNode buildEntityBasedWinCondition(WinCondition winConditionObj, ObjectNode winCondition) {
    winCondition.put("type", "EntityBased");
    try {
      EntityBasedCondition entityBased = (EntityBasedCondition) winConditionObj;
      winCondition.put("entityType", entityBased.entityType());
    } catch (Exception e) {
      winCondition.put("entityType", "dot");  // Default
    }
    return winCondition;
  }

  /**
   * Builds a survive-for-time win condition
   */
  private ObjectNode buildSurviveForTimeWinCondition(WinCondition winConditionObj, ObjectNode winCondition) {
    winCondition.put("type", "SurviveForTime");
    try {
      SurviveForTimeCondition surviveTime = (SurviveForTimeCondition) winConditionObj;
      winCondition.put("amount", surviveTime.amount());
    } catch (Exception e) {
      winCondition.put("amount", 5);  // Default
    }
    return winCondition;
  }

  /**
   * Adds level references to the game configuration
   */
  private void addLevels(AuthoringModel model, ObjectNode root, ObjectMapper mapper) {
    ArrayNode levels = root.putArray("levels");
    for (int i = 0; i < model.getLevels().size(); i++) {
      ObjectNode levelRef = mapper.createObjectNode();
      levelRef.put("levelMap", "level" + (i + 1));
      levels.add(levelRef);
    }
  }

  /**
   * Adds collision rules to the game configuration
   */
  private void addCollisionRules(AuthoringModel model, ObjectNode root, ObjectMapper mapper) {
    ArrayNode collisionRules = root.putArray("collisions");
    for (CollisionRule collisionRule : model.getCollisionRules()) {
      collisionRules.add(mapper.valueToTree(collisionRule));
    }
  }

  /**
   * Builds a JSON representation for a level configuration. Includes entity ID mappings, level
   * settings, and tile layout strings.
   *
   * @param draft         the level draft object containing entity placements and size
   * @param entityToIdMap mapping of entity names to integer IDs
   * @param mapper        the Jackson ObjectMapper instance
   * @return a JSON ObjectNode representing the level configuration
   */
  public ObjectNode buildLevelConfig(LevelDraft draft, Map<String, Integer> entityToIdMap,
      ObjectMapper mapper) {
    ObjectNode root = mapper.createObjectNode();

    ArrayNode mappings = root.putArray("entityMappings");
    entityToIdMap.forEach((name, id) -> {
      ObjectNode entry = mapper.createObjectNode();
      entry.put("entity", name);
      entry.put("id", id);
      mappings.add(entry);
    });

    ObjectNode settings = root.putObject("mapInfo");
    settings.put("width", draft.getWidth());
    settings.put("height", draft.getHeight());
    settings.put("edgePolicy", draft.getEdgePolicy());

    ArrayNode layout = root.putArray("layout");
    double tileSize = 40.0;
    double threshold = 1.0;

    for (int row = 0; row < draft.getHeight(); row++) {
      List<String> rowTiles = new ArrayList<>();
      for (int col = 0; col < draft.getWidth(); col++) {
        double x = col * tileSize;
        double y = row * tileSize;

        Optional<EntityPlacement> opt = draft.findEntityPlacementAt(x, y, threshold);
        if (opt.isPresent()) {
          EntityPlacement placement = opt.get();
          int entityId = entityToIdMap.getOrDefault(placement.getTypeString(), 0);
          int modeIndex = getModeIndex(placement);
          rowTiles.add(entityId + "." + modeIndex);
        } else {
          rowTiles.add("0");
        }
      }
      layout.add(String.join(" ", rowTiles));
    }

    // === spawn events ===
    ArrayNode spawnEvents = root.putArray("spawnEvents");
    // TODO: Implement spawn events

    // === mode change events ===
    ArrayNode modeChangeEvents = root.putArray("modeChangeEvents");
    // TODO: Implement mode change events

    return root;
  }

  /**
   * Determines the mode index (e.g., "1.0" → 0) based on the placement's mode name.
   *
   * @param placement the entity placement to look up
   * @return the index of the mode in the EntityType's mode list
   */
  private int getModeIndex(EntityPlacement placement) {
    EntityType type = placement.getType();
    String modeName = placement.getMode();
    List<String> modeList = new ArrayList<>(type.modes().keySet());
    return modeList.indexOf(modeName);
  }

  /**
   * Builds the full configuration JSON object for an entity type. Includes the control strategy,
   * movement speed, and all defined modes.
   *
   * @param type   the entity type to serialize
   * @param mapper the Jackson ObjectMapper instance
   * @return a JSON ObjectNode representing the entity type
   */
  public ObjectNode buildEntityTypeConfig(EntityType type, ObjectMapper mapper) {
    ObjectNode root = mapper.createObjectNode();
    ObjectNode entityTypeNode = root.putObject("entityType");

    addEntityBasics(type, entityTypeNode);
    addControlConfig(type.controlConfig(), entityTypeNode, mapper);
    addMovementSpeed(type, entityTypeNode);
    addModesArray(type, root);

    return root;
  }

  private void addEntityBasics(EntityType type, ObjectNode entityTypeNode) {
    entityTypeNode.put("name", type.type());
  }

  private void addControlConfig(ControlConfig config, ObjectNode entityTypeNode,
      ObjectMapper mapper) {
    JsonNode serialized = mapper.valueToTree(config);
    entityTypeNode.set("controlConfig", serialized);
  }

  private void addMovementSpeed(EntityType type, ObjectNode entityTypeNode) {
    ModeConfig defaultMode = type.modes().get("Default");
    if (defaultMode != null) {
      entityTypeNode.put("movementSpeed", defaultMode.entityProperties().movementSpeed());
    }
  }

  private void addModesArray(EntityType type, ObjectNode root) {
    ArrayNode modesArray = root.putArray("modes");

    for (ModeConfig mode : type.modes().values()) {
      ObjectNode modeNode = modesArray.addObject();
      modeNode.put("name", mode.name());

      ObjectNode imageNode = modeNode.putObject("image");
      imageNode.put("imagePath", getRelativeImagePath(mode.image().imagePath()));

      // Replace with actual values if ModeConfig/Image provides them
      imageNode.put("tileWidth", 14);
      imageNode.put("tileHeight", 14);
      imageNode.putArray("tilesToCycle").add(1);
      imageNode.put("animationSpeed", 2);
    }
  }


  /**
   * Assigns integer IDs to each entity type in the game. IDs start at 1 and increase in insertion
   * order.
   *
   * @param entityTypeMap the map of entity names to types
   * @return a map of entity names to unique integer IDs
   */
  public Map<String, Integer> assignIds(Map<String, EntityType> entityTypeMap) {
    Map<String, Integer> result = new HashMap<>();
    int id = 1;

    for (String entityName : entityTypeMap.keySet()) {
      result.put(entityName, id++);
    }

    return result;
  }

  private String getRelativeImagePath(String fullPath) {
    // Assume your assets root is in `src/main/resources/assets/`
    String marker = "assets/";
    int index = fullPath.indexOf(marker);
    return index >= 0 ? fullPath.substring(index) : fullPath;
  }

}
