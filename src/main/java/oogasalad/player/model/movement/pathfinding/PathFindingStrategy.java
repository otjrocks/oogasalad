package oogasalad.player.model.movement.pathfinding;

import java.util.List;
import oogasalad.engine.model.GameMap;

public interface PathFindingStrategy {
  List<int[]> getPath(GameMap map, int startX, int startY, int targetX, int targetY);
}
