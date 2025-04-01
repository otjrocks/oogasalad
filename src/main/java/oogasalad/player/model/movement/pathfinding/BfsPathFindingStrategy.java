package oogasalad.player.model.movement.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import oogasalad.player.model.movement.Grid;

public class BfsPathFindingStrategy implements PathFindingStrategy {

  @Override
  public List<int[]> getPath(Grid map, int startX, int startY, int targetX, int targetY) {

    // don't know if this method needs to check for valid positions
    if (!map.isValidPosition(startX, startY) || !map.isValidPosition(targetX, targetY)) {
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

  private List<int[]> bfs(Grid map, int startX, int startY, int targetX, int targetY) {
    Queue<Node> queue = new LinkedList<>();
    queue.offer(new Node(startX, startY, null));

    Set<String> visited = new HashSet<>();
    visited.add(startX + "," + startY);

    List<int[]> current = bfsIteration(map, targetX, targetY, queue, visited);
    if (current != null) {
      return current;
    }
    return null;
  }

  private List<int[]> bfsIteration(Grid map, int targetX, int targetY, Queue<Node> queue,
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
      for (int[] neighbor : map.getAdjacentPositions(x, y)) {
        int newX = neighbor[0];
        int newY = neighbor[1];

        String posKey = newX + "," + newY;

        if (!visited.contains(posKey) && map.isValidPosition(newX, newY)) {
          queue.offer(new Node(newX, newY, current));
          visited.add(posKey);
        }
      }
    }
    return null;
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

  // way to keep track of positions and parents so we don't need to do the silly things with like
  // 2 arrays, good part about 330 is you get to pseudocode it
  private static class Node {

    private final int x;
    private final int y;
    private final Node parent;

    Node(int x, int y, Node parent) {
      this.x = x;
      this.y = y;
      this.parent = parent;
    }
  }
}
