package oogasalad.player.model.control;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.exceptions.ControlStrategyException;
import org.junit.jupiter.api.Test;

public class ControlStrategyFactoryTest {

  @Test
  void createControlStrategy_validNoneStrategy_returnsInstanceOfNoneStrategy() {
    GameInputManager input = mock(GameInputManager.class);
    GameMap gameMap = mock(GameMap.class);
    EntityPlacement placement = mock(EntityPlacement.class);
    EntityType type = mock(EntityType.class);

    when(placement.getType()).thenReturn(type);
    when(type.controlType()).thenReturn("None");

    ControlStrategy strategy = ControlStrategyFactory.createControlStrategy(input, placement, gameMap);
    assertNotNull(strategy);
    assertEquals(NoneControlStrategy.class, strategy.getClass());
  }

  @Test
  void createControlStrategy_validTargetAheadOfEntityStrategy_returnsInstanceOfTargetStrategy() {
    GameInputManager input = mock(GameInputManager.class);
    GameMap gameMap = mock(GameMap.class);
    EntityPlacement placement = mock(EntityPlacement.class);
    EntityType type = mock(EntityType.class);

    Map<String, Object> config = new HashMap<>();
    config.put("targetType", "enemy");
    config.put("tilesAhead", 1);

    when(type.strategyConfig()).thenReturn(config);

    when(placement.getType()).thenReturn(type);
    when(type.controlType()).thenReturn("TargetAheadOfEntity");

    ControlStrategy strategy = ControlStrategyFactory.createControlStrategy(input, placement, gameMap);
    assertNotNull(strategy);
    assertEquals(TargetControlStrategy.class, strategy.getClass());
  }

  @Test
  void createControlStrategy_validTargetEntityStrategy_returnsInstanceOfTargetStrategy() {
    GameInputManager input = mock(GameInputManager.class);
    GameMap gameMap = mock(GameMap.class);
    EntityPlacement placement = mock(EntityPlacement.class);
    EntityType type = mock(EntityType.class);

    Map<String, Object> config = new HashMap<>();
    config.put("targetType", "enemy");
    config.put("tilesAhead", 1);

    when(type.strategyConfig()).thenReturn(config);

    when(placement.getType()).thenReturn(type);
    when(type.controlType()).thenReturn("TargetEntity");

    ControlStrategy strategy = ControlStrategyFactory.createControlStrategy(input, placement, gameMap);
    assertNotNull(strategy);
    assertEquals(TargetControlStrategy.class, strategy.getClass());
  }

  @Test
  void createControlStrategy_validKeyboardStrategy_returnsInstanceOfKeyboardStrategy() {
    GameInputManager input = mock(GameInputManager.class);
    GameMap gameMap = mock(GameMap.class);
    EntityPlacement placement = mock(EntityPlacement.class);
    EntityType type = mock(EntityType.class);

    when(placement.getType()).thenReturn(type);
    when(type.controlType()).thenReturn("Keyboard");

    ControlStrategy strategy = ControlStrategyFactory.createControlStrategy(input, placement, gameMap);
    assertNotNull(strategy);
    assertEquals(KeyboardControlStrategy.class, strategy.getClass());
  }

  @Test
  void createControlStrategy_invalidControlType_throwsException() {
    GameInputManager input = mock(GameInputManager.class);
    GameMap gameMap = mock(GameMap.class);
    EntityPlacement placement = mock(EntityPlacement.class);
    EntityType type = mock(EntityType.class);

    when(placement.getType()).thenReturn(type);
    when(type.controlType()).thenReturn("NonExistent");

    assertThrows(ControlStrategyException.class, () ->
        ControlStrategyFactory.createControlStrategy(input, placement, gameMap));
  }
}
