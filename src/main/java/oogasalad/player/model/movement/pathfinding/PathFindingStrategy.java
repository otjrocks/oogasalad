package oogasalad.player.model.movement.pathfinding;

import oogasalad.engine.model.GameMap;

/**
 * An interface to describe path finding strategies for entities, given a game map.
 *
 * @author Jessica Chen
 */
public interface PathFindingStrategy {

  /**
   * Get the path of a path finding algorithm.
   *
   * @param map     The game map to find a path in.
   * @param startX  The starting x in the map.
   * @param startY  The starting y in the map.
   * @param targetX The target's x value.
   * @param targetY The target's y value.
   * @return A int[] that represents the first direction on the path as (dx, dy).
   */
  int[] getPath(GameMap map, int startX, int startY, int targetX, int targetY);

}
