package oogasalad.player.model.strategies.control.pathfinding;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.model.GameMapInterface;

/**
 * An interface to describe path finding strategies for entities, given a game map.
 *
 * @author Jessica Chen
 */
public interface PathFindingStrategyInterface {

  /**
   * Get the path of a path finding algorithm.
   *
   * @param map        The game map to find a path in.
   * @param startX     The starting x in the map.
   * @param startY     The starting y in the map.
   * @param targetX    The target's x value.
   * @param targetY    The target's y value.
   * @param thisEntity The entityPlacement of the entity calling this strategy.
   * @return A int[] that represents the first direction on the path as (dx, dy).
   */
  int[] getPath(GameMapInterface map, int startX, int startY, int targetX, int targetY,
      EntityPlacement thisEntity, Direction thisDirection);
}
