package oogasalad.player.model.strategies.control.pathfinding;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.model.EntityTypeRecord;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BfsPathFindingStrategyTest {

  private GameMapInterface mockMap;
  private BfsPathFindingStrategy strategy;
  private EntityPlacement mockEntityPlacement;

  @BeforeEach
  public void setUp() {
    mockMap = mock(GameMapInterface.class);
    mockEntityPlacement = mock(EntityPlacement.class);
    strategy = new BfsPathFindingStrategy();
  }

  @Test
  void getPath_simplePath_returnsCorrectInitialDirection() {
    int width = 5;
    int height = 5;

    // All tiles are empty
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        when(mockMap.getEntityAt(x, y)).thenReturn(Optional.empty());
      }
    }

    when(mockMap.isNotBlocked(anyString(), anyInt(), anyInt())).thenReturn(true);
    when(mockMap.isValidPosition(anyInt(), anyInt())).thenReturn(true);
    when(mockEntityPlacement.getTypeString()).thenReturn("someType");

    int startX = 2, startY = 2;
    int targetX = 2, targetY = 3;

    int[] expected = {0, 1};

    int[] actual = strategy.getPath(mockMap, startX, startY, targetX, targetY, mockEntityPlacement,
        Direction.NONE);
    assertArrayEquals(expected, actual);
  }

  @Test
  void getPath_simplePathTargetIsNonEmpty_returnsCorrectInitialDirection() {
    int width = 5;
    int height = 5;

    // All tiles are empty
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        when(mockMap.getEntityAt(x, y)).thenReturn(Optional.empty());
      }
    }

    when(mockMap.isNotBlocked(anyString(), anyInt(), anyInt())).thenReturn(true);
    when(mockMap.isValidPosition(anyInt(), anyInt())).thenReturn(true);
    when(mockEntityPlacement.getTypeString()).thenReturn("someType");

    when(mockMap.isValidPosition(2, 3)).thenReturn(false);

    int startX = 2, startY = 2;
    int targetX = 2, targetY = 3;

    int[] expected = {0, 0};

    int[] actual = strategy.getPath(mockMap, startX, startY, targetX, targetY, mockEntityPlacement,
        Direction.NONE);
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

    int[] expected = {0, 0};

    int[] actual = strategy.getPath(mockMap, startX, startY, targetX, targetY, mockEntityPlacement,
        Direction.NONE);
    assertArrayEquals(expected, actual);
  }

  @Test
  void getPath_invalidStart_returnsZeroVector() {
    when(mockMap.getWidth()).thenReturn(3);
    when(mockMap.getHeight()).thenReturn(3);

    int[] actual = strategy.getPath(mockMap, -1, 1, 2, 2, mockEntityPlacement,
        Direction.NONE);
    assertArrayEquals(new int[]{0, 0}, actual);
  }

  @Test
  void getPath_blockedPath_returnsZeroVector() {
    when(mockMap.getWidth()).thenReturn(3);
    when(mockMap.getHeight()).thenReturn(3);

    // Start and target valid
    Entity blockedEntity = mock(Entity.class);
    EntityPlacement blockedPlacement = mock(EntityPlacement.class);
    EntityTypeRecord blockedType = mock(EntityTypeRecord.class);
    when(mockMap.getEntityAt(anyInt(), anyInt())).thenReturn(Optional.of(blockedEntity));
    when(blockedEntity.getEntityPlacement()).thenReturn(blockedPlacement);
    when(blockedPlacement.getType()).thenReturn(blockedType);
    when(blockedType.blocks()).thenReturn(List.of("type"));

    when(mockEntityPlacement.getTypeString()).thenReturn("type");

    int[] actual = strategy.getPath(mockMap, 0, 0, 2, 2, mockEntityPlacement,
        Direction.NONE);

    assertArrayEquals(new int[]{0, 0}, actual);
  }


}
