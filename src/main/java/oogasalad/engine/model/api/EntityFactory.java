package oogasalad.engine.model.api;

import javafx.scene.Scene;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.BasicEntity;
import oogasalad.engine.model.entity.BfsEntity;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.EntityData;
import oogasalad.engine.model.entity.KeyboardControlledEntity;

/**
 * A factory design pattern used to create various entities.
 *
 * @author Jessica Chen
 */
public class EntityFactory {

  /**
   * Create an entity with the provided parameters.
   *
   * @param data  The entity data for the entity that you wish to create.
   * @return An Entity object.
   * @see Entity
   */
  public static Entity createEntity(GameInputManager input, EntityData data, GameMap gameMap) {
    return switch (data.getControlType().toLowerCase()) {
      case "keyboard" -> new KeyboardControlledEntity(input, data);
      case "bfs" -> new BfsEntity(data, gameMap);
      case "wall", "dot" -> new BasicEntity(data);
      default -> throw new IllegalArgumentException("Unknown entity");
    };
  }

}
