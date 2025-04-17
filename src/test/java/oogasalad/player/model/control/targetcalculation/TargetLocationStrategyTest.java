package oogasalad.player.model.control.targetcalculation;

import oogasalad.engine.model.GameMapInterface;
import oogasalad.player.model.exceptions.TargetStrategyException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TargetLocationStrategyTest {

  @Test
  void getTargetPosition_properConfig_returnsCorrectCoordinates() {
    GameMapInterface mockMap = mock(GameMapInterface.class);
    Map<String, Object> config = Map.of("targetX", 7, "targetY", 3);

    TargetStrategyInterface strategy = new TargetLocationStrategy(mockMap, config);

    assertArrayEquals(new int[]{7, 3}, strategy.getTargetPosition());
  }

  @Test
  void getTargetPosition_missingConfig_throwsException() {
    GameMapInterface mockMap = mock(GameMapInterface.class);
    Map<String, Object> config = Map.of("targetX", 7);

    assertThrows(TargetStrategyException.class, () -> new TargetLocationStrategy(mockMap, config));
  }
}
