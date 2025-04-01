package oogasalad.player.model.movement.pathfinding;

import java.util.List;
import oogasalad.player.model.movement.Grid;

/**
 * An interface to handle path finding.
 *
 * @author Jessica Chen
 */
public interface PathFindingStrategy {

  /**
   * Get the path for the provided parameters.
   *
   * @param map     The grid representation of the game map.
   * @param startX  The starting location for the BFS.
   * @param startY  The starting location for the BFS.
   * @param targetX The target location x for the BFS.
   * @param targetY The target location y for the BFS.
   * @return An int[] representing the path from start position to target.
   */
  List<int[]> getPath(Grid map, int startX, int startY, int targetX, int targetY);
}
