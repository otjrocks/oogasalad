package oogasalad.player.model.movement.pathfinding;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import oogasalad.engine.model.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import oogasalad.engine.model.GameMap;

class BfsPathFindingStrategyTest {

  private GameMap mockMap;
  private BfsPathFindingStrategy strategy;

  @BeforeEach
  public void setUp() {
    mockMap = mock(GameMap.class);
    strategy = new BfsPathFindingStrategy();
  }

  @Test
  void getPath_simplePath_returnsCorrectInitialDirection() {
    int width = 5;
    int height = 5;

    when(mockMap.getWidth()).thenReturn(width);
    when(mockMap.getHeight()).thenReturn(height);

    // All tiles are empty
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        when(mockMap.getEntityAt(x, y)).thenReturn(Optional.empty());
      }
    }

    int startX = 2, startY = 2;
    int targetX = 2, targetY = 3;

    int[] expected = { 0, 1 };

    int[] actual = strategy.getPath(mockMap, startX, startY, targetX, targetY);
    assertArrayEquals(expected, actual);
  }

  @Test
  void getPath_simplePathTargetIsNonEmpty_returnsCorrectInitialDirection() {
    int width = 5;
    int height = 5;

    when(mockMap.getWidth()).thenReturn(width);
    when(mockMap.getHeight()).thenReturn(height);

    // All tiles are empty
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        when(mockMap.getEntityAt(x, y)).thenReturn(Optional.empty());
      }
    }

    when(mockMap.getEntityAt(2, 3)).thenReturn(Optional.ofNullable(mock(Entity.class)));

    int startX = 2, startY = 2;
    int targetX = 2, targetY = 3;

    int[] expected = { 0, 1 };

    int[] actual = strategy.getPath(mockMap, startX, startY, targetX, targetY);
    assertArrayEquals(expected, actual);
  }

  @Test
  void getPath_atDestination_returnsZeroVector() {
    int width = 5;
    int height = 5;

    when(mockMap.getWidth()).thenReturn(width);
    when(mockMap.getHeight()).thenReturn(height);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        when(mockMap.getEntityAt(x, y)).thenReturn(Optional.empty());
      }
    }

    int startX = 2, startY = 2;
    int targetX = 2, targetY = 2;

    int[] expected = { 0, 0 };

    int[] actual = strategy.getPath(mockMap, startX, startY, targetX, targetY);
    assertArrayEquals(expected, actual);
  }

  @Test
  void getPath_invalidStart_returnsZeroVector() {
    when(mockMap.getWidth()).thenReturn(3);
    when(mockMap.getHeight()).thenReturn(3);

    int[] actual = strategy.getPath(mockMap, -1, 1, 2, 2);
    assertArrayEquals(new int[] {0, 0}, actual);
  }

  @Test
  void getPath_blockedPath_returnsZeroVector() {
    when(mockMap.getWidth()).thenReturn(3);
    when(mockMap.getHeight()).thenReturn(3);

    // Start and target valid
    when(mockMap.getEntityAt(anyInt(), anyInt())).thenReturn(Optional.of(mock(Entity.class)));

    int[] actual = strategy.getPath(mockMap, 0, 0, 2, 2);
    assertArrayEquals(new int[] {0, 0}, actual);
  }


    
}
