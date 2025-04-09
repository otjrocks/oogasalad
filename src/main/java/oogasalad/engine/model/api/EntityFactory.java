package oogasalad.engine.model.api;

import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.BasicEntity;
import oogasalad.engine.model.entity.BfsEntity;
import oogasalad.engine.model.entity.Entity;
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
   * @param data The entity data for the entity that you wish to create.
   * @return An Entity object.
   * @see Entity
   */
  public static Entity createEntity(GameInputManager input, EntityPlacement data, GameMap gameMap) {
    String controlType = data.getType().controlType();

    if (isBasicEntity(controlType)) {
      return new BasicEntity(data);
    }
    return createControlledEntity(controlType.toLowerCase(), input, data, gameMap);
  }

  private static boolean isBasicEntity(String controlType) {
    return controlType == null || controlType.isBlank() ||
        controlType.equalsIgnoreCase("wall") ||
        controlType.equalsIgnoreCase("dot") ||
            controlType.equalsIgnoreCase("fruit");
  }

  private static Entity createControlledEntity(String controlType, GameInputManager input,
      EntityPlacement data, GameMap gameMap) {
    return switch (controlType) {
      case "keyboard" -> new KeyboardControlledEntity(input, data, gameMap);
      case "targetentity", "targetaheadofentity" -> new BfsEntity(data, gameMap);
      default -> throw new IllegalArgumentException("Unknown entity: " + controlType);
    };
  }
}