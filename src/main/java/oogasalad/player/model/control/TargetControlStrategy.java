package oogasalad.player.model.control;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.BfsEntityException;
import oogasalad.player.model.control.pathfinding.BfsPathFindingStrategy;
import oogasalad.player.model.control.pathfinding.PathFindingStrategy;
import oogasalad.player.model.control.targetcalculation.TargetStrategy;
import oogasalad.player.model.control.targetcalculation.TargetStrategyFactory;

public class TargetControlStrategy implements ControlStrategy {

  private final GameMap myGameMap;
  private final EntityPlacement myEntityPlacement;
  private final PathFindingStrategy myPathFindingStrategy;
  private final TargetStrategy myTargetStrategy;

  /**
   * Create a BFS strategy.
   *
   * @param gameMap         The game map this entity is a part of.
   * @param entityPlacement The data to include with this entity.
   */
  public TargetControlStrategy(GameMap gameMap, EntityPlacement entityPlacement) {
    myEntityPlacement = entityPlacement;
    myGameMap = gameMap;
    myPathFindingStrategy = new BfsPathFindingStrategy();
    myTargetStrategy = TargetStrategyFactory.createTargetStrategy(entityPlacement, myGameMap);
  }

  @Override
  public void update(Entity entity) {
    int[] target = validateAndGetTargetPosition();

    int[] dir = myPathFindingStrategy.getPath(myGameMap,
        (int) Math.round(myEntityPlacement.getX()),
        (int) Math.round(myEntityPlacement.getY()),
        target[0], target[1],
        myEntityPlacement);

    setEntityDirection(dir[0], dir[1], entity);
  }

  private int[] validateAndGetTargetPosition() {
    int[] targetPosition = myTargetStrategy.getTargetPosition();
    if (targetPosition.length != 2) {
      throw new BfsEntityException("Target position must be of length 2");
    }
    return targetPosition;
  }

  private void setEntityDirection(int dx, int dy, Entity entity) {
    char[] directions = {'R', 'L', 'D', 'U'};
    for (char direction : directions) {
      if (isValidDirection(dx, dy, direction, entity)) {
        entity.setEntityDirection(direction);
        return;
      }
    }
  }

  private boolean isValidDirection(int dx, int dy, char direction, Entity entity) {
    return switch (direction) {
      case 'R' -> dx > 0 && entity.canMove('R');
      case 'L' -> dx < 0 && entity.canMove('L');
      case 'D' -> dy > 0 && entity.canMove('D');
      case 'U' -> dy < 0 && entity.canMove('U');
      default -> false;
    };
  }
}
