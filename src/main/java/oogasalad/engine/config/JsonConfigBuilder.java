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

public class JsonConfigBuilder {

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
//    defaultSettings.put("scoreStrategy", model.getDefaultSettings().getScoresStrategy());
//    defaultSettings.put("winCondition", model.getDefaultSettings().getWinCondition());

    // === levels ===
    ArrayNode levels = root.putArray("levels");
    for (int i = 0; i < model.getLevels().size(); i++) {
      ObjectNode levelRef = mapper.createObjectNode();
      levelRef.put("levelMap", "level" + (i + 1));
      levels.add(levelRef);
    }

    return root;
  }

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

    System.out.println(draft.getWidth());
    System.out.println(draft.getHeight());
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

  private int getModeIndex(EntityPlacement placement) {
    EntityType type = placement.getType();
    String modeName = placement.getMode();
    List<String> modeList = new ArrayList<>(type.modes().keySet());
    return modeList.indexOf(modeName);
  }


  public ObjectNode buildEntityTypeConfig(EntityType type, ObjectMapper mapper) {
    ObjectNode root = mapper.createObjectNode();

    // === entityType block ===
    ObjectNode entityTypeNode = root.putObject("entityType");
    entityTypeNode.put("name", type.type());

    // Control type
    ObjectNode controlTypeNode = entityTypeNode.putObject("controlType");
    controlTypeNode.put("controlType", type.controlType());

    if (type.strategyConfig() != null && !type.strategyConfig().isEmpty()) {
      ObjectNode controlConfig = mapper.valueToTree(type.strategyConfig());
      controlTypeNode.set("controlTypeConfig", controlConfig);
    }

    // Movement speed — grab from default mode
    ModeConfig defaultMode = type.modes().get("Default");
    if (defaultMode != null) {
      entityTypeNode.put("movementSpeed", defaultMode.getMovementSpeed());
    }

    // === modes array ===
    ArrayNode modesArray = root.putArray("modes");
    for (ModeConfig mode : type.modes().values()) {
      ObjectNode modeNode = mapper.createObjectNode();
      modeNode.put("name", mode.getModeName());

      ObjectNode imageNode = modeNode.putObject("image");
      imageNode.put("imagePath", mode.getImagePath());

      // TODO: remove these hardcoded values
      imageNode.put("tileWidth", 14);
      imageNode.put("tileHeight", 14);

      ArrayNode tilesToCycleArray = imageNode.putArray("tilesToCycle");
//      for (int tile : mode.getTilesToCycle()) {
//        tilesToCycleArray.add(tile);
//      }
      tilesToCycleArray.add(1);

      imageNode.put("animationSpeed", 2);

      modesArray.add(modeNode);
    }

    return root;
  }


  private int getFirstSpeed(EntityType entity) {
    return entity.modes().values().stream()
        .findFirst()
        .map(ModeConfig::getMovementSpeed)
        .orElse(100);
  }

  public Map<String, Integer> assignIds(Map<String, EntityType> entityTypeMap) {
    Map<String, Integer> result = new HashMap<>();
    int id = 1;

    for (String entityName : entityTypeMap.keySet()) {
      result.put(entityName, id++);
    }

    return result;
  }

}
