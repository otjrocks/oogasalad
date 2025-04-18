package oogasalad.player.model.strategies.control.pathfinding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.model.GameMapInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PathFindingStrategyHelperMethodsTest {

  private GameMapInterface mockMap;
  private EntityPlacement mockEntity;

  @BeforeEach
  void setup() {
    mockMap = mock(GameMapInterface.class);
    mockEntity = mock(EntityPlacement.class);
    when(mockEntity.getTypeString()).thenReturn("TestEntity");

    when(mockMap.isValidPosition(anyInt(), anyInt())).thenReturn(true);
    when(mockMap.isNotBlocked(anyString(), anyInt(), anyInt())).thenReturn(true);
  }


  @Test
  void getPreferredNeighbors_down_returnsDownLeftRight() {
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.D);
    assertContainsDirections(result, 5, 5, List.of(Direction.D, Direction.L, Direction.R));
  }

  @Test
  void getPreferredNeighbors_up_returnsUpLeftRight() {
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.U);
    assertContainsDirections(result, 5, 5, List.of(Direction.U, Direction.L, Direction.R));
  }

  @Test
  void getPreferredNeighbors_left_returnsLeftUpDown() {
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.L);
    assertContainsDirections(result, 5, 5, List.of(Direction.L, Direction.U, Direction.D));
  }

  @Test
  void getPreferredNeighbors_right_returnsRightUpDown() {
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.R);
    assertContainsDirections(result, 5, 5, List.of(Direction.R, Direction.U, Direction.D));
  }

  @Test
  void getPreferredNeighbors_none_returnsEmpty() {
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.NONE);
    assertTrue(result.isEmpty(), "Expected no preferred neighbors for NONE direction.");
  }


  @Test
  void getPreferredNeighbors_leftBlocked_returnsDownRight() {
    blockPosition(4, 5); // L from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.D);
    assertContainsDirections(result, 5, 5, List.of(Direction.D, Direction.R));
  }

  @Test
  void getPreferredNeighbors_rightBlocked_returnsDownLeft() {
    blockPosition(6, 5); // R from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.D);
    assertContainsDirections(result, 5, 5, List.of(Direction.D, Direction.L));
  }

  @Test
  void getPreferredNeighbors_downBlocked_returnsLeftRight() {
    blockPosition(5, 6); // D from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.D);
    assertContainsDirections(result, 5, 5, List.of(Direction.L, Direction.R));
  }

  @Test
  void getPreferredNeighbors_upBlocked_returnsRightDown() {
    blockPosition(5, 4); // U from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.R);
    assertContainsDirections(result, 5, 5, List.of(Direction.R, Direction.D));
  }

  @Test
  void getPreferredNeighbors_downBlockedOnLeft_returnsUpLeft() {
    blockPosition(5, 6); // D from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.L);
    assertContainsDirections(result, 5, 5, List.of(Direction.L, Direction.U));
  }

  @Test
  void getPreferredNeighbors_leftBlockedOnUp_returnsUpRight() {
    blockPosition(4, 5); // L from (5,5)
    List<int[]> result = PathFindingStrategyHelperMethods.getPreferredNeighbors(mockMap, 5, 5, mockEntity, Direction.U);
    assertContainsDirections(result, 5, 5, List.of(Direction.U, Direction.R));
  }


  @Test
  void getAllValidNeighbors_returnsAllDirections() {
    List<int[]> result = PathFindingStrategyHelperMethods.getAllValidNeighbors(mockMap, 0, 0, mockEntity);

    Set<List<Integer>> expected = new HashSet<>();
    for (Direction dir : Direction.values()) {
      if (!dir.isNone()) {
        expected.add(List.of(dir.getDx(), dir.getDy()));
      }
    }

    Set<List<Integer>> actual = toOffsetSet(result);
    assertEquals(expected, actual, "Should return all valid neighbors except NONE.");
  }


  @Test
  void getValidDirections_null_returnsAllValid() {
    List<int[]> result = PathFindingStrategyHelperMethods.getValidDirections(mockMap, 5, 5, mockEntity, null);

    Set<List<Integer>> expected = new HashSet<>();
    for (Direction dir : Direction.values()) {
      if (!dir.isNone()) {
        expected.add(List.of(5 + dir.getDx(), 5 + dir.getDy()));
      }
    }

    assertEquals(expected, toOffsetSet(result), "Should return all valid directions for null input.");
  }

  @Test
  void getValidDirections_none_returnsAllValid() {
    List<int[]> result = PathFindingStrategyHelperMethods.getValidDirections(mockMap, 5, 5, mockEntity, Direction.NONE);

    Set<List<Integer>> expected = new HashSet<>();
    for (Direction dir : Direction.values()) {
      if (!dir.isNone()) {
        expected.add(List.of(5 + dir.getDx(), 5 + dir.getDy()));
      }
    }

    assertEquals(expected, toOffsetSet(result), "Should return all valid directions for NONE.");
  }

  @Test
  void getValidDirections_directionD_returnsPreferredOnly() {
    List<int[]> result = PathFindingStrategyHelperMethods.getValidDirections(mockMap, 5, 5, mockEntity, Direction.D);
    assertContainsDirections(result, 5, 5, List.of(Direction.D, Direction.L, Direction.R));
  }

  private void blockPosition(int x, int y) {
    when(mockMap.isNotBlocked(anyString(), eq(x), eq(y))).thenReturn(false);
  }

  private void assertContainsDirections(List<int[]> result, int baseX, int baseY, List<Direction> directions) {
    Set<String> expected = new HashSet<>();
    for (Direction dir : directions) {
      expected.add((baseX + dir.getDx()) + "," + (baseY + dir.getDy()));
    }

    Set<String> actual = new HashSet<>();
    for (int[] pos : result) {
      actual.add(pos[0] + "," + pos[1]);
    }

    assertEquals(expected, actual, "Expected directions do not match actual.");
  }

  private Set<List<Integer>> toOffsetSet(List<int[]> positions) {
    Set<List<Integer>> set = new HashSet<>();
    for (int[] pos : positions) {
      set.add(List.of(pos[0], pos[1]));
    }
    return set;
  }
}
