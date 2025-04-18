package oogasalad.player.model.strategies.control;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.exceptions.ControlStrategyException;
import oogasalad.player.model.strategies.control.pathfinding.PathFindingStrategyInterface;
import oogasalad.player.model.strategies.control.targetcalculation.TargetStrategyInterface;

/**
 * The {@code ControlStrategyHelperMethods} class provides utility methods to assist in validating
 * and retrieving configuration values for control strategies.
 *
 * <p>This class includes methods to ensure the presence of required keys in a strategy
 * configuration map and to retrieve their corresponding values as strings. If a required key is
 * missing or its value is null, an exception is thrown to indicate the issue.
 */
public class ControlStrategyHelperMethods {

  static void getDirectionFromTargetAndPath(GameMapInterface gameMap, Entity entity,
      EntityPlacement entityPlacement, TargetStrategyInterface targetStrategy,
      PathFindingStrategyInterface pathFindingStrategy) {
    int[] target = validateAndGetTargetPosition(targetStrategy);

    int[] dir = pathFindingStrategy.getPath(gameMap,
        (int) Math.round(entityPlacement.getX()),
        (int) Math.round(entityPlacement.getY()),
        target[0], target[1],
        entityPlacement, entity.getEntityDirection());

    setEntityDirection(dir[0], dir[1], entity);
  }

  static int[] validateAndGetTargetPosition(TargetStrategyInterface targetStrategy) {
    int[] targetPosition = targetStrategy.getTargetPosition();
    if (targetPosition.length != 2) {
      throw new ControlStrategyException("Target position must be of length 2");
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
