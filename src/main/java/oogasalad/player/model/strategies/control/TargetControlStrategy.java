package oogasalad.player.model.strategies.control;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.records.config.model.controlConfig.TargetControlConfigRecord;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.api.PathFindingStrategyFactory;
import oogasalad.player.model.api.TargetStrategyFactory;
import oogasalad.player.model.exceptions.ControlStrategyException;
import oogasalad.player.model.strategies.control.pathfinding.PathFindingStrategyInterface;
import oogasalad.player.model.strategies.control.targetcalculation.TargetStrategyInterface;

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
 *   <li>{@link GameMapInterface} - Represents the game map the entity is part of.</li>
 *   <li>{@link EntityPlacement} - Contains data related to the entity's placement.</li>
 *   <li>{@link PathFindingStrategyInterface} - Strategy for finding the path to the target.</li>
 *   <li>{@link TargetStrategyInterface} - Strategy for determining the target position.</li>
 * </ul>
 *
 * @author Jessica Chen
 */
public class TargetControlStrategy implements ControlStrategyInterface {

  private final GameMapInterface myGameMap;
  private final EntityPlacement myEntityPlacement;
  private final PathFindingStrategyInterface myPathFindingStrategy;
  private final TargetStrategyInterface myTargetStrategy;

  /**
   * Create a BFS strategy.
   *
   * @param gameMap         The game map this entity is a part of.
   * @param entityPlacement The data to include with this entity.
   */
  public TargetControlStrategy(GameMapInterface gameMap, EntityPlacement entityPlacement,
      ControlConfigInterface config) {
    myEntityPlacement = entityPlacement;
    myGameMap = gameMap;
    myPathFindingStrategy = getPathFindingStrategy(config);
    myTargetStrategy = TargetStrategyFactory.createTargetStrategy(entityPlacement, myGameMap);
  }

  private PathFindingStrategyInterface getPathFindingStrategy(ControlConfigInterface config) {
    if (config instanceof TargetControlConfigRecord record) {
      return PathFindingStrategyFactory.createPathFindingStrategy(
          record.pathFindingStrategy());
    } else {
      throw new ControlStrategyException("Config is not a TargetControlConfig");
    }
  }


  @Override
  public void update(Entity entity) {
    ControlStrategyHelperMethods.getDirectionFromTargetAndPath(myGameMap, entity, myEntityPlacement,
        myTargetStrategy, myPathFindingStrategy);
  }

}
