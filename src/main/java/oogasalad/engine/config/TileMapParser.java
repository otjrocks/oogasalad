package oogasalad.engine.util;

import java.util.HashMap;
import java.util.Map;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityData;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.api.EntityFactory;
import oogasalad.engine.model.exceptions.InvalidPositionException;

public class TileMapParser {

  private final Map<Character, String> tileToEntityType = new HashMap<>();

  public TileMapParser() {
    tileToEntityType.put('#', "Wall");
    tileToEntityType.put('.', "Dot");
  }

  public void parseTiles(String[] layout, GameInputManager input, GameMap map, Map<String, EntityData> templates)
      throws InvalidPositionException {

    for (int y = 0; y < layout.length; y++) {
      String row = layout[y];
      for (int x = 0; x < row.length(); x++) {
        char symbol = row.charAt(x);
        String type = tileToEntityType.get(symbol);
        if (type == null) continue;

        EntityData template = templates.get(type);
        if (template == null) continue;

        EntityData clone = cloneWithPosition(template, x, y);
        Entity entity = EntityFactory.createEntity(input, clone, map);
        map.addEntity(entity);
      }
    }
  }

  private EntityData cloneWithPosition(EntityData template, int x, int y) {
    EntityData data = new EntityData();
    data.setType(template.getType());
    data.setImagePath(template.getImagePath());
    data.setControlType(template.getControlType());
    data.setEffect(template.getEffect());
    data.setInitialX(x);
    data.setInitialY(y);
    return data;
  }
}
