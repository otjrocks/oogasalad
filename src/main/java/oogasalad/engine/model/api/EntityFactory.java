package oogasalad.engine.model.api;

import javafx.scene.Group;
import javafx.scene.Scene;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.BfsEntity;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.EntityData;
import oogasalad.engine.model.entity.KeyboardControlledEntity;

public class EntityFactory {

  public static Entity createEntity(Scene scene, EntityData data, GameMap gameMap) {
    return switch (data.getControlType().toLowerCase()) {
      case "keyboard" -> new KeyboardControlledEntity(scene, data);
      case "bfs" -> new BfsEntity(data, gameMap);
      default -> throw new IllegalArgumentException("Unknown entity");
    };
  }

}
