package oogasalad.player.model.movement.pathfinding;

import java.util.List;
import oogasalad.player.model.movement.Grid;

public interface PathFindingStrategy {
  List<int[]> getPath(Grid map, int startX, int startY, int targetX, int targetY);
}
