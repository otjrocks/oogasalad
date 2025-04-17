package oogasalad.player.model.strategies.control.pathfinding;

import java.util.*;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.player.model.GameMapInterface;

/**
 * BfsPathFindingStrategy implements the PathFindingStrategy interface using the Breadth-First
 * Search (BFS) algorithm. This strategy calculates the shortest path from a starting position to a
 * target position on a game map. It considers valid positions, entity placement, and directional
 * preferences when determining the path.
 */
public class BfsPathFindingStrategy implements PathFindingStrategyInterface {

  @Override
  public int[] getPath(GameMapInterface map, int startX, int startY, int targetX, int targetY,
      EntityPlacement thisEntity, Direction thisDirection) {
    if (!map.isValidPosition(startX, startY) || !map.isValidPosition(targetX, targetY)) {
      return new int[]{0, 0};
    }

    Node targetNode = bfs(map, startX, startY, targetX, targetY, thisEntity, thisDirection);
    return buildDirection(startX, startY, targetNode);
  }

  private Node bfs(GameMapInterface map, int startX, int startY, int targetX, int targetY,
      EntityPlacement thisEntity, Direction thisDirection) {
    Queue<Node> queue = new LinkedList<>();
    Set<String> visited = new HashSet<>();
    Node startNode = new Node(startX, startY, null, 0);

    enqueue(queue, visited, startNode);

    while (!queue.isEmpty()) {
      Node current = queue.poll();

      if (reachedTarget(current, targetX, targetY)) {
        return current;
      }

      exploreNeighbors(map, current, thisEntity, thisDirection, queue, visited);
    }

    return null;
  }

  private void exploreNeighbors(GameMapInterface map, Node current,
      EntityPlacement thisEntity, Direction thisDirection,
      Queue<Node> queue, Set<String> visited) {
    for (int[] neighbor : getNeighbors(map, current, thisEntity, thisDirection)) {
      processNeighbor(neighbor, current, queue, visited);
    }
  }

  private void processNeighbor(int[] neighbor, Node current,
      Queue<Node> queue, Set<String> visited) {
    int newX = neighbor[0];
    int newY = neighbor[1];
    String key = key(newX, newY);

    if (!visited.contains(key)) {
      Node newNode = new Node(newX, newY, current, current.depth + 1);
      enqueue(queue, visited, newNode);
    }
  }

  private boolean reachedTarget(Node node, int targetX, int targetY) {
    return node.x == targetX && node.y == targetY;
  }


  private List<int[]> getNeighbors(GameMapInterface map, Node current, EntityPlacement thisEntity,
      Direction thisDirection) {
    if (current.depth == 0 && thisDirection != null && !thisDirection.isNone()
        && thisDirection.getAngle() != null) {
      return PathFindingStrategyHelperMethods.getPreferredNeighbors(map, current.x, current.y,
          thisEntity, thisDirection);
    }
    return PathFindingStrategyHelperMethods.getAllValidNeighbors(map, current.x, current.y,
        thisEntity);
  }

  private void enqueue(Queue<Node> queue, Set<String> visited, Node node) {
    queue.offer(node);
    visited.add(key(node.x, node.y));
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

  private String key(int x, int y) {
    return x + "," + y;
  }

  private record Node(int x, int y, Node parent, int depth) {

  }
}