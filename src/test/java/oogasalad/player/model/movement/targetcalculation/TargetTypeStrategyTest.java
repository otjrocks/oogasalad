package oogasalad.player.model.movement.targetcalculation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Iterator;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TargetTypeStrategyTest {

  private GameMap mockMap;
  private Entity mockEntity;
  private EntityPlacement mockPlacement;

  @BeforeEach
  public void setUp() {
    mockMap = mock(GameMap.class);
    mockEntity = mock(Entity.class);
    mockPlacement = mock(EntityPlacement.class);
  }

  @Test
  void getTargetPosition_noTilesAheadValidEntity_returnsEntityLocation() {
    when(mockEntity.getEntityDirection()).thenReturn('U'); // Any direction, doesn't matter here
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockPlacement.getTypeString()).thenReturn("enemy");
    when(mockPlacement.getX()).thenReturn(5.0);
    when(mockPlacement.getY()).thenReturn(5.0);

    Iterator<Entity> iterator = Collections.singletonList(mockEntity).iterator();
    when(mockMap.iterator()).thenReturn(iterator);

    TargetStrategy strategy = new TargetTypeStrategy(mockMap, "enemy", 0);
    int[] result = strategy.getTargetPosition();

    assertArrayEquals(new int[]{5, 5}, result);
  }

  @Test
  void getTargetPosition_tilesAheadValidEntityWithDirection_returnsEntityLocationNAhead() {
    when(mockEntity.getEntityDirection()).thenReturn('R');
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockPlacement.getTypeString()).thenReturn("enemy");
    when(mockPlacement.getX()).thenReturn(3.0);
    when(mockPlacement.getY()).thenReturn(2.0);

    Iterator<Entity> iterator = Collections.singletonList(mockEntity).iterator();
    when(mockMap.iterator()).thenReturn(iterator);

    TargetStrategy strategy = new TargetTypeStrategy(mockMap, "enemy", 2);
    int[] result = strategy.getTargetPosition();

    assertArrayEquals(new int[]{5, 2}, result);
  }

  @Test
  void getTargetPosition_tilesAheadValidEntityWithoutDirection_returnsEntityLocationNTilesAbove() {
    // Default direction is 'U' (up) if unrecognized, but the class uses 'U' as fallback
    when(mockEntity.getEntityDirection()).thenReturn('X');
    when(mockEntity.getEntityPlacement()).thenReturn(mockPlacement);
    when(mockPlacement.getTypeString()).thenReturn("enemy");
    when(mockPlacement.getX()).thenReturn(4.0);
    when(mockPlacement.getY()).thenReturn(4.0);

    Iterator<Entity> iterator = Collections.singletonList(mockEntity).iterator();
    when(mockMap.iterator()).thenReturn(iterator);

    TargetStrategy strategy = new TargetTypeStrategy(mockMap, "enemy", 1);
    int[] result = strategy.getTargetPosition();

    // default case is moving up
    assertArrayEquals(new int[]{4, 3}, result);
  }

  @DisplayName("Current default behavior is to target 0, 0")
  @Test
  void getTargetPosition_NoValidEntity_returnsDefaultLocation() {
    // Empty iterator
    Iterator<Entity> iterator = Collections.emptyIterator();
    when(mockMap.iterator()).thenReturn(iterator);

    TargetStrategy strategy = new TargetTypeStrategy(mockMap, "enemy", 2);
    int[] result = strategy.getTargetPosition();

    assertArrayEquals(new int[]{0, 0}, result);
  }
}
