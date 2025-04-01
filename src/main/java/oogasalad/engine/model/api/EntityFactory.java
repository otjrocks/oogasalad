package oogasalad.engine.model.api;

import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.BasicEntity;
import oogasalad.engine.model.entity.BfsEntity;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.entity.KeyboardControlledEntity;
import oogasalad.engine.model.EntityData;

public class EntityFactory {

  public static Entity createEntity(GameInputManager input, EntityData data, GameMap gameMap) {
    String controlType = data.getControlType();

    if (controlType == null || controlType.isBlank() || controlType.equalsIgnoreCase("none")) {
      return new BasicEntity(data); // for Wall, Dot, etc.
    }

    return switch (controlType.toLowerCase()) {
      case "keyboard" -> new KeyboardControlledEntity(input, data);
      case "bfs" -> new BfsEntity(data, gameMap);
      default -> throw new IllegalArgumentException("Unknown controlType: " + controlType);
    };
  }
}
