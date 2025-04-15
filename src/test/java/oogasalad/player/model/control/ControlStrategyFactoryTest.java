package oogasalad.player.model.control;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.controlConfig.*;
import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.exceptions.ControlStrategyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Dummy strategy classes for testing
class NoneControlStrategy implements ControlStrategy {
  public NoneControlStrategy() {
    // empty constructor
  }

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}

class KeyboardControlStrategy implements ControlStrategy {
  public KeyboardControlStrategy(GameInputManager input, GameMap map, EntityPlacement placement) {}

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}

class TargetControlStrategy implements ControlStrategy {
  public TargetControlStrategy(GameMap map, EntityPlacement placement, ControlConfig config) {}

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}

class ConditionalControlStrategy implements ControlStrategy {
  public ConditionalControlStrategy(GameMap map, EntityPlacement placement, ControlConfig config) {}

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}


public class ControlStrategyFactoryTest {

  private GameInputManager mockInput;
  private GameMap mockMap;
  private EntityPlacement mockPlacement;

  @BeforeEach
  void setup() {
    mockInput = mock(GameInputManager.class);
    mockMap = mock(GameMap.class);
    mockPlacement = mock(EntityPlacement.class);
  }

  private void mockControlConfig(ControlConfig config) {
    EntityType mockType = mock(EntityType.class);
    when(mockType.controlConfig()).thenReturn(config);
    when(mockPlacement.getType()).thenReturn(mockType);
  }

  @Test
  void createControlStrategy_noneControl_strategyCreated() {
    ControlConfig config = new NoneControlConfig();
    mockControlConfig(config);

    ControlStrategy strategy = ControlStrategyFactory.createControlStrategy(mockInput, mockPlacement, mockMap);
    assertInstanceOf(NoneControlStrategy.class, strategy);
  }

  @Test
  void createControlStrategy_keyboardControl_strategyCreated() {
    ControlConfig config = new KeyboardControlConfig();
    mockControlConfig(config);

    ControlStrategy strategy = ControlStrategyFactory.createControlStrategy(mockInput, mockPlacement, mockMap);
    assertInstanceOf(KeyboardControlStrategy.class, strategy);
  }

  @Test
  void createControlStrategy_targetControl_strategyCreated() {
    ControlConfig config = new TargetControlConfig("A*", null);
    mockControlConfig(config);

    ControlStrategy strategy = ControlStrategyFactory.createControlStrategy(mockInput, mockPlacement, mockMap);
    assertInstanceOf(TargetControlStrategy.class, strategy);
  }

  @Test
  void createControlStrategy_conditionalControl_strategyCreated() {
    ControlConfig config = new ConditionalControlConfig(5, "A*", "Dijkstra", null);
    mockControlConfig(config);

    ControlStrategy strategy = ControlStrategyFactory.createControlStrategy(mockInput, mockPlacement, mockMap);
    assertInstanceOf(ConditionalControlStrategy.class, strategy);
  }

  @Test
  void createControlStrategy_invalidStrategyControl_throwsControlStrategyException() {
    ControlConfig config = new ControlConfig() {}; // anonymous class to simulate an unknown config
    mockControlConfig(config);

    assertThrows(ControlStrategyException.class,
        () -> ControlStrategyFactory.createControlStrategy(mockInput, mockPlacement, mockMap));
  }
}
