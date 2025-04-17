package oogasalad.player.model.control.targetcalculation;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.entity.Entity;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TargetEntityWithTrapStrategyTest {

  @Test
  void getTargetPosition_withTeammate_returnsRotatedTarget() {
    GameMap mockMap = mock(GameMap.class);
    Entity targetEntity = mock(Entity.class);
    Entity teammateEntity = mock(Entity.class);
    EntityPlacement targetPlacement = mock(EntityPlacement.class);
    EntityPlacement teammatePlacement = mock(EntityPlacement.class);

    // Target entity position and direction
    when(targetPlacement.getX()).thenReturn(3.0);
    when(targetPlacement.getY()).thenReturn(3.0);
    when(targetPlacement.getTypeString()).thenReturn("Enemy");
    when(targetEntity.getEntityPlacement()).thenReturn(targetPlacement);
    when(targetEntity.getEntityDirection()).thenReturn(Direction.R);

    // Teammate position
    when(teammatePlacement.getX()).thenReturn(1.0);
    when(teammatePlacement.getY()).thenReturn(3.0);
    when(teammatePlacement.getTypeString()).thenReturn("Trap");
    when(teammateEntity.getEntityPlacement()).thenReturn(teammatePlacement);

    // GameMap setup
    when(mockMap.iterator()).thenReturn(java.util.List.of(targetEntity, teammateEntity).iterator());
    when(mockMap.isValidPosition(4, 3)).thenReturn(true);
    when(mockMap.isNotBlocked("Caller", 4, 3)).thenReturn(true);

    Map<String, Object> config = Map.of(
        "targetType", "Enemy",
        "teamEntityType", "Trap",
        "tilesAhead", 1
    );

    TargetStrategy strategy = new TargetEntityWithTrapStrategy(mockMap, config, "Caller");

    // Original ahead target = (4, 3)
    // Teammate = (1, 3)
    // Rotated = (2*4 - 1, 2*3 - 3) = (7, 3)
    assertArrayEquals(new int[]{7, 3}, strategy.getTargetPosition());
  }

  @Test
  void getTargetPosition_noTeammate_returnsUnrotatedTargetAkaDefaultAheadOfTarget() {
    GameMap mockMap = mock(GameMap.class);
    Entity targetEntity = mock(Entity.class);
    EntityPlacement targetPlacement = mock(EntityPlacement.class);

    when(targetPlacement.getX()).thenReturn(3.0);
    when(targetPlacement.getY()).thenReturn(2.0);
    when(targetPlacement.getTypeString()).thenReturn("Enemy");
    when(targetEntity.getEntityPlacement()).thenReturn(targetPlacement);
    when(targetEntity.getEntityDirection()).thenReturn(Direction.D);

    when(mockMap.iterator()).thenReturn(java.util.List.of(targetEntity).iterator());
    when(mockMap.isValidPosition(3, 3)).thenReturn(true);
    when(mockMap.isNotBlocked("Caller", 3, 3)).thenReturn(true);

    Map<String, Object> config = Map.of(
        "targetType", "Enemy",
        "teamEntityType", "Trap", // But no Trap on map
        "tilesAhead", 1
    );

    TargetStrategy strategy = new TargetEntityWithTrapStrategy(mockMap, config, "Caller");

    // No teammate → just return position ahead
    assertArrayEquals(new int[]{3, 3}, strategy.getTargetPosition());
  }

  @Test
  void getTargetPosition_noTargetEntity_returnsDefaultZero() {
    GameMap mockMap = mock(GameMap.class);
    when(mockMap.iterator()).thenReturn(java.util.Collections.emptyIterator());

    Map<String, Object> config = Map.of(
        "targetType", "Enemy",
        "teamEntityType", "Trap",
        "tilesAhead", 2
    );

    TargetStrategy strategy = new TargetEntityWithTrapStrategy(mockMap, config, "Caller");

    assertArrayEquals(new int[]{0, 0}, strategy.getTargetPosition());
  }
}
