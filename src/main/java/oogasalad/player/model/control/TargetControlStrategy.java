package oogasalad.player.model.control;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.controlConfig.TargetControlConfig;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.BfsEntityException;
import oogasalad.player.model.control.pathfinding.PathFindingStrategy;
import oogasalad.player.model.control.pathfinding.PathFindingStrategyFactory;
import oogasalad.player.model.control.targetcalculation.TargetStrategy;
import oogasalad.player.model.control.targetcalculation.TargetStrategyFactory;
import oogasalad.player.model.exceptions.ControlStrategyException;

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
  public TargetControlStrategy(GameMap gameMap, EntityPlacement entityPlacement,
      ControlConfig config) {
    myEntityPlacement = entityPlacement;
    myGameMap = gameMap;
    myPathFindingStrategy = getPathFindingStrategy(config);
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

  private PathFindingStrategy getPathFindingStrategy(ControlConfig config) {
    if (config instanceof TargetControlConfig targetConfig) {
      return PathFindingStrategyFactory.createPathFindingStrategy(
          targetConfig.pathFindingStrategy());
    } else {
      throw new ControlStrategyException("Config is not a TargetControlConfig");
    }
  }


  @Override
  public void update(Entity entity) {
    int[] target = ControlStrategyHelperMethods.validateAndGetTargetPosition(myTargetStrategy);
//    int[] target = new int[]{0, 0};

    int[] dir = myPathFindingStrategy.getPath(myGameMap,
        (int) Math.round(myEntityPlacement.getX()),
        (int) Math.round(myEntityPlacement.getY()),
        target[0], target[1],
        myEntityPlacement, entity.getEntityDirection());

    ControlStrategyHelperMethods.setEntityDirection(dir[0], dir[1], entity);
  }

}
