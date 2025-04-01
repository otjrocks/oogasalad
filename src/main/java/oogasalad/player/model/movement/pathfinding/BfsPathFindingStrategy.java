package oogasalad.player.model.movement.pathfinding;

import java.util.ArrayList;
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
  public int[] getPath(GameMap map, int startX, int startY, int targetX, int targetY) {

    // don't know if this method needs to check for valid positions
    if (!isValidPosition(map, startX, startY) || !isValidPosition(map, targetX, targetY)) {
      return new int[]{0, 0};
    }

    // standard BFS algorithm
    Queue<Node> queue = new LinkedList<>();
    queue.offer(new Node(startX, startY, null));

    Set<String> visited = new HashSet<>();
    visited.add(startX + "," + startY);

    while (!queue.isEmpty()) {
      Node current = queue.poll();
      int x = current.x;
      int y = current.y;

      if (x == targetX && y == targetY) {
        // once you get the target retrace to build the path
        int[] nextPos = firstDirection(current);
        int dx = nextPos[0] - startX;
        int dy = nextPos[1] - startY;
        return new int[]{dx, dy};
      }

      // map ideally gives you all adjacent positions to traverse in that ARE VALID
      // so here not doing any valid checking
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

    // otherwise it just no move
    return new int[]{0, 0};
  }

  private int[] firstDirection(Node target) {

    // reconstruct path tree from BFS traversal list because we love algorithms

    List<int[]> path = new ArrayList<>();
    Node current = target;

    while (current != null) {
      path.add(new int[]{current.x, current.y});
      current = current.parent;
    }

    // next direction to move to
    if (path.size() > 1) {
      return path.get(path.size() - 2);
    }

    // already at destination
    return path.getLast();
  }

  private boolean isValidPosition(GameMap map, int x, int y) {
    // TODO: eventually need to account for blocking entities
    return x >= 0 && y >= 0 && x < map.getWidth() && y < map.getHeight();
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
  private static class Node {

    int x;
    int y;
    Node parent;

    Node(int x, int y, Node parent) {
      this.x = x;
      this.y = y;
      this.parent = parent;
    }
  }
}

