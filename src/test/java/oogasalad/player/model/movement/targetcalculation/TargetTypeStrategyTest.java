package oogasalad.player.model.movement.targetcalculation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import oogasalad.engine.model.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TargetTypeStrategyTest {

  private GameMap mockMap;

  @BeforeEach
  public void setUp() {
    mockMap = mock(GameMap.class);
  }

  // tests for get Target Position once if it exists, once if it does not
  @Test
  void getTargetPosition_noTilesAheadValidEntity_returnsEntityLocation() {
  }

  @Test
  void getTargetPosition_tilesAheadValidEntityWithDirection_returnsEntityLocationNAhead() {}

  @Test
  void getTargetPosition_tilesAheadValidEntityWithoutDirection_returnsEntityLocationNTilesAbove() {}

  @DisplayName("Current default behavior is to target 0, 0")
  @Test
  void getTargetPosition_NoValidEntity_returnsDefaultLocation() {}


}
