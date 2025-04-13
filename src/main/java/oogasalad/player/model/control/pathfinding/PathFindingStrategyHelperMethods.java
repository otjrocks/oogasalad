package oogasalad.player.model.control.pathfinding;

import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;

public class PathFindingStrategyHelperMethods {

  public static List<int[]> getPreferredNeighbors(GameMap map, int x, int y,
      EntityPlacement thisEntity,
      Direction thisDirection) {
    int baseAngle = thisDirection.getAngle();
    List<Direction> preferredDirs = List.of(
        Direction.fromAngle(baseAngle),
        Direction.fromAngle(baseAngle + 90),
        Direction.fromAngle(baseAngle - 90)
    );

    return getValidNeighborsFromDirections(map, x, y, thisEntity, preferredDirs);
  }

  public static List<int[]> getAllValidNeighbors(GameMap map, int x, int y,
      EntityPlacement thisEntity) {
    List<Direction> allDirs = new ArrayList<>();
    for (Direction dir : Direction.values()) {
      if (!dir.isNone()) {
        allDirs.add(dir);
      }
    }
    return getValidNeighborsFromDirections(map, x, y, thisEntity,
        allDirs);
  }

  private static List<int[]> getValidNeighborsFromDirections(GameMap map, int x, int y,
      EntityPlacement thisEntity, List<Direction> directions) {
    List<int[]> neighbors = new ArrayList<>();
    for (Direction dir : directions) {
      int nx = x + dir.getDx();
      int ny = y + dir.getDy();
      if (map.isValidPosition(nx, ny) && map.isNotBlocked(thisEntity.getTypeString(), nx, ny)) {
        neighbors.add(new int[]{nx, ny});
      }
    }
    return neighbors;
  }

}
