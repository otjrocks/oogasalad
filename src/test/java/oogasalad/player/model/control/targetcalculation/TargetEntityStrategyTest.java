package oogasalad.player.model.control.targetcalculation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TargetEntityStrategyTest {

  private GameMap mockMap;
  private Entity mockEntity;
  private EntityPlacement mockPlacement;

  @BeforeEach
  void setUp() {
    mockMap = mock(GameMap.class);
    mockEntity = mock(Entity.class);
    mockPlacement = mock(EntityPlacement.class);
  }

  @Test
  void getTargetPosition_validEntity_returnsEntityLocation() {
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockPlacement.getTypeString()).thenReturn("enemy");
    when(mockPlacement.getX()).thenReturn(5.0);
    when(mockPlacement.getY()).thenReturn(5.0);

    Iterator<Entity> iterator = Collections.singletonList(mockEntity).iterator();
    when(mockMap.iterator()).thenReturn(iterator);

    Map<String, Object> config = new HashMap<>();
    config.put("targetType", "enemy");

    TargetStrategy strategy = new TargetEntityStrategy(mockMap, config);
    int[] result = strategy.getTargetPosition();

    assertArrayEquals(new int[]{5, 5}, result);
  }

  @DisplayName("Returns default location when no valid entity is found")
  @Test
  void getTargetPosition_noValidEntity_returnsDefaultLocation() {
    Iterator<Entity> iterator = Collections.emptyIterator();
    when(mockMap.iterator()).thenReturn(iterator);

    Map<String, Object> config = new HashMap<>();
    config.put("targetType", "enemy");

    TargetStrategy strategy = new TargetEntityStrategy(mockMap, config);
    int[] result = strategy.getTargetPosition();

    assertArrayEquals(new int[]{0, 0}, result);
  }
}
