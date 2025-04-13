package oogasalad.player.model.control.pathfinding;

import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;

/**
 * A utility class that provides helper methods for pathfinding strategies.
 * This class includes methods to retrieve neighboring positions on a game map
 * based on specific criteria, such as preferred directions or all valid directions.
 * 
 * <p>Methods in this class are designed to work with a {@code GameMap} object
 * and take into account the position, entity placement, and direction to determine
 * valid neighboring positions.
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

  /**
 * Retrieves a list of preferred neighboring positions around a given position
 * based on the specified direction. The preferred neighbors are determined by
 * the base direction and its perpendicular directions (90 degrees clockwise
 * and counterclockwise).
 *
 * @param map          the game map to check for valid positions
 * @param x            the x-coordinate of the current position
 * @param y            the y-coordinate of the current position
 * @param thisEntity   the entity for which neighbors are being determined
 * @param thisDirection the direction used to determine preferred neighbors
 * @return a list of valid neighboring positions as int arrays [nx, ny]
 */
  public static List<int[]> getPreferredNeighbors(GameMap map, int x, int y,
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

  /**
   * Retrieves a list of all valid neighboring positions for a given entity on the game map.
   * A valid neighbor is determined based on the entity's placement rules and the map's constraints.
   *
   * @param map the game map to evaluate neighbors on
   * @param x the x-coordinate of the current position
   * @param y the y-coordinate of the current position
   * @param thisEntity the entity whose placement rules are being considered
   * @return a list of valid neighboring positions, where each position is represented as an array of two integers [x, y]
   */
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


  /**
   * Determines the valid directions for an entity to move on the game map based on its current position
   * and movement preferences. If a specific direction is provided, it returns the preferred neighbors
   * in that direction. Otherwise, it returns all valid neighboring positions.
   *
   * @param map the game map representing the current state of the game
   * @param startX the x-coordinate of the starting position
   * @param startY the y-coordinate of the starting position
   * @param thisEntity the entity for which valid directions are being determined
   * @param thisDirection the preferred direction of movement, or null/Direction.NONE for no preference
   * @return a list of valid neighboring positions represented as arrays of integers [x, y]
   */
  public static List<int[]> getValidDirections(GameMap map, int startX, int startY,
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
