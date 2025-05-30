package oogasalad.player.model.strategies.control.pathfinding;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.List;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.model.GameMapInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class RandomPathFindingStrategyTest {

  private RandomPathFindingStrategy strategy;
  private GameMapInterface mockMap;
  private EntityPlacement mockEntity;

  @BeforeEach
  void setUp() {
    strategy = new RandomPathFindingStrategy();
    mockMap = mock(GameMapInterface.class);
    mockEntity = mock(EntityPlacement.class);
    when(mockEntity.getTypeString()).thenReturn("Enemy");
  }

  @Test
  void getPath_noValidDirections_returnsZeroMovement() {
    try (MockedStatic<PathFindingStrategyHelperMethods> mockedHelper = mockStatic(
        PathFindingStrategyHelperMethods.class)) {
      mockedHelper.when(() ->
              PathFindingStrategyHelperMethods.getValidDirections(mockMap, 5, 5, mockEntity,
                  Direction.NONE))
          .thenReturn(List.of());

      int[] result = strategy.getPath(mockMap, 5, 5, 10, 10, mockEntity, Direction.NONE);
      assertArrayEquals(new int[]{0, 0}, result);
    }
  }

  @Test
  void getPath_singleDirection_returnsCorrectOffset() {
    List<int[]> onlyOneOption = List.of(new int[]{6, 5}); // Right

    try (MockedStatic<PathFindingStrategyHelperMethods> mockedHelper = mockStatic(
        PathFindingStrategyHelperMethods.class)) {
      mockedHelper.when(() ->
              PathFindingStrategyHelperMethods.getValidDirections(mockMap, 5, 5, mockEntity,
                  Direction.R))
          .thenReturn(onlyOneOption);

      int[] result = strategy.getPath(mockMap, 5, 5, 0, 0, mockEntity, Direction.R);
      assertArrayEquals(new int[]{1, 0}, result);
    }
  }

  @RepeatedTest(5)
  void getPath_multipleValidDirections_returnsAnyValidOffset() {
    List<int[]> options = List.of(
        new int[]{5, 6}, // Down
        new int[]{4, 5}, // Left
        new int[]{5, 4}  // Up
    );

    try (MockedStatic<PathFindingStrategyHelperMethods> mockedHelper = mockStatic(
        PathFindingStrategyHelperMethods.class)) {
      mockedHelper.when(() ->
              PathFindingStrategyHelperMethods.getValidDirections(mockMap, 5, 5, mockEntity,
                  Direction.NONE))
          .thenReturn(options);

      int[] result = strategy.getPath(mockMap, 5, 5, 0, 0, mockEntity, Direction.NONE);

      // Should be one of the defined offsets
      assertTrue(
          result[0] == 0 && (result[1] == 1 || result[1] == -1) || // Up or Down
              result[1] == 0 && result[0] == -1                         // Left
      );
    }
  }
}
