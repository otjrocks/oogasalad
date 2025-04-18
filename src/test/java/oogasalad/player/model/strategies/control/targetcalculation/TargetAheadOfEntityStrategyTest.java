package oogasalad.player.model.strategies.control.targetcalculation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import org.junit.jupiter.api.Test;

class TargetAheadOfEntityStrategyTest {

  @Test
  void getTargetPosition_validEntity_returnsPositionAhead() {
    GameMapInterface mockMap = mock(GameMapInterface.class);
    Entity mockEntity = mock(Entity.class);
    EntityPlacement mockPlacement = mock(EntityPlacement.class);

    when(mockPlacement.getX()).thenReturn(4.0);
    when(mockPlacement.getY()).thenReturn(4.0);
    when(mockPlacement.getTypeString()).thenReturn("Enemy");

    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockEntity.getEntityDirection()).thenReturn(Direction.R);

    when(mockMap.iterator()).thenReturn(java.util.List.of(mockEntity).iterator());
    when(mockMap.isValidPosition(6, 4)).thenReturn(true);
    when(mockMap.isNotBlocked("Caller", 6, 4)).thenReturn(true);

    Map<String, Object> config = Map.of("targetType", "Enemy", "tilesAhead", 2);
    TargetStrategyInterface strategy = new TargetAheadOfEntityStrategy(mockMap, config, "Caller");

    assertArrayEquals(new int[]{6, 4}, strategy.getTargetPosition());
  }

  @Test
  void getTargetPosition_noEntity_returnsDefaultZero() {
    GameMapInterface mockMap = mock(GameMapInterface.class);
    when(mockMap.iterator()).thenReturn(java.util.Collections.emptyIterator());

    Map<String, Object> config = Map.of("targetType", "Enemy", "tilesAhead", 3);
    TargetStrategyInterface strategy = new TargetAheadOfEntityStrategy(mockMap, config, "Caller");

    assertArrayEquals(new int[]{0, 0}, strategy.getTargetPosition());
  }

  @Test
  void getTargetPosition_aheadBlocked_returnsCurrentPosition() {
    GameMapInterface mockMap = mock(GameMapInterface.class);
    Entity mockEntity = mock(Entity.class);
    EntityPlacement mockPlacement = mock(EntityPlacement.class);

    when(mockPlacement.getX()).thenReturn(2.0);
    when(mockPlacement.getY()).thenReturn(3.0);
    when(mockPlacement.getTypeString()).thenReturn("Enemy");

    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockEntity.getEntityDirection()).thenReturn(Direction.D);

    when(mockMap.iterator()).thenReturn(java.util.List.of(mockEntity).iterator());

    // Target position ahead is blocked
    when(mockMap.isValidPosition(2, 4)).thenReturn(true);
    when(mockMap.isNotBlocked("Caller", 2, 4)).thenReturn(false); // Blocked

    Map<String, Object> config = Map.of("targetType", "Enemy", "tilesAhead", 1);
    TargetStrategyInterface strategy = new TargetAheadOfEntityStrategy(mockMap, config, "Caller");

    assertArrayEquals(new int[]{2, 3}, strategy.getTargetPosition());
  }
}
