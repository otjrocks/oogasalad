package oogasalad.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.config.ModeConfig;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for converting the internal AuthoringModel data structures
 * into serializable JSON configuration files using Jackson's ObjectMapper.
 *
 * This builder supports generating:
 * - The top-level game configuration file (gameConfig.json)
 * - Per-level layout files (levelX.json)
 * - Per-entity configuration files (e.g., blueghost.json)
 *
 * All methods assume that the structure of the AuthoringModel is valid and complete.
 *
 * @author Will He
 */
public class JsonConfigBuilder {

  /**
   * Builds the top-level game configuration (gameConfig.json) from the model.
   * Includes metadata, global settings, and references to level config files.
   *
   * @param model  the authoring model representing the game's data
   * @param mapper the Jackson ObjectMapper instance
   * @return a JSON ObjectNode representing the game configuration
   */
  public ObjectNode buildGameConfig(AuthoringModel model, ObjectMapper mapper) {
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
    // defaultSettings.put("scoreStrategy", model.getDefaultSettings().getScoresStrategy());
    // defaultSettings.put("winCondition", model.getDefaultSettings().getWinCondition());

    // === levels ===
    ArrayNode levels = root.putArray("levels");
    for (int i = 0; i < model.getLevels().size(); i++) {
      ObjectNode levelRef = mapper.createObjectNode();
      levelRef.put("levelMap", "level" + (i + 1));
      levels.add(levelRef);
    }

    return root;
  }

  /**
   * Builds a JSON representation for a level configuration.
   * Includes entity ID mappings, level settings, and tile layout strings.
   *
   * @param draft         the level draft object containing entity placements and size
   * @param entityToIdMap mapping of entity names to integer IDs
   * @param mapper        the Jackson ObjectMapper instance
   * @return a JSON ObjectNode representing the level configuration
   */
  public ObjectNode buildLevelConfig(LevelDraft draft, Map<String, Integer> entityToIdMap, ObjectMapper mapper) {
    ObjectNode root = mapper.createObjectNode();

    ArrayNode mappings = root.putArray("entityMappings");
    entityToIdMap.forEach((name, id) -> {
      ObjectNode entry = mapper.createObjectNode();
      entry.put("entity", name);
      entry.put("id", id);
      mappings.add(entry);
    });

    ObjectNode settings = root.putObject("settings");
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
   * Builds the full configuration JSON object for an entity type.
   * Includes the control strategy, movement speed, and all defined modes.
   *
   * @param type   the entity type to serialize
   * @param mapper the Jackson ObjectMapper instance
   * @return a JSON ObjectNode representing the entity type
   */
  public ObjectNode buildEntityTypeConfig(EntityType type, ObjectMapper mapper) {
    ObjectNode root = mapper.createObjectNode();

    ObjectNode entityTypeNode = root.putObject("entityType");
    entityTypeNode.put("name", type.type());

    // Control Type and its config
    ObjectNode controlTypeNode = entityTypeNode.putObject("controlType");
    controlTypeNode.put("controlType", type.controlType());

    if (type.strategyConfig() != null && !type.strategyConfig().isEmpty()) {
      ObjectNode controlConfig = mapper.valueToTree(type.strategyConfig());
      controlTypeNode.set("controlTypeConfig", controlConfig);
    }

    // Movement speed: extracted from "Default" mode if it exists
    ModeConfig defaultMode = type.modes().get("Default");
    if (defaultMode != null) {
      entityTypeNode.put("movementSpeed", defaultMode.getMovementSpeed());
    }

    // === Modes array ===
    ArrayNode modesArray = root.putArray("modes");
    for (ModeConfig mode : type.modes().values()) {
      ObjectNode modeNode = mapper.createObjectNode();
      modeNode.put("name", mode.getModeName());

      ObjectNode imageNode = modeNode.putObject("image");
      imageNode.put("imagePath", getRelativeImagePath(mode.getImagePath()));

      // TODO: Temporary hardcoded size and animation data; replace if dynamic
      imageNode.put("tileWidth", 14);
      imageNode.put("tileHeight", 14);

      ArrayNode tilesToCycleArray = imageNode.putArray("tilesToCycle");
      tilesToCycleArray.add(1); // Replace with mode.getTilesToCycle() if available
      imageNode.put("animationSpeed", 2);

      modesArray.add(modeNode);
    }

    return root;
  }

  /**
   * Assigns integer IDs to each entity type in the game.
   * IDs start at 1 and increase in insertion order.
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
