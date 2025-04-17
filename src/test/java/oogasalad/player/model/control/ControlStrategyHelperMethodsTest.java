package oogasalad.player.model.control;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.BfsEntityException;
import oogasalad.player.model.control.pathfinding.PathFindingStrategy;
import oogasalad.player.model.control.targetcalculation.TargetStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControlStrategyHelperMethodsTest {

  private GameMap mockMap;
  private Entity mockEntity;
  private EntityPlacement mockPlacement;
  private TargetStrategy mockTargetStrategy;
  private PathFindingStrategy mockPathStrategy;

  @BeforeEach
  void setUp() {
    mockMap = mock(GameMap.class);
    mockEntity = mock(Entity.class);
    mockPlacement = mock(EntityPlacement.class);
    mockTargetStrategy = mock(TargetStrategy.class);
    mockPathStrategy = mock(PathFindingStrategy.class);

    when(mockEntity.getEntityDirection()).thenReturn(Direction.D);
  }


  @Test
  void validateAndGetTargetPosition_valid_returnsTarget() {
    int[] expected = {3, 4};
    when(mockTargetStrategy.getTargetPosition()).thenReturn(expected);

    int[] result = ControlStrategyHelperMethods.validateAndGetTargetPosition(mockTargetStrategy);

    assertArrayEquals(expected, result);
  }

  @Test
  void validateAndGetTargetPosition_invalidLength_throwsException() {
    when(mockTargetStrategy.getTargetPosition()).thenReturn(new int[]{1});

    Exception exception = assertThrows(BfsEntityException.class, () ->
        ControlStrategyHelperMethods.validateAndGetTargetPosition(mockTargetStrategy));

    assertEquals("Target position must be of length 2", exception.getMessage());
  }


  @Test
  void getDirectionFromTargetAndPath_validTargetAndStart_setsDirectionCorrectly() {
    when(mockTargetStrategy.getTargetPosition()).thenReturn(new int[]{6, 5});
    when(mockPlacement.getX()).thenReturn(5.0);
    when(mockPlacement.getY()).thenReturn(5.0);
    when(mockEntity.canMove(Direction.R)).thenReturn(true);
    when(mockPathStrategy.getPath(mockMap, 5, 5, 6, 5, mockPlacement, Direction.D))
        .thenReturn(new int[]{1, 0});

    ControlStrategyHelperMethods.getDirectionFromTargetAndPath(
        mockMap, mockEntity, mockPlacement, mockTargetStrategy, mockPathStrategy);

    verify(mockEntity).setEntitySnapDirection(Direction.R);
  }

  @Test
  void getDirectionFromTargetAndPath_invalidDirection_doesNotSetAnything() {
    when(mockTargetStrategy.getTargetPosition()).thenReturn(new int[]{4, 4});
    when(mockPlacement.getX()).thenReturn(4.0);
    when(mockPlacement.getY()).thenReturn(4.0);
    when(mockEntity.canMove(any())).thenReturn(false);
    when(mockPathStrategy.getPath(any(), anyInt(), anyInt(), anyInt(), anyInt(), any(), any()))
        .thenReturn(new int[]{0, 0});


    ControlStrategyHelperMethods.getDirectionFromTargetAndPath(
        mockMap, mockEntity, mockPlacement, mockTargetStrategy, mockPathStrategy);

    verify(mockEntity, never()).setEntitySnapDirection(any());
  }


  @Test
  void setEntityDirection_validMove_setsCorrectDirection() {
    when(mockEntity.canMove(Direction.U)).thenReturn(true);

    ControlStrategyHelperMethods.setEntityDirection(0, -1, mockEntity);

    verify(mockEntity).setEntitySnapDirection(Direction.U);
  }

  @Test
  void setEntityDirection_invalidMove_doesNotSetDirection() {
    when(mockEntity.canMove(any())).thenReturn(false);

    ControlStrategyHelperMethods.setEntityDirection(1, 1, mockEntity);

    verify(mockEntity, never()).setEntitySnapDirection(any());
  }

  @Test
  void setEntityDirection_zeroMovement_doesNotSetDirection() {
    when(mockEntity.canMove(any())).thenReturn(true);

    ControlStrategyHelperMethods.setEntityDirection(0, 0, mockEntity);

    verify(mockEntity, never()).setEntitySnapDirection(any());
  }
}
