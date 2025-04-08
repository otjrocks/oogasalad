package oogasalad.engine.model.entity;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.exceptions.BfsEntityException;
import oogasalad.player.model.movement.pathfinding.BfsPathFindingStrategy;
import oogasalad.player.model.movement.pathfinding.PathFindingStrategy;
import oogasalad.player.model.movement.targetcalculation.TargetStrategy;
import oogasalad.player.model.movement.targetcalculation.TargetStrategyFactory;

// quick entity to demonstrate BfsMovement, I will admit its very scuffed

/**
 * A BFS entity.
 *
 * @author Jessica Chen
 */
public class BfsEntity extends Entity {

  private final GameMap myGameMap;
  // TODO: would be nice if this was static, so each class don't need own for path
  private final PathFindingStrategy myPathFindingStrategy;
  private final TargetStrategy myTargetStrategy;

  /**
   * Create a BFS entity.
   *
   * @param entityPlacement The data to include with this entity.
   * @param gameMap         The game map this entity is a part of.
   */
  public BfsEntity(EntityPlacement entityPlacement, GameMap gameMap) {
    super(entityPlacement);

    myGameMap = gameMap;
    myPathFindingStrategy = new BfsPathFindingStrategy();
    myTargetStrategy = TargetStrategyFactory.createTargetStrategy(entityPlacement, myGameMap);
  }

  @Override
  public void update() {
    int[] target = validateAndGetTargetPosition();

    int[] dir = myPathFindingStrategy.getPath(myGameMap,
        (int) Math.round(getEntityPlacement().getX()),
        (int) Math.round(getEntityPlacement().getY()),
        target[0], target[1],
        getEntityPlacement());

    setEntityDirection(dir[0], dir[1]);
  }

  private int[] validateAndGetTargetPosition() {
    int[] targetPosition = myTargetStrategy.getTargetPosition();
    if (targetPosition.length != 2) {
      throw new BfsEntityException("Target position must be of length 2");
    }
    return targetPosition;
  }

  private void setEntityDirection(int dx, int dy) {
    char[] directions = {'R', 'L', 'D', 'U'};
    for (char direction : directions) {
      if (isValidDirection(dx, dy, direction)) {
        setEntityDirection(direction);
        return;
      }
    }
  }

  private boolean isValidDirection(int dx, int dy, char direction) {
    return switch (direction) {
      case 'R' -> dx > 0 && canMove('R');
      case 'L' -> dx < 0 && canMove('L');
      case 'D' -> dy > 0 && canMove('D');
      case 'U' -> dy < 0 && canMove('U');
      default -> false;
    };
  }


}
