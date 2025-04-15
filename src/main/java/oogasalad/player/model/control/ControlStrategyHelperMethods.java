package oogasalad.player.model.control;

import java.util.Map;
import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.BfsEntityException;
import oogasalad.player.model.control.pathfinding.PathFindingStrategy;
import oogasalad.player.model.control.targetcalculation.TargetStrategy;
import oogasalad.player.model.exceptions.TargetStrategyException;

/**
 * The {@code ControlStrategyHelperMethods} class provides utility methods to assist in validating
 * and retrieving configuration values for control strategies.
 *
 * <p>This class includes methods to ensure the presence of required keys in a strategy
 * configuration map and to retrieve their corresponding values as strings. If a required key is
 * missing or its value is null, an exception is thrown to indicate the issue.
 */
public class ControlStrategyHelperMethods {

  /**
   * Validates the presence of a specified key in the given strategy configuration map and retrieves
   * its corresponding value as a string. If the key is missing or its value is null, a
   * {@link TargetStrategyException} is thrown.
   *
   * @param strategyConfig the map containing strategy configuration key-value pairs
   * @param key            the key to validate and retrieve the value for
   * @return the value associated with the specified key as a string
   * @throws TargetStrategyException if the key is missing or its value is null
   */
  static String validateAndGetPathFindingStrategy(Map<String, Object> strategyConfig, String key) {
    if (!strategyConfig.containsKey(key)
        || strategyConfig.get(key) == null) {
      throw new TargetStrategyException("Type " + key + " is required");
    }

    return strategyConfig.get(key).toString();
  }

  static void getDirectionFromTargetAndPath(GameMap gameMap, Entity entity,
      EntityPlacement entityPlacement, TargetStrategy targetStrategy,
      PathFindingStrategy pathFindingStrategy) {
    int[] target = validateAndGetTargetPosition(targetStrategy);

    int[] dir = pathFindingStrategy.getPath(gameMap,
        (int) Math.round(entityPlacement.getX()),
        (int) Math.round(entityPlacement.getY()),
        target[0], target[1],
        entityPlacement, entity.getEntityDirection());

    setEntityDirection(dir[0], dir[1], entity);
  }

  static int[] validateAndGetTargetPosition(TargetStrategy targetStrategy) {
    int[] targetPosition = targetStrategy.getTargetPosition();
    if (targetPosition.length != 2) {
      throw new BfsEntityException("Target position must be of length 2");
    }
    return targetPosition;
  }

  static void setEntityDirection(int dx, int dy, Entity entity) {
    for (Direction direction : Direction.values()) {
      if (direction == Direction.NONE) {
        break;
      }
      if (isValidDirection(dx, dy, direction, entity)) {
        entity.setEntitySnapDirection(direction);
        return;
      }
    }
  }

  private static boolean isValidDirection(int dx, int dy, Direction direction, Entity entity) {
    int normDx = Integer.compare(dx, 0);
    int normDy = Integer.compare(dy, 0);
    return normDx == direction.getDx() && normDy == direction.getDy() && entity.canMove(direction);
  }

}
