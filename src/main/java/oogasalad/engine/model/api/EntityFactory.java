package oogasalad.engine.model.api;

import javafx.scene.Group;
import javafx.scene.Scene;
import oogasalad.engine.model.entity.BfsEntity;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.EntityData;
import oogasalad.engine.model.entity.KeyboardControlledEntity;

public class EntityFactory {

  public static Entity createEntity(Group root, EntityData data) {
    return switch (data.getControlType().toLowerCase()) {
      case "keyboard" -> new KeyboardControlledEntity(root, data);
      case "bfs" -> new BfsEntity(data);
      default -> throw new IllegalArgumentException("Unknown entity");
    };
  }

}
