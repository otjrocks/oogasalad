package oogasalad.player.model.movement.pathfinding;

import java.util.List;
import oogasalad.player.model.movement.Grid;

/**
 * An interface to handle path finding.
 *
 * @author Jessica Chen
 */
public interface PathFindingStrategy {
  List<int[]> getPath(Grid map, int startX, int startY, int targetX, int targetY);
}
