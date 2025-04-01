package oogasalad.engine.model.api;

import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.BasicEntity;
import oogasalad.engine.model.entity.BfsEntity;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.EntityType;
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
  public static Entity createEntity(GameInputManager input, EntityPlacement data, GameMap gameMap) {
    String controlType = data.getType().getControlType();

    if (controlType == null || controlType.isBlank()) {
      return new BasicEntity(data); // for Wall, Dot, etc.
    }


    return switch (controlType.toLowerCase()) {
      case "keyboard" -> new KeyboardControlledEntity(input, data);
      case "bfs" -> new BfsEntity(data, gameMap);
      case "wall", "dot" -> new BasicEntity(data);
      default -> throw new IllegalArgumentException("Unknown entity");
    };
  }

}