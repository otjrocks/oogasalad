package oogasalad.player.model.control.pathfinding;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMapInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EuclideanPathFindingStrategyTest {

  private EuclideanPathFindingStrategy strategy;
  private GameMapInterface mockMap;
  private EntityPlacement mockEntity;

  @BeforeEach
  void setUp() {
    strategy = new EuclideanPathFindingStrategy();
    mockMap = mock(GameMapInterface.class);
    mockEntity = mock(EntityPlacement.class);
    when(mockEntity.getTypeString()).thenReturn("Player");
  }

  @Test
  void getPath_unblockedPathMockAllNeighbors_returnUp() {
    int startX = 0, startY = 0;
    int targetX = 1, targetY = 0;

    List<int[]> neighbors = new ArrayList<>(List.of(
        new int[]{1, 0},
        new int[]{0, 1},
        new int[]{1, 1}
    ));

    try (MockedStatic<PathFindingStrategyHelperMethods> utilities = mockStatic(PathFindingStrategyHelperMethods.class)) {
      utilities.when(() ->
              PathFindingStrategyHelperMethods.getValidDirections(mockMap, startX, startY, mockEntity, Direction.NONE))
          .thenReturn(neighbors);

      mockPositionValidity(neighbors, true);

      int[] result = strategy.getPath(mockMap, startX, startY, targetX, targetY, mockEntity, Direction.NONE);

      assertArrayEquals(new int[]{1, 0}, result);
    }
  }

  @Test
  void getPath_unblockedPathMockPreferred_returnRight() {
    int startX = 2, startY = 2;
    int targetX = 2, targetY = 3;

    List<int[]> preferred = new ArrayList<>(List.of(
        new int[]{2, 3},
        new int[]{1, 2}
    ));

    when(mockMap.isValidPosition(anyInt(), anyInt())).thenReturn(true);
    when(mockMap.isNotBlocked(any(), anyInt(), anyInt())).thenReturn(true);

    try (MockedStatic<PathFindingStrategyHelperMethods> utilities = mockStatic(PathFindingStrategyHelperMethods.class)) {
      utilities.when(() ->
              PathFindingStrategyHelperMethods.getValidDirections(mockMap, startX, startY, mockEntity, Direction.D))
          .thenReturn(preferred);

      int[] result = strategy.getPath(mockMap, startX, startY, targetX, targetY, mockEntity, Direction.D);

      assertArrayEquals(new int[]{0, 1}, result);
    }
  }

  private void mockPositionValidity(List<int[]> positions, boolean isValid) {
    for (int[] pos : positions) {
      when(mockMap.isValidPosition(pos[0], pos[1])).thenReturn(isValid);
      when(mockMap.isNotBlocked(any(), eq(pos[0]), eq(pos[1]))).thenReturn(isValid);
    }
  }
}
