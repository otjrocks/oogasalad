package oogasalad.player.model.movement;

import java.util.ArrayList;
import java.util.List;

/**
 * Temporary Grid with things needed for path finding
 * - what are the adjacent positions for a given position, what positions are valid...
 *
 * Thing this would be GameMap? but I don't want to change it if its not
 */
public class Grid {
  private final int width;
  private final int height;

  public Grid(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public boolean isValidPosition(int x, int y) {
    return x >= 0 && y >= 0 && x < width && y < height;
  }

  // written by chatgpt because I was too lazy
  public List<int[]> getAdjacentPositions(int x, int y) {
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

      if (isValidPosition(newX, newY)) {
        neighbors.add(new int[]{newX, newY});
      }
    }

    return neighbors;

  }

}
