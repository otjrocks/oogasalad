package oogasalad.player.model.control;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.control.pathfinding.BfsPathFindingStrategy;
import oogasalad.player.model.control.targetcalculation.TargetStrategy;
import oogasalad.player.model.control.targetcalculation.TargetStrategyFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

// written by chatGPT
public class TargetControlStrategyTest {

  private GameMap gameMap;
  private EntityPlacement placement;
  private EntityType entityType;
  private Entity entity;

  @BeforeEach
  void setup() {
    gameMap = mock(GameMap.class);
    placement = mock(EntityPlacement.class);
    entityType = mock(EntityType.class);
    entity = mock(Entity.class);

    when(placement.getX()).thenReturn(5.0);
    when(placement.getY()).thenReturn(5.0);

    when(placement.getType()).thenReturn(entityType);
    Map<String, Object> config = new HashMap<>();
    config.put("targetType", "enemy");
    config.put("tilesAhead", 1);
    when(entityType.strategyConfig()).thenReturn(config);
    when(entityType.controlType()).thenReturn("targetEntity");

    // Avoid null pointer in real strategy if triggered
    when(gameMap.iterator()).thenReturn(Collections.emptyIterator());
  }

  @Test
  void update_targetToRight_setsDirectionRight() {
    runUpdateTest(new int[]{6, 5}, new int[]{1, 0}, Direction.R, true);
  }

  @Test
  void update_targetToLeft_setsDirectionLeft() {
    runUpdateTest(new int[]{4, 5}, new int[]{-1, 0}, Direction.L, true);
  }

  @Test
  void update_targetAbove_setsDirectionUp() {
    runUpdateTest(new int[]{5, 4}, new int[]{0, -1}, Direction.U, true);
  }

  @Test
  void update_targetBelow_setsDirectionDown() {
    runUpdateTest(new int[]{5, 6}, new int[]{0, 1}, Direction.D, true);
  }

  @Test
  void update_cannotMoveInDirection_doesNotSetDirection() {
    runUpdateTest(new int[]{6, 5}, new int[]{1, 0}, Direction.R, false);
  }

  private void runUpdateTest(int[] targetPosition, int[] directionVector, Direction expectedDirection, boolean canMove) {
    TargetStrategy mockTargetStrategy = mock(TargetStrategy.class);
    when(mockTargetStrategy.getTargetPosition()).thenReturn(targetPosition);

    when(entity.canMove(expectedDirection)).thenReturn(canMove);

    try (
        MockedStatic<TargetStrategyFactory> mockedFactory = mockStatic(TargetStrategyFactory.class);
        MockedConstruction<BfsPathFindingStrategy> mockedConstruction =
            mockConstruction(BfsPathFindingStrategy.class,
                (mock, context) -> {
                  when(mock.getPath(any(), anyInt(), anyInt(), anyInt(), anyInt(), any(), any()))
                      .thenReturn(directionVector);
                })
    ) {
      mockedFactory.when(() -> TargetStrategyFactory.createTargetStrategy(placement, gameMap))
          .thenReturn(mockTargetStrategy);

      TargetControlStrategy strategy = new TargetControlStrategy(gameMap, placement);

      strategy.update(entity);

      if (canMove) {
        verify(entity).setEntityDirection(expectedDirection);
      } else {
        verify(entity, never()).setEntityDirection(any(Direction.class));
      }
    }
  }

}
