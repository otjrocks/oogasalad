package oogasalad.player.model.control.pathfinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import oogasalad.engine.enums.Directions;
import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;

/**
 * A pathfinding strategy using Breadth-First Search. Returns the next direction to move from start
 * to target position, returns the direction as (dx, dy) or (0, 0) if target should remain in same
 * location.
 *
 * @author Jessica Chen
 */
public class BfsPathFindingStrategy implements PathFindingStrategy {

  @Override
  public int[] getPath(GameMap map, int startX, int startY, int targetX, int targetY,
      EntityPlacement thisEntity, Direction thisDirection) {
    if (!map.isValidPosition(startX, startY) || !map.isValidPosition(targetX, targetY)) {
      return new int[]{0, 0};
    }

    Node targetNode = bfs(map, startX, startY, targetX, targetY, thisEntity, thisDirection);
    return buildDirection(startX, startY, targetNode);
  }

  private Node bfs(GameMap map, int startX, int startY, int targetX, int targetY,
      EntityPlacement thisEntity, Direction thisDirection) {
    Queue<Node> queue = new LinkedList<>();
    Set<String> visited = new HashSet<>();
    Node startNode = new Node(startX, startY, null, 0);

    enqueue(queue, visited, startNode);

    while (!queue.isEmpty()) {
      Node current = queue.poll();

      if (isTarget(current, targetX, targetY)) {
        return current;
      }

      processNeighbors(map, current, queue, visited, thisEntity, thisDirection);
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

  private void processNeighbors(GameMap map, Node current, Queue<Node> queue, Set<String> visited,
      EntityPlacement thisEntity, Direction thisDirection) {
    List<int[]> neighbors;
    if (current.depth == 0) {
      neighbors = getPreferredNeighbors(map, current.x, current.y, thisEntity, thisDirection);
    } else {
      neighbors = getAllValidNeighbors(map, current.x, current.y, thisEntity);
    }

    for (int[] neighbor : neighbors) {
      int newX = neighbor[0];
      int newY = neighbor[1];
      String neighborKey = key(newX, newY);

      if (!visited.contains(neighborKey)) {
        enqueue(queue, visited, new Node(newX, newY, current, current.depth + 1));
      }
    }
  }

  private List<int[]> reconstructPath(Node node) {
    List<int[]> path = new LinkedList<>();
    while (node.parent != null) {
      path.addFirst(new int[]{node.x, node.y});
      node = node.parent;
    }
    return path;
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

  private List<int[]> getPreferredNeighbors(GameMap map, int x, int y, EntityPlacement thisEntity,
      Direction thisDirection) {
    List<int[]> neighbors = new ArrayList<>();

    if (thisDirection == null || thisDirection.isNone() || thisDirection.getAngle() == null) {
      return getAllValidNeighbors(map, x, y, thisEntity);
    }

    int baseAngle = thisDirection.getAngle();
    List<Directions.Direction> preferredDirs = List.of(
        Directions.Direction.fromAngle(baseAngle),         // Forward
        Directions.Direction.fromAngle(baseAngle + 90),    // Right
        Directions.Direction.fromAngle(baseAngle - 90)     // Left
    );

    for (Directions.Direction dir : preferredDirs) {
      int nx = x + dir.getDx();
      int ny = y + dir.getDy();
      if (map.isValidPosition(nx, ny) && map.isNotBlocked(thisEntity.getTypeString(), nx, ny)) {
        neighbors.add(new int[]{nx, ny});
      }
    }

    return neighbors;
  }

  private List<int[]> getAllValidNeighbors(GameMap map, int x, int y, EntityPlacement thisEntity) {
    List<int[]> neighbors = new ArrayList<>();
    for (Directions.Direction dir : Directions.Direction.values()) {
      if (dir.isNone()) continue;
      int nx = x + dir.getDx();
      int ny = y + dir.getDy();
      if (map.isValidPosition(nx, ny) && map.isNotBlocked(thisEntity.getTypeString(), nx, ny)) {
        neighbors.add(new int[]{nx, ny});
      }
    }
    return neighbors;
  }

  private String key(int x, int y) {
    return x + "," + y;
  }

  private record Node(int x, int y, Node parent, int depth) { }
}
