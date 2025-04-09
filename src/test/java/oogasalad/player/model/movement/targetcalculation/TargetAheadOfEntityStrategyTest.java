package oogasalad.player.model.movement.targetcalculation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.Optional;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TargetAheadOfEntityStrategyTest {

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
  void getTargetPosition_entityRightDirection_returnsCorrectLocation() {
    when(mockEntity.getEntityDirection()).thenReturn('R');
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockPlacement.getTypeString()).thenReturn("enemy");
    when(mockPlacement.getX()).thenReturn(3.0);
    when(mockPlacement.getY()).thenReturn(2.0);

    Iterator<Entity> iterator = Collections.singletonList(mockEntity).iterator();
    when(mockMap.iterator()).thenReturn(iterator);
    when(mockMap.isValidPosition(anyInt(), anyInt())).thenReturn(true);
    when(mockMap.isNotBlocked(anyString(), anyInt(), anyInt())).thenReturn(true);

    Map<String, Object> config = new HashMap<>();
    config.put("targetType", "enemy");
    config.put("tilesAhead", 2);

    TargetStrategy strategy = new TargetAheadOfEntityStrategy(mockMap, config, "someType");
    int[] result = strategy.getTargetPosition();

    assertArrayEquals(new int[]{5, 2}, result);
  }

  @Test
  void getTargetPosition_targetEntityBlocksCaller_returnsTargetEntityPosition() {
    when(mockEntity.getEntityDirection()).thenReturn('R');
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockPlacement.getTypeString()).thenReturn("enemy");
    when(mockPlacement.getX()).thenReturn(3.0);
    when(mockPlacement.getY()).thenReturn(2.0);

    Iterator<Entity> iterator = Collections.singletonList(mockEntity).iterator();
    when(mockMap.iterator()).thenReturn(iterator);
    when(mockMap.isValidPosition(anyInt(), anyInt())).thenReturn(true);
    when(mockMap.isNotBlocked(anyString(), anyInt(), anyInt())).thenReturn(false);

    Map<String, Object> config = new HashMap<>();
    config.put("targetType", "enemy");
    config.put("tilesAhead", 2);

    TargetStrategy strategy = new TargetAheadOfEntityStrategy(mockMap, config, "someType");
    int[] result = strategy.getTargetPosition();

    assertArrayEquals(new int[]{3, 2}, result);
  }

  @Test
  void getTargetPosition_unrecognizedDirection_defaultsToUp() {
    when(mockEntity.getEntityDirection()).thenReturn('X');
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockPlacement.getTypeString()).thenReturn("enemy");
    when(mockPlacement.getX()).thenReturn(4.0);
    when(mockPlacement.getY()).thenReturn(4.0);

    Iterator<Entity> iterator = Collections.singletonList(mockEntity).iterator();
    when(mockMap.iterator()).thenReturn(iterator);
    when(mockMap.isValidPosition(anyInt(), anyInt())).thenReturn(true);
    when(mockMap.isNotBlocked(anyString(), anyInt(), anyInt())).thenReturn(true);

    Map<String, Object> config = new HashMap<>();
    config.put("targetType", "enemy");
    config.put("tilesAhead", 1);

    TargetStrategy strategy = new TargetAheadOfEntityStrategy(mockMap, config, "someType");
    int[] result = strategy.getTargetPosition();

    assertArrayEquals(new int[]{4, 3}, result);
  }

  @Test
  void getTargetPosition_noValidEntity_returnsDefaultLocation() {
    when(mockPlacement.getX()).thenReturn(4.0);
    when(mockPlacement.getY()).thenReturn(4.0);

    Iterator<Entity> iterator = Collections.emptyIterator();
    when(mockMap.iterator()).thenReturn(iterator);

    Map<String, Object> config = new HashMap<>();
    config.put("targetType", "enemy");
    config.put("tilesAhead", 3);

    TargetStrategy strategy = new TargetAheadOfEntityStrategy(mockMap, config, "someType");
    int[] result = strategy.getTargetPosition();

    assertArrayEquals(new int[]{0, 0}, result);
  }
}
