package oogasalad.player.model.control;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.controlConfig.ConditionalControlConfig;
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.BfsEntityException;
import oogasalad.player.model.control.pathfinding.PathFindingStrategy;
import oogasalad.player.model.control.pathfinding.PathFindingStrategyFactory;
import oogasalad.player.model.control.targetcalculation.TargetStrategy;
import oogasalad.player.model.control.targetcalculation.TargetStrategyFactory;
import oogasalad.player.model.exceptions.ControlStrategyException;


/**
 * The ConditionalControlStrategy class implements the ControlStrategy interface and provides
 * a mechanism to control entities in a game based on specific conditions. It uses different
 * pathfinding strategies depending on whether the target is within a specified radius or outside it.
 */
public class ConditionalControlStrategy implements ControlStrategy {

  private final GameMap myGameMap;
  private final EntityPlacement myEntityPlacement;
  private final PathFindingStrategy myPathFindingStrategyInRadius;
  private final PathFindingStrategy myPathFindingStrategyOutRadius;
  private final TargetStrategy myTargetStrategy;
  private int myRadius;

  private final String NOT_CONDITIONALCONFIG_EXCEPTION =
      "Config is not a ConditionalControlConfig";

  /**
   * Constructs a ConditionalControlStrategy object that determines control behavior
   * based on specific conditions and strategies.
   *
   * @param gameMap the GameMap object representing the current state of the game map
   * @param entityPlacement the EntityPlacement object used to manage entity placement
   * @param config the ControlConfig object containing configuration details for the strategy
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
      throw new ControlStrategyException(NOT_CONDITIONALCONFIG_EXCEPTION);
    }
  }

  private PathFindingStrategy getPathFindingStrategy2(ControlConfig config) {
    if (config instanceof ConditionalControlConfig conditionalConfig) {
      return PathFindingStrategyFactory.createPathFindingStrategy(
          conditionalConfig.pathFindingStrategyOutRadius());
    } else {
      throw new ControlStrategyException(NOT_CONDITIONALCONFIG_EXCEPTION);
    }
  }

  private int getRadius(ControlConfig config) {
    if (config instanceof ConditionalControlConfig conditionalConfig) {
      return conditionalConfig.radius();
    } else {
      throw new ControlStrategyException(NOT_CONDITIONALCONFIG_EXCEPTION);
    }
  }


  @Override
  public void update(Entity entity) {
    int[] target = ControlStrategyHelperMethods.validateAndGetTargetPosition(myTargetStrategy);

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

    ControlStrategyHelperMethods.setEntityDirection(dir[0], dir[1], entity);
  }





}
