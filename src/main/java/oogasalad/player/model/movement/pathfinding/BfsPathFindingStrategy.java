package oogasalad.player.model.movement.pathfinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import oogasalad.engine.model.GameMap;

/**
 * A pathfinding strategy using Breadth-First Search. Returns the next direction to move from
 * start to target position, returns the direction as (dx, dy) or (0, 0) if target should
 * remain in same location.
 *
 * @author Jessica Chen
 */
public class BfsPathFindingStrategy implements PathFindingStrategy {

  @Override
  public int[] getPath(GameMap map, int startX, int startY, int targetX, int targetY) {
    if (!isValidPosition(map, startX, startY) || !isValidPosition(map, targetX, targetY)) {
      return new int[]{0, 0};
    }

    Node targetNode = bfs(map, startX, startY, targetX, targetY);
    return buildDirection(startX, startY, targetNode);
  }

  private Node bfs(GameMap map, int startX, int startY, int targetX, int targetY) {
    Queue<Node> queue = new LinkedList<>();
    Set<String> visited = new HashSet<>();
    Node startNode = new Node(startX, startY, null);

    queue.offer(startNode);
    visited.add(key(startX, startY));

    while (!queue.isEmpty()) {
      Node current = queue.poll();
      if (current.x == targetX && current.y == targetY) {
        return current;
      }

      for (int[] neighbor : getNeighbors(map, current.x, current.y)) {
        int newX = neighbor[0];
        int newY = neighbor[1];
        String key = key(newX, newY);

        if (!visited.contains(key)) {
          visited.add(key);
          queue.offer(new Node(newX, newY, current));
        }
      }
    }
    return null; // no path found
  }

  private int[] buildDirection(int startX, int startY, Node targetNode) {
    if (targetNode == null) return new int[]{0, 0};

    List<int[]> path = reconstructPath(targetNode);
    if (path.size() < 2) return new int[]{0, 0}; // already at destination

    int[] nextPos = path.get(1); // step after start
    return new int[]{nextPos[0] - startX, nextPos[1] - startY};
  }

  private List<int[]> reconstructPath(Node node) {
    LinkedList<int[]> path = new LinkedList<>();
    while (node != null) {
      path.addFirst(new int[]{node.x, node.y});
      node = node.parent;
    }
    return path;
  }

  private List<int[]> getNeighbors(GameMap map, int x, int y) {
    List<int[]> neighbors = new ArrayList<>();
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    for (int[] d : directions) {
      int nx = x + d[0];
      int ny = y + d[1];
      if (isValidPosition(map, nx, ny)) {
        neighbors.add(new int[]{nx, ny});
      }
    }

    return neighbors;
  }

  private boolean isValidPosition(GameMap map, int x, int y) {
    return x >= 0 && y >= 0 && x < map.getWidth() && y < map.getHeight();
  }

  private String key(int x, int y) {
    return x + "," + y;
  }

  private record Node (int x, int y, Node parent) {
  }
}
