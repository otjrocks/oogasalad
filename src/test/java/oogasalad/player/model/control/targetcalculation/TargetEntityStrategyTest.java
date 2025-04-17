package oogasalad.player.model.control.targetcalculation;

import java.util.Collections;
import oogasalad.engine.model.GameMapInterface;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.EntityPlacement;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TargetEntityStrategyTest {

  @Test
  void getTargetPosition_validEntity_returnsEntityCoordinates() {
    GameMapInterface mockMap = mock(GameMapInterface.class);
    Entity mockEntity = mock(Entity.class);
    EntityPlacement mockPlacement = mock(EntityPlacement.class);

    when(mockPlacement.getX()).thenReturn(2.0);
    when(mockPlacement.getY()).thenReturn(4.0);
    when(mockPlacement.getTypeString()).thenReturn("Enemy");
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockMap.iterator()).thenReturn(
        java.util.List.of(mockEntity).iterator()
    );

    Map<String, Object> config = Map.of("targetType", "Enemy");
    TargetStrategyInterface strategy = new TargetEntityStrategy(mockMap, config);

    int[] result = strategy.getTargetPosition();
    assertArrayEquals(new int[]{2, 4}, result);
  }

  @Test
  void getTargetPosition_noEntity_returnsDefaultZero() {
    GameMapInterface mockMap = mock(GameMapInterface.class);
    Iterator<Entity> emptyIterator = Collections.emptyIterator();
    when(mockMap.iterator()).thenReturn(emptyIterator);

    Map<String, Object> config = Map.of("targetType", "Enemy");
    TargetStrategyInterface strategy = new TargetEntityStrategy(mockMap, config);

    int[] result = strategy.getTargetPosition();
    assertArrayEquals(new int[]{0, 0}, result);
  }
}
