package oogasalad.player.model.control.pathfinding;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PathFindingStrategyHelperMethodsTest {

  private GameMap mockMap;
  private EntityPlacement mockEntity;

  @BeforeEach
  void setup() {
    mockMap = mock(GameMap.class);
    mockEntity = mock(EntityPlacement.class);
    when(mockEntity.getTypeString()).thenReturn("TestEntity");

    // Allow all positions by default
    when(mockMap.isValidPosition(anyInt(), anyInt())).thenReturn(true);
    when(mockMap.isNotBlocked(anyString(), anyInt(), anyInt())).thenReturn(true);
  }

  @Test
  void getPreferredNeighbors_movingDown_returnDirectionNotUp() {
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.D);
    assertContainsDirections(result, 5, 5, List.of(Direction.D, Direction.L, Direction.R));
  }

  @Test
  void getPreferredNeighbors_movingUp_returnDirectionNotDown() {
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.U);
    assertContainsDirections(result, 5, 5, List.of(Direction.U, Direction.L, Direction.R));
  }

  @Test
  void getPreferredNeighbors_movingLeft_returnDirectionNotRight() {
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.L);
    assertContainsDirections(result, 5, 5, List.of(Direction.L, Direction.U, Direction.D));
  }

  @Test
  void getPreferredNeighbors_movingRight_returnDirectionNotLeft() {
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.R);
    assertContainsDirections(result, 5, 5, List.of(Direction.R, Direction.U, Direction.D));
  }

  @Test
  void getPreferredNeighbors_noDirection_returnEmpty() {
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.NONE);
    assertContainsDirections(result, 5, 5, List.of());
  }

  @Test
  void getAllValidNeighbors_noBlockedDirections_returnAllDirections() {
    List<int[]> result = PathFindingStrategyHelperMethods.getAllValidNeighbors(mockMap, 0, 0, mockEntity);

    Set<List<Integer>> expectedOffsets = new HashSet<>();
    for (Direction dir : Direction.values()) {
      if (!dir.isNone()) {
        expectedOffsets.add(List.of(dir.getDx(), dir.getDy()));
      }
    }

    Set<List<Integer>> actualOffsets = new HashSet<>();
    for (int[] arr : result) {
      actualOffsets.add(List.of(arr[0], arr[1]));
    }

    assertEquals(expectedOffsets, actualOffsets, "Should return valid neighbors in all directions except NONE.");
  }

  @Test
  void getPreferredNeighbors_movingDownLeftBlocked_returnDownAndRight() {
    blockPosition(4, 5); // Direction.L from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.D);
    assertContainsDirections(result, 5, 5, List.of(Direction.D, Direction.R));
  }

  @Test
  void getPreferredNeighbors_movingDownRightBlocked_returnDownAndLeft() {
    blockPosition(6, 5); // Direction.R from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.D);
    assertContainsDirections(result, 5, 5, List.of(Direction.D, Direction.L));
  }

  @Test
  void getPreferredNeighbors_movingDownForwardBlocked_returnLeftAndRight() {
    blockPosition(5, 6); // Direction.D from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.D);
    assertContainsDirections(result, 5, 5, List.of(Direction.L, Direction.R));
  }

  @Test
  void getPreferredNeighbors_movingRightUpBlocked_returnRightAndDown() {
    blockPosition(5, 4); // Direction.U from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.R);
    assertContainsDirections(result, 5, 5, List.of(Direction.R, Direction.D));
  }

  @Test
  void getPreferredNeighbors_movingLeftDownBlocked_returnLeftAndUp() {
    blockPosition(5, 6); // Direction.D from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.L);
    assertContainsDirections(result, 5, 5, List.of(Direction.L, Direction.U));
  }

  @Test
  void getPreferredNeighbors_movingUpLeftBlocked_returnUpAndRight() {
    blockPosition(4, 5); // Direction.L from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.U);
    assertContainsDirections(result, 5, 5, List.of(Direction.U, Direction.R));
  }

  private void blockPosition(int x, int y) {
    when(mockMap.isNotBlocked(anyString(), eq(x), eq(y))).thenReturn(false);
  }


  private void assertContainsDirections(List<int[]> result, int baseX, int baseY, List<Direction> directions) {
    Set<String> expected = new HashSet<>();
    for (Direction d : directions) {
      expected.add((baseX + d.getDx()) + "," + (baseY + d.getDy()));
    }

    Set<String> actual = new HashSet<>();
    for (int[] pos : result) {
      actual.add(pos[0] + "," + pos[1]);
    }

    assertEquals(expected, actual, "Expected directions not found.");
  }
}
