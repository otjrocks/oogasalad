package oogasalad.player.model.control.pathfinding;

import java.util.*;
import oogasalad.engine.enums.Directions;
import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;

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

      List<int[]> neighbors = getNeighbors(map, current, thisEntity, thisDirection);
      for (int[] neighbor : neighbors) {
        int newX = neighbor[0];
        int newY = neighbor[1];
        String neighborKey = key(newX, newY);

        if (!visited.contains(neighborKey)) {
          enqueue(queue, visited, new Node(newX, newY, current, current.depth + 1));
        }
      }
    }

    return null;
  }

  private List<int[]> getNeighbors(GameMap map, Node current, EntityPlacement thisEntity,
      Direction thisDirection) {
    if (current.depth == 0 && thisDirection != null && !thisDirection.isNone() && thisDirection.getAngle() != null) {
      return getPreferredNeighbors(map, current.x, current.y, thisEntity, thisDirection);
    }
    return getAllValidNeighbors(map, current.x, current.y, thisEntity);
  }

  private void enqueue(Queue<Node> queue, Set<String> visited, Node node) {
    queue.offer(node);
    visited.add(key(node.x, node.y));
  }

  private boolean isTarget(Node node, int targetX, int targetY) {
    return node.x == targetX && node.y == targetY;
  }

  private List<int[]> getPreferredNeighbors(GameMap map, int x, int y, EntityPlacement thisEntity,
      Direction thisDirection) {
    int baseAngle = thisDirection.getAngle();
    List<Direction> preferredDirs = List.of(
        Direction.fromAngle(baseAngle),
        Direction.fromAngle(baseAngle + 90),
        Direction.fromAngle(baseAngle - 90)
    );
    return getValidNeighborsFromDirections(map, x, y, thisEntity, preferredDirs);
  }

  private List<int[]> getAllValidNeighbors(GameMap map, int x, int y, EntityPlacement thisEntity) {
    List<Direction> allDirs = new ArrayList<>();
    for (Direction dir : Direction.values()) {
      if (!dir.isNone()) {
        allDirs.add(dir);
      }
    }
    return getValidNeighborsFromDirections(map, x, y, thisEntity, allDirs);
  }

  private List<int[]> getValidNeighborsFromDirections(GameMap map, int x, int y,
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

  private List<int[]> reconstructPath(Node node) {
    LinkedList<int[]> path = new LinkedList<>();
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

  private String key(int x, int y) {
    return x + "," + y;
  }

  private record Node(int x, int y, Node parent, int depth) {
  }
}