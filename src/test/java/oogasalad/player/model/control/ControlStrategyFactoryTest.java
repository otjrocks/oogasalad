package oogasalad.player.model.control;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityTypeRecord;
import oogasalad.engine.model.GameMapInterface;
import oogasalad.engine.model.controlConfig.*;
import oogasalad.player.model.exceptions.ControlStrategyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ControlStrategyFactoryTest {

  private GameInputManager mockInput;
  private GameMapInterface mockMap;
  private EntityPlacement mockPlacement;

  @BeforeEach
  void setup() throws Exception {
    mockInput = mock(GameInputManager.class);
    mockMap = mock(GameMapInterface.class);
    mockPlacement = mock(EntityPlacement.class);

    // Set up mock control type
    EntityTypeRecord mockType = mock(EntityTypeRecord.class);
    when(mockPlacement.getType()).thenReturn(mockType);

    // Override the strategy package for testing
    Field strategyPackageField = ControlStrategyFactory.class.getDeclaredField("STRATEGY_PACKAGE");
    strategyPackageField.setAccessible(true);
    strategyPackageField.set(null, "oogasalad.player.model.control.testdoubles.");
  }


  private void mockControlConfig(ControlConfigInterface config) {
    EntityTypeRecord mockType = mock(EntityTypeRecord.class);
    when(mockType.controlConfig()).thenReturn(config);
    when(mockPlacement.getType()).thenReturn(mockType);
  }

  @Test
  void createControlStrategy_noneControl_strategyCreated() {
    ControlConfigInterface config = new NoneControlConfigRecord();
    mockControlConfig(config);

    ControlStrategyInterface strategy = ControlStrategyFactory.createControlStrategy(mockInput, mockPlacement, mockMap);
    assertInstanceOf(oogasalad.player.model.control.testdoubles.NoneControlStrategy.class, strategy);
  }

  @Test
  void createControlStrategy_keyboardControl_strategyCreated() {
    ControlConfigInterface config = new KeyboardControlConfigRecord();
    mockControlConfig(config);

    ControlStrategyInterface strategy = ControlStrategyFactory.createControlStrategy(mockInput, mockPlacement, mockMap);
    assertInstanceOf(oogasalad.player.model.control.testdoubles.KeyboardControlStrategy.class, strategy);
  }

  @Test
  void createControlStrategy_targetControl_strategyCreated() {
    ControlConfigInterface config = new TargetControlConfigRecord("A*", null);
    mockControlConfig(config);

    ControlStrategyInterface strategy = ControlStrategyFactory.createControlStrategy(mockInput, mockPlacement, mockMap);
    assertInstanceOf(oogasalad.player.model.control.testdoubles.TargetControlStrategy.class, strategy);
  }

  @Test
  void createControlStrategy_conditionalControl_strategyCreated() {
    ControlConfigInterface config = new ConditionalControlConfigRecord(5, "A*", "Dijkstra", null);
    mockControlConfig(config);

    ControlStrategyInterface strategy = ControlStrategyFactory.createControlStrategy(mockInput, mockPlacement, mockMap);
    assertInstanceOf(oogasalad.player.model.control.testdoubles.ConditionalControlStrategy.class, strategy);
  }

  @Test
  void createControlStrategy_invalidStrategyControl_throwsControlStrategyException() {
    ControlConfigInterface config = new ControlConfigInterface() {}; // anonymous class to simulate an unknown config
    mockControlConfig(config);

    assertThrows(ControlStrategyException.class,
        () -> ControlStrategyFactory.createControlStrategy(mockInput, mockPlacement, mockMap));
  }
}
