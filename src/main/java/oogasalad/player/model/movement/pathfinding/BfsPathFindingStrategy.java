package oogasalad.player.model.movement.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import oogasalad.engine.model.GameMap;

/**
 * A path finding strategy implementation that follows the rules of BFS.
 *
 * @author Jessica Chen
 */
public class BfsPathFindingStrategy implements PathFindingStrategy {

  @Override
  public List<int[]> getPath(GameMap map, int startX, int startY, int targetX, int targetY) {

    // don't know if this method needs to check for valid positions
    if (!isValidPosition(map, startX, startY) || !isValidPosition(map, targetX, targetY)) {
      return List.of();
    }

    // standard BFS algorithm
    List<int[]> current = bfs(map, startX, startY, targetX, targetY);
    if (current != null) {
      return current;
    }

    // otherwise it just no move
    return List.of();
  }

  private List<int[]> bfs(GameMap map, int startX, int startY, int targetX, int targetY) {
    Queue<Node> queue = new LinkedList<>();
    queue.offer(new Node(startX, startY, null));

    Set<String> visited = new HashSet<>();
    visited.add(startX + "," + startY);

    List<int[]> current = bfsIterations(map, targetX, targetY, queue, visited);
    if (current != null) {
      return current;
    }
    return null;
  }

  private List<int[]> bfsIterations(GameMap map, int targetX, int targetY, Queue<Node> queue,
      Set<String> visited) {
    while (!queue.isEmpty()) {
      Node current = queue.poll();
      int x = current.x;
      int y = current.y;

      if (x == targetX && y == targetY) {
        // once you get the target retrace to build the path
        return buildPath(current);
      }

      // map ideally gives you all adjacent positions to traverse in that ARE VALID
      // so here not doing any valid checking
      handleNeighbors(map, queue, visited, x, y, current);
    }
    return null;
  }

  private void handleNeighbors(GameMap map, Queue<Node> queue, Set<String> visited, int x, int y,
      Node current) {
    for (int[] neighbor : getAdjacentPositions(map, x, y)) {
      int newX = neighbor[0];
      int newY = neighbor[1];

      String posKey = newX + "," + newY;

      if (!visited.contains(posKey) && isValidPosition(map, newX, newY)) {
        queue.offer(new Node(newX, newY, current));
        visited.add(posKey);
      }
    }
  }

  private List<int[]> buildPath(Node target) {

    // reconstruct path tree from BFS traversal list because we love algorithms

    List<int[]> path = new ArrayList<>();
    Node current = target;

    while (current != null) {
      path.add(new int[]{current.x, current.y});
      current = current.parent;
    }

    Collections.reverse(path);
    return path;
  }

  private boolean isValidPosition(GameMap map, int x, int y) {
    return x >= 0 && y >= 0 && x < map.getWidth() && y < map.getHeight() && map.getEntityAt(x, y)
        .isEmpty();
  }

  private List<int[]> getAdjacentPositions(GameMap map, int x, int y) {
    List<int[]> neighbors = new ArrayList<>();

    int[][] directions = {
        {-1, 0},  // Up
        {1, 0},   // Down
        {0, -1},  // Left
        {0, 1}    // Right
    };

    for (int[] dir : directions) {
      int newX = x + dir[0];
      int newY = y + dir[1];

      if (isValidPosition(map, newX, newY)) {
        neighbors.add(new int[]{newX, newY});
      }
    }

    return neighbors;

  }

  // way to keep track of positions and parents so we don't need to do the silly things with like
  // 2 arrays, good part about 330 is you get to pseudocode it

  /**
   * A record to represent a node in a graph.
   *
   * @param x      The x coordinate of a node.
   * @param y      The y coordinate of a node.
   * @param parent The parent of a node.
   */
  private record Node(int x, int y, BfsPathFindingStrategy.Node parent) {

  }
}

