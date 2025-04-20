package oogasalad.engine.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.records.config.ModeConfigRecord;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.records.config.model.wincondition.EntityBasedConditionRecord;
import oogasalad.engine.records.config.model.wincondition.SurviveForTimeConditionRecord;
import oogasalad.engine.records.model.EntityTypeRecord;

/**
 * Utility class for converting the internal AuthoringModel data structures into serializable JSON
 * configuration files using Jackson's ObjectMapper.
 * <p>
 * This builder supports generating: - The top-level game configuration file (gameConfig.json) -
 * Per-level layout files (levelX.json) - Per-entity configuration files (e.g., blueghost.json)
 * <p>
 * All methods assume that the structure of the AuthoringModel is valid and complete.
 *
 * @author Will He, Angela Predolac
 */
public class JsonConfigBuilder {

  /**
   * Builds the top-level game configuration (gameConfig.json) from the model. Includes metadata,
   * global settings, and references to level config files.
   *
   * @param model  the authoring model representing the game's data
   * @param mapper the Jackson ObjectMapper instance
   * @return a JSON ObjectNode representing the game configuration
   */
  public ObjectNode buildGameConfig(AuthoringModel model, ObjectMapper mapper) {
    final String WIN_CONDITION_SURVIVE_FOR_TIME = "SurviveForTime";
    final String WIN_CONDITION_ENTITY_BASED = "EntityBased";

    ObjectNode root = mapper.createObjectNode();

    // === metadata ===
    ObjectNode metadata = root.putObject("metadata");
    metadata.put("gameTitle", model.getGameTitle());
    metadata.put("author", model.getAuthor());
    metadata.put("gameDescription", model.getGameDescription());

    // === defaultSettings ===
    ObjectNode defaultSettings = root.putObject("defaultSettings");
    defaultSettings.put("gameSpeed", model.getDefaultSettings().gameSpeed());
    defaultSettings.put("startingLives", model.getDefaultSettings().startingLives());
    defaultSettings.put("initialScore", model.getDefaultSettings().initialScore());
    // TODO: add these to settings
    defaultSettings.put("scoreStrategy", "Cumulative"); // TODO: remove hardcoded value

    // === win conditions ===
    // TODO: remove hard coded win condition and replace with settings from authoring environment
    ObjectNode winCondition = mapper.createObjectNode();
    if (model.getDefaultSettings().winCondition() instanceof SurviveForTimeConditionRecord(int amount)) {
      winCondition.put("type", WIN_CONDITION_SURVIVE_FOR_TIME);
      winCondition.put("amount", amount);
    } else if (model.getDefaultSettings().winCondition() instanceof EntityBasedConditionRecord(String entityType)) {
      winCondition.put("type", WIN_CONDITION_ENTITY_BASED);
      winCondition.put("entityType", entityType);
    }
    defaultSettings.set("winCondition", winCondition);

    // === levels ===
    ArrayNode levels = root.putArray("levels");
    for (int i = 0; i < model.getLevels().size(); i++) {
      ObjectNode levelRef = mapper.createObjectNode();
      levelRef.put("levelMap", "level" + (i + 1));
      levels.add(levelRef);
    }

    // === collisions ===
    ArrayNode collisionRules = root.putArray("collisions");
    for (CollisionRule collisionRule : model.getCollisionRules()) {
      collisionRules.add(mapper.valueToTree(collisionRule));
    }
    return root;
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
    EntityTypeRecord type = placement.getType();
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
  public ObjectNode buildEntityTypeConfig(EntityTypeRecord type, ObjectMapper mapper) {
    ObjectNode root = mapper.createObjectNode();
    ObjectNode entityTypeNode = root.putObject("entityType");

    addEntityBasics(type, entityTypeNode);
    addControlConfig(type.controlConfig(), entityTypeNode, mapper);
    addMovementSpeed(type, entityTypeNode);
    addModesArray(type, root);

    return root;
  }

  private void addEntityBasics(EntityTypeRecord type, ObjectNode entityTypeNode) {
    entityTypeNode.put("name", type.type());
  }

  private void addControlConfig(ControlConfigInterface config, ObjectNode entityTypeNode,
      ObjectMapper mapper) {
    JsonNode serialized = mapper.valueToTree(config);
    entityTypeNode.set("controlConfig", serialized);
  }

  private void addMovementSpeed(EntityTypeRecord type, ObjectNode entityTypeNode) {
    ModeConfigRecord defaultMode = type.modes().get("Default");
    if (defaultMode != null) {
      entityTypeNode.put("movementSpeed", defaultMode.entityProperties().movementSpeed());
    }
  }

  private void addModesArray(EntityTypeRecord type, ObjectNode root) {
    ArrayNode modesArray = root.putArray("modes");

    for (ModeConfigRecord mode : type.modes().values()) {
      ObjectNode modeNode = modesArray.addObject();
      modeNode.put("name", mode.name());

      ObjectNode imageNode = modeNode.putObject("image");
      imageNode.put("imagePath", getImagePath(mode.image().imagePath()));

      // Replace with actual values if ModeConfig/Image provides them
      imageNode.put("tileWidth", 14);
      imageNode.put("tileHeight", 14);
      imageNode.put("tilesToCycle", 1);
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
  public Map<String, Integer> assignIds(Map<String, EntityTypeRecord> entityTypeMap) {
    Map<String, Integer> result = new HashMap<>();
    int id = 1;

    for (String entityName : entityTypeMap.keySet()) {
      result.put(entityName, id++);
    }

    return result;
  }

  private String getImagePath(String fullPath) {
    // Extract just the file name from the full path
    String fileName = fullPath.substring(fullPath.lastIndexOf('/') + 1);
    return "assets/" + fileName;
  }

}
