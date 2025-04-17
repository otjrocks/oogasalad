package oogasalad.player.model.control.targetcalculation.testdoubles;

import java.util.Map;
import oogasalad.engine.model.GameMapInterface;
import oogasalad.player.model.control.targetcalculation.TargetStrategyInterface;

// Dummy strategy classes for testing
public class TargetLocationStrategy implements TargetStrategyInterface {

  public TargetLocationStrategy(GameMapInterface map, Map<String, Object> config) {
  }

  @Override
  public int[] getTargetPosition() {
    return new int[0];
  }
}
