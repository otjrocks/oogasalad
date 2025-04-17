package oogasalad.player.model.strategies.control.targetcalculation.testdoubles;

import java.util.Map;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.strategies.control.targetcalculation.TargetStrategyInterface;

// Dummy strategy classes for testing
public class TargetLocationStrategy implements TargetStrategyInterface {

  public TargetLocationStrategy(GameMapInterface map, Map<String, Object> config) {
  }

  @Override
  public int[] getTargetPosition() {
    return new int[0];
  }
}
