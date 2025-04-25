package oogasalad.player.model.strategies.control;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.controlConfig.ConditionalControlConfigRecord;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.api.PathFindingStrategyFactory;
import oogasalad.player.model.api.TargetStrategyFactory;
import oogasalad.player.model.exceptions.ControlStrategyException;
import oogasalad.player.model.strategies.control.pathfinding.PathFindingStrategyInterface;
import oogasalad.player.model.strategies.control.targetcalculation.TargetStrategyInterface;


/**
 * The ConditionalControlStrategy class implements the ControlStrategy interface and provides a
 * mechanism to control entities in a game based on specific conditions. It uses different
 * pathfinding strategies depending on whether the target is within a specified radius or outside
 * it.
 */
public class ConditionalControlStrategy implements ControlStrategyInterface {

  private final GameMapInterface myGameMap;
  private final EntityPlacement myEntityPlacement;
  private final PathFindingStrategyInterface myPathFindingStrategyInRadius;
  private final PathFindingStrategyInterface myPathFindingStrategyOutRadius;
  private final TargetStrategyInterface myTargetStrategy;
  private final int myRadius;

  private static final String NOT_CONDITIONAL_CONFIG_EXCEPTION =
      "Config is not a ConditionalControlConfig";

  /**
   * Constructs a ConditionalControlStrategy object that determines control behavior based on
   * specific conditions and strategies.
   *
   * @param gameMap         the GameMap object representing the current state of the game map
   * @param entityPlacement the EntityPlacement object used to manage entity placement
   * @param config          the ControlConfig object containing configuration details for the
   *                        strategy
   */
  public ConditionalControlStrategy(GameMapInterface gameMap, EntityPlacement entityPlacement,
      ControlConfigInterface config) {
    myEntityPlacement = entityPlacement;
    myGameMap = gameMap;
    myPathFindingStrategyInRadius = getPathFindingStrategy1(config);
    myPathFindingStrategyOutRadius = getPathFindingStrategy2(config);
    myRadius = getRadius(config);
    myTargetStrategy = TargetStrategyFactory.createTargetStrategy(entityPlacement, myGameMap);
  }

  private PathFindingStrategyInterface getPathFindingStrategy1(ControlConfigInterface config) {
    if (config instanceof ConditionalControlConfigRecord conditionalConfig) {
      return PathFindingStrategyFactory.createPathFindingStrategy(
          conditionalConfig.pathFindingStrategyInRadius());
    } else {
      throw new ControlStrategyException(NOT_CONDITIONAL_CONFIG_EXCEPTION);
    }
  }

  private PathFindingStrategyInterface getPathFindingStrategy2(ControlConfigInterface config) {
    if (config instanceof ConditionalControlConfigRecord conditionalConfig) {
      return PathFindingStrategyFactory.createPathFindingStrategy(
          conditionalConfig.pathFindingStrategyOutRadius());
    } else {
      throw new ControlStrategyException(NOT_CONDITIONAL_CONFIG_EXCEPTION);
    }
  }

  private int getRadius(ControlConfigInterface config) {
    if (config instanceof ConditionalControlConfigRecord conditionalConfig) {
      return conditionalConfig.radius();
    } else {
      throw new ControlStrategyException(NOT_CONDITIONAL_CONFIG_EXCEPTION);
    }
  }


  @Override
  public void update(Entity entity) {
    int[] target = ControlStrategyHelperMethods.validateAndGetTargetPosition(myTargetStrategy);

    PathFindingStrategyInterface pathFindingStrategy = getPathFindingStrategy(
        myEntityPlacement.getX(),
        myEntityPlacement.getY(), target[0], target[1]);

    ControlStrategyHelperMethods.getDirectionFromTargetAndPath(myGameMap, entity, myEntityPlacement,
        myTargetStrategy, pathFindingStrategy);
  }

  private PathFindingStrategyInterface getPathFindingStrategy(double currentX, double currentY,
      int targetX,
      int targetY) {

    double distance = Math.sqrt(
        Math.pow(targetX - currentX, 2) + Math.pow(targetY - currentY, 2));

    if (distance <= myRadius) {
      return myPathFindingStrategyInRadius;
    } else {
      return myPathFindingStrategyOutRadius;
    }
  }


}
