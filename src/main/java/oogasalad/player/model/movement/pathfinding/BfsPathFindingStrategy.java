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

    enqueue(queue, visited, startNode);

    while (!queue.isEmpty()) {
      Node current = queue.poll();

      if (isTarget(current, targetX, targetY)) {
        return current;
      }

      processNeighbors(map, current, queue, visited, targetX, targetY);
    }

    return null; // no path found
  }

  private void enqueue(Queue<Node> queue, Set<String> visited, Node node) {
    queue.offer(node);
    visited.add(key(node.x, node.y));
  }

  private boolean isTarget(Node node, int targetX, int targetY) {
    return node.x == targetX && node.y == targetY;
  }

  private void processNeighbors(GameMap map, Node current, Queue<Node> queue, Set<String> visited, int targetX, int targetY) {
    for (int[] neighbor : getNeighbors(map, current.x, current.y, targetX, targetY)) {
      int newX = neighbor[0];
      int newY = neighbor[1];
      String neighborKey = key(newX, newY);

      if (!visited.contains(neighborKey)) {
        enqueue(queue, visited, new Node(newX, newY, current));
      }
    }
  }

  private int[] buildDirection(int startX, int startY, Node targetNode) {
    if (targetNode == null) {
      return new int[]{0, 0};
    }

    List<int[]> path = reconstructPath(targetNode);
    if (path.isEmpty()) {
      return new int[]{0, 0};
    }

    int[] nextPos = path.getFirst();
    return new int[]{nextPos[0] - startX, nextPos[1] - startY};
  }

  private List<int[]> reconstructPath(Node node) {
    List<int[]> path = new LinkedList<>();
    while (node.parent != null) {
      path.addFirst(new int[]{node.x, node.y});
      node = node.parent;
    }
    return path;
  }

  private List<int[]> getNeighbors(GameMap map, int x, int y, int targetX, int targetY) {
    List<int[]> neighbors = new ArrayList<>();
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    for (int[] d : directions) {
      int nx = x + d[0];
      int ny = y + d[1];
      if (isValidPosition(map, nx, ny)) {
        // Only allow non-target neighbors if they are empty
        if ((nx == targetX && ny == targetY) || isEmpty(map, nx, ny)) {
          neighbors.add(new int[]{nx, ny});
        }
      }
    }

    return neighbors;
  }

  private boolean isValidPosition(GameMap map, int x, int y) {
    return x >= 0 && y >= 0 && x < map.getWidth() && y < map.getHeight();
  }

  private boolean isEmpty(GameMap map, int x, int y) {
    return map.getEntityAt(x, y).isEmpty();
  }

  private String key(int x, int y) {
    return x + "," + y;
  }

  private record Node(int x, int y, Node parent) {
  }
}