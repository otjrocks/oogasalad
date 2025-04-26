package oogasalad.player.model.strategies.control.pathfinding;

import java.util.*;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;

/**
 * GreedyPathFindingStrategy now searches a few steps in each direction
 * to find the nearest "dot" even if not directly in line.
 */
public class GreedyPathFindingStrategy implements PathFindingStrategyInterface {

  private final Random random = new Random();
  private static final String TARGET_ENTITY_NAME = "dot";
  private static final int SEARCH_DEPTH = 15; // how far to look in each direction

  @Override
  public int[] getPath(GameMapInterface map, int startX, int startY, int targetX, int targetY,
      EntityPlacement thisEntity, Direction thisDirection) {

    List<int[]> possibleDirections = PathFindingStrategyHelperMethods.getValidDirections(
        map, startX, startY, thisEntity, thisDirection);

    if (possibleDirections.isEmpty()) {
      return new int[]{0, 0};
    }

    int minDistance = Integer.MAX_VALUE;
    List<int[]> bestDirections = new ArrayList<>();

    for (int[] direction : possibleDirections) {
      int dx = direction[0] - startX;
      int dy = direction[1] - startY;
      int distance = bfsToNearestDot(map, startX + dx, startY + dy, dx, dy);

      if (distance < minDistance) {
        minDistance = distance;
        bestDirections.clear();
        bestDirections.add(direction);
      } else if (distance == minDistance) {
        bestDirections.add(direction);
      }
    }

    List<int[]> chosenList = bestDirections.isEmpty() ? possibleDirections : bestDirections;
    int[] chosenDirection = chosenList.get(random.nextInt(chosenList.size()));

    return new int[]{chosenDirection[0] - startX, chosenDirection[1] - startY};
  }

  private int bfsToNearestDot(GameMapInterface map, int startX, int startY, int dirX, int dirY) {
    Queue<int[]> queue = new LinkedList<>();
    Set<String> visited = new HashSet<>();
    queue.add(new int[]{startX, startY, 1}); // {x, y, distance}
    visited.add(startX + "," + startY);

    while (!queue.isEmpty()) {
      int[] current = queue.poll();
      int x = current[0];
      int y = current[1];
      int dist = current[2];

      if (dist > SEARCH_DEPTH) {
        continue;
      }

      if (!map.isValidPosition(x, y)) {
        continue;
      }

      Optional<Entity> entity = map.getEntityAt(x, y);
      if (entity.isPresent() && TARGET_ENTITY_NAME.equalsIgnoreCase(entity.get().getEntityPlacement().getTypeString())) {
        return dist;
      }

      // Explore straight + slight deviations (forward, left, right)
      int[][] deltas = new int[][]{
          {dirX, dirY}, // continue forward
          {dirY, -dirX}, // turn right
          {-dirY, dirX}  // turn left
      };

      for (int[] delta : deltas) {
        int newX = x + delta[0];
        int newY = y + delta[1];
        String key = newX + "," + newY;

        if (!visited.contains(key)) {
          visited.add(key);
          queue.add(new int[]{newX, newY, dist + 1});
        }
      }
    }

    return Integer.MAX_VALUE; // no dot found within depth
  }
}
