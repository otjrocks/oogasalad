package oogasalad.player.model.strategies.control.targetcalculation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.Map;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.exceptions.TargetStrategyException;
import org.junit.jupiter.api.Test;

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
