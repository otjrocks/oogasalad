package oogasalad.player.model.control.targetcalculation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityTypeRecord;
import oogasalad.engine.model.GameMapInterface;
import oogasalad.engine.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.model.controlConfig.TargetControlConfigRecord;
import oogasalad.engine.model.controlConfig.ConditionalControlConfigRecord;
import oogasalad.engine.model.controlConfig.targetStrategy.*;
import oogasalad.player.model.exceptions.TargetStrategyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TargetStrategyFactoryTest {

  private GameMapInterface mockMap;
  private EntityPlacement mockPlacement;
  private EntityTypeRecord mockType;

  @BeforeEach
  void setup() throws Exception {
    mockMap = mock(GameMapInterface.class);
    mockPlacement = mock(EntityPlacement.class);
    mockType = mock(EntityTypeRecord.class);
    when(mockPlacement.getType()).thenReturn(mockType);
    when(mockPlacement.getTypeString()).thenReturn("SomeType");

    // Override the package used by the factory for class lookup
    Field pkgField = TargetStrategyFactory.class.getDeclaredField("STRATEGY_PACKAGE");
    pkgField.setAccessible(true);
    pkgField.set(null, "oogasalad.player.model.control.targetcalculation.testdoubles.");
  }

  private void mockTargetControlConfig(TargetCalculationConfigInterface config) {
    TargetControlConfigRecord controlConfig = new TargetControlConfigRecord("", config);
    when(mockType.controlConfig()).thenReturn(controlConfig);
  }

  private void mockConditionalControlConfig(TargetCalculationConfigInterface config) {
    ConditionalControlConfigRecord controlConfig = new ConditionalControlConfigRecord(3, "", "", config);
    when(mockType.controlConfig()).thenReturn(controlConfig);
  }

  @Test
  void createTargetStrategy_targetLocationStrategy_createdSuccessfully() {
    mockTargetControlConfig(new TargetLocationConfigRecord(10.0, 20.0));
    TargetStrategyInterface strategy = TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap);
    assertInstanceOf(
        oogasalad.player.model.control.targetcalculation.testdoubles.TargetLocationStrategy.class, strategy);
  }

  @Test
  void createTargetStrategy_targetEntityStrategy_createdSuccessfully() {
    mockTargetControlConfig(new TargetEntityConfigRecord("Enemy"));
    TargetStrategyInterface strategy = TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap);
    assertInstanceOf(
        oogasalad.player.model.control.targetcalculation.testdoubles.TargetEntityStrategy.class, strategy);
  }

  @Test
  void createTargetStrategy_targetAheadOfEntityStrategy_createdSuccessfully() {
    mockTargetControlConfig(new TargetAheadOfEntityConfigRecord("Ally", 2));
    TargetStrategyInterface strategy = TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap);
    assertInstanceOf(
        oogasalad.player.model.control.targetcalculation.testdoubles.TargetAheadOfEntityStrategy.class, strategy);
  }

  @Test
  void createTargetStrategy_targetEntityWithTrapStrategy_createdSuccessfully() {
    mockConditionalControlConfig(new TargetEntityWithTrapConfigRecord("Enemy", 1, "Trap"));
    TargetStrategyInterface strategy = TargetStrategyFactory.createTargetStrategy(mockPlacement, mockMap);
    assertInstanceOf(
        oogasalad.player.model.control.targetcalculation.testdoubles.TargetEntityWithTrapStrategy.class, strategy);
  }

  // NEGATIVE TESTS

  @Test
  void createTargetStrategy_unknownControlConfig_throwsException() {
    ControlConfigInterface unknownConfig = mock(ControlConfigInterface.class); // Not TargetControlConfig or ConditionalControlConfig
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
  record TargetNoConstructorConfig(String key) implements TargetCalculationConfigInterface {}

  static class TargetNoConstructorStrategy implements TargetStrategyInterface {
    private TargetNoConstructorStrategy() {} // Private constructor
    @Override
    public int[] getTargetPosition() {
      return new int[0];
    }
  }
}