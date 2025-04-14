package oogasalad.player.model.control;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.controlConfig.ConditionalControlConfig;
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.controlConfig.TargetControlConfig;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.BfsEntityException;
import oogasalad.player.model.control.pathfinding.PathFindingStrategy;
import oogasalad.player.model.control.pathfinding.PathFindingStrategyFactory;
import oogasalad.player.model.control.targetcalculation.TargetStrategy;
import oogasalad.player.model.control.targetcalculation.TargetStrategyFactory;
import oogasalad.player.model.exceptions.ControlStrategyException;


public class ConditionalControlStrategy implements ControlStrategy {

  private final GameMap myGameMap;
  private final EntityPlacement myEntityPlacement;
  private final PathFindingStrategy myPathFindingStrategyInRadius;
  private final PathFindingStrategy myPathFindingStrategyOutRadius;
  private final TargetStrategy myTargetStrategy;
  private int myRadius;

  /**
   * Create a BFS strategy.
   *
   * @param gameMap         The game map this entity is a part of.
   * @param entityPlacement The data to include with this entity.
   */
  public ConditionalControlStrategy(GameMap gameMap, EntityPlacement entityPlacement,
      ControlConfig config) {
    myEntityPlacement = entityPlacement;
    myGameMap = gameMap;
    myPathFindingStrategyInRadius = getPathFindingStrategy1(config);
    myPathFindingStrategyOutRadius = getPathFindingStrategy2(config);
    myRadius = getRadius(config);
    myTargetStrategy = TargetStrategyFactory.createTargetStrategy(entityPlacement, myGameMap);
  }

  private PathFindingStrategy getPathFindingStrategy1(ControlConfig config) {
    if (config instanceof ConditionalControlConfig conditionalConfig) {
      return PathFindingStrategyFactory.createPathFindingStrategy(
          conditionalConfig.pathFindingStrategyInRadius());
    } else {
      throw new ControlStrategyException("Config is not a ConditionalControlConfig");
    }
  }

  private PathFindingStrategy getPathFindingStrategy2(ControlConfig config) {
    if (config instanceof ConditionalControlConfig conditionalConfig) {
      return PathFindingStrategyFactory.createPathFindingStrategy(
          conditionalConfig.pathFindingStrategyOutRadius());
    } else {
      throw new ControlStrategyException("Config is not a ConditionalControlConfig");
    }
  }

  private int getRadius(ControlConfig config) {
    if (config instanceof ConditionalControlConfig conditionalConfig) {
      return conditionalConfig.radius();
    } else {
      throw new ControlStrategyException("Config is not a ConditionalControlConfig");
    }
  }


  @Override
  public void update(Entity entity) {
    int[] target = validateAndGetTargetPosition();

    double currentX = myEntityPlacement.getX();
    double currentY = myEntityPlacement.getY();

    double distance = Math.sqrt(Math.pow(target[0] - currentX, 2) + Math.pow(target[1] - currentY, 2));

    PathFindingStrategy strategy;
    if (distance <= myRadius) {
      strategy = myPathFindingStrategyInRadius;
    } else {
      strategy = myPathFindingStrategyOutRadius;
    }

    int[] dir = strategy.getPath(myGameMap,
        (int) Math.round(currentX),
        (int) Math.round(currentY),
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
