package oogasalad.player.model.control.pathfinding;

import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;

/**
 * A utility class that provides helper methods for pathfinding strategies. This class includes
 * methods to retrieve neighboring positions on a game map based on specific criteria, such as
 * preferred directions or all valid directions.
 *
 * <p>Methods in this class are designed to work with a {@code GameMap} object
 * and take into account the position, entity placement, and direction to determine valid
 * neighboring positions.
 *
 * <p>Key functionalities include:
 * <ul>
 *   <li>Retrieving preferred neighbors based on a given direction.</li>
 *   <li>Retrieving all valid neighbors around a given position.</li>
 *   <li>Filtering neighbors based on map validity and entity placement constraints.</li>
 * </ul>
 *
 * <p>This class is intended to be used as a utility and should not be instantiated.
 */
public class PathFindingStrategyHelperMethods {

  static List<int[]> getPreferredNeighbors(GameMap map, int x, int y,
      EntityPlacement thisEntity,
      Direction thisDirection) {
    if (thisDirection == Direction.NONE) {
      return new ArrayList<>();
    }

    int baseAngle = thisDirection.getAngle();
    List<Direction> preferredDirs = List.of(
        Direction.fromAngle(baseAngle),
        Direction.fromAngle(baseAngle + 90),
        Direction.fromAngle(baseAngle - 90)
    );

    return getValidNeighborsFromDirections(map, x, y, thisEntity, preferredDirs);
  }

  static List<int[]> getAllValidNeighbors(GameMap map, int x, int y,
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


  static List<int[]> getValidDirections(GameMap map, int startX, int startY,
      EntityPlacement thisEntity, Direction thisDirection) {
    if (thisDirection == null || thisDirection == Direction.NONE) {
      return getAllValidNeighbors(map, startX, startY, thisEntity);
    } else {
      return getPreferredNeighbors(map, startX, startY, thisEntity, thisDirection);
    }
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
