package oogasalad.player.model.control.targetcalculation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.controlConfig.TargetControlConfig;
import oogasalad.engine.model.controlConfig.ConditionalControlConfig;
import oogasalad.engine.model.controlConfig.targetStrategy.*;
import oogasalad.player.model.exceptions.TargetStrategyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TargetStrategyFactoryTest {

  private GameMap mockMap;
  private EntityPlacement mockPlacement;
  private EntityType mockType;

  @BeforeEach
  void setup() throws Exception {
    mockMap = mock(GameMap.class);
    mockPlacement = mock(EntityPlacement.class);
    mockType = mock(EntityType.class);
    when(mockPlacement.getType()).thenReturn(mockType);
    when(mockPlacement.getTypeString()).thenReturn("SomeType");

    // Override the package used by the factory for class lookup
    Field pkgField = TargetStrategyFactory.class.getDeclaredField("STRATEGY_PACKAGE");
    pkgField.setAccessible(true);
    pkgField.set(null, "oogasalad.player.model.control.targetcalculation.testdoubles.");
  }

  private void mockTargetControlConfig(TargetCalculationConfig config) {
    TargetControlConfig controlConfig = new TargetControlConfig("", config);
    when(mockType.controlConfig()).thenReturn(controlConfig);
  }

  private void mockConditionalControlConfig(TargetCalculationConfig config) {
    ConditionalControlConfig controlConfig = new ConditionalControlConfig(3, "", "", config);
    when(mockType.controlConfig()).thenReturn(controlConfig);
  }

  @Test
  void createTargetStrategy_targetLocationStrategy_createdSuccessfully() {
    mockTargetControlConfig(new TargetLocationConfig(10.0, 20.0));
    TargetStrategyInterface strategy = TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap);
    assertInstanceOf(
        oogasalad.player.model.control.targetcalculation.testdoubles.TargetLocationStrategy.class, strategy);
  }

  @Test
  void createTargetStrategy_targetEntityStrategy_createdSuccessfully() {
    mockTargetControlConfig(new TargetEntityConfig("Enemy"));
    TargetStrategyInterface strategy = TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap);
    assertInstanceOf(
        oogasalad.player.model.control.targetcalculation.testdoubles.TargetEntityStrategy.class, strategy);
  }

  @Test
  void createTargetStrategy_targetAheadOfEntityStrategy_createdSuccessfully() {
    mockTargetControlConfig(new TargetAheadOfEntityConfig("Ally", 2));
    TargetStrategyInterface strategy = TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap);
    assertInstanceOf(
        oogasalad.player.model.control.targetcalculation.testdoubles.TargetAheadOfEntityStrategy.class, strategy);
  }

  @Test
  void createTargetStrategy_targetEntityWithTrapStrategy_createdSuccessfully() {
    mockConditionalControlConfig(new TargetEntityWithTrapConfig("Enemy", 1, "Trap"));
    TargetStrategyInterface strategy = TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap);
    assertInstanceOf(
        oogasalad.player.model.control.targetcalculation.testdoubles.TargetEntityWithTrapStrategy.class, strategy);
  }

  // NEGATIVE TESTS

  @Test
  void createTargetStrategy_unknownControlConfig_throwsException() {
    ControlConfig unknownConfig = mock(ControlConfig.class); // Not TargetControlConfig or ConditionalControlConfig
    when(mockType.controlConfig()).thenReturn(unknownConfig);
    TargetStrategyException exception = assertThrows(
        TargetStrategyException.class,
        () -> TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap)
    );
    assertTrue(exception.getMessage().contains("No TargetStrategy available"));
  }

  @Test
  void createTargetStrategy_noValidConstructor_throwsException() {
    mockTargetControlConfig(new TargetNoConstructorConfig("Nope"));
    assertThrows(
        TargetStrategyException.class,
        () -> TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap)
    );
  }

  // Dummy config and class with no matching constructor
  record TargetNoConstructorConfig(String key) implements TargetCalculationConfig {}

  static class TargetNoConstructorStrategy implements TargetStrategyInterface {
    private TargetNoConstructorStrategy() {} // Private constructor
    @Override
    public int[] getTargetPosition() {
      return new int[0];
    }
  }
}