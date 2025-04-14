package oogasalad.player.model.control;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.BfsEntityException;
import oogasalad.player.model.control.pathfinding.EuclideanPathFindingStrategy;
import oogasalad.player.model.control.pathfinding.PathFindingStrategy;
import oogasalad.player.model.control.targetcalculation.TargetStrategy;
import oogasalad.player.model.control.targetcalculation.TargetStrategyFactory;

/**
 * The TargetControlStrategy class implements the ControlStrategy interface and provides
 * functionality for controlling an entity's movement towards a target position on a game map. It
 * uses a pathfinding strategy to determine the optimal path and a target strategy to identify the
 * target position.
 *
 * <p>This class is responsible for:
 * <ul>
 *   <li>Validating and retrieving the target position.</li>
 *   <li>Calculating the path to the target using a pathfinding strategy.</li>
 *   <li>Setting the entity's direction based on the calculated path.</li>
 * </ul>
 *
 * <p>Dependencies:
 * <ul>
 *   <li>{@link GameMap} - Represents the game map the entity is part of.</li>
 *   <li>{@link EntityPlacement} - Contains data related to the entity's placement.</li>
 *   <li>{@link PathFindingStrategy} - Strategy for finding the path to the target.</li>
 *   <li>{@link TargetStrategy} - Strategy for determining the target position.</li>
 * </ul>
 *
 * @author Jessica Chen
 */
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
    myPathFindingStrategy = new EuclideanPathFindingStrategy();
    myTargetStrategy = TargetStrategyFactory.createTargetStrategy(entityPlacement, myGameMap);
  }

  /**
   * Create a BFS strategy.
   *
   * @param gameMap             The game map this entity is a part of.
   * @param entityPlacement     The data to include with this entity.
   * @param pathFindingStrategy fix path finding strategy for test, in the future make it with
   *                            factory like with target startegy
   */
  public TargetControlStrategy(GameMap gameMap, EntityPlacement entityPlacement,
      PathFindingStrategy pathFindingStrategy) {
    myEntityPlacement = entityPlacement;
    myGameMap = gameMap;
    myPathFindingStrategy = pathFindingStrategy;
    myTargetStrategy = TargetStrategyFactory.createTargetStrategy(entityPlacement, myGameMap);
  }


  @Override
  public void update(Entity entity) {
    int[] target = validateAndGetTargetPosition();
//    int[] target = new int[]{0, 0};

    int[] dir = myPathFindingStrategy.getPath(myGameMap,
        (int) Math.round(myEntityPlacement.getX()),
        (int) Math.round(myEntityPlacement.getY()),
        target[0], target[1],
        myEntityPlacement, entity.getEntityDirection());

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


  private boolean isValidDirection(int dx, int dy, Direction direction, Entity entity) {
    int normDx = Integer.compare(dx, 0);
    int normDy = Integer.compare(dy, 0);
    return normDx == direction.getDx() && normDy == direction.getDy() && entity.canMove(direction);
  }

}
