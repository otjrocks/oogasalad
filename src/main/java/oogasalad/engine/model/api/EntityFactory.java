package oogasalad.engine.model.api;

import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.BasicEntity;
import oogasalad.engine.model.entity.BfsEntity;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.entity.KeyboardControlledEntity;
import oogasalad.engine.model.EntityData;

/**
 * A factory design pattern to create a Entities provided a configuration model.
 *
 * @author Luke Fu
 */
public class EntityFactory {

  /**
   * Creates an entity instance based on the provided {@link EntityData}.
   *
   * @param input the input manager to use for player-controlled entities
   * @param data  the configuration data for the entity
   * @param gameMap the game map context, used for AI/pathfinding
   * @return a specific subclass of {@link Entity}, depending on controlType
   * @throws IllegalArgumentException if the controlType is unrecognized
   */
  public static Entity createEntity(GameInputManager input, EntityData data, GameMap gameMap) {
    String controlType = data.getControlType();

    if (controlType == null || controlType.isBlank()) {
      return new BasicEntity(data); // for Wall, Dot, etc.
    }

    return switch (controlType.toLowerCase()) {
      case "keyboard" -> new KeyboardControlledEntity(input, data);
      case "bfs" -> new BfsEntity(data, gameMap);
      default -> throw new IllegalArgumentException("Unknown controlType: " + controlType);
    };
  }
}
