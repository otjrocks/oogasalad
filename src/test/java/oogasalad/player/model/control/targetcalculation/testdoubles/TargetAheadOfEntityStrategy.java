package oogasalad.player.model.control.targetcalculation.testdoubles;

import java.util.Map;
import oogasalad.engine.model.GameMapInterface;
import oogasalad.player.model.control.targetcalculation.TargetStrategyInterface;

public class TargetAheadOfEntityStrategy implements TargetStrategyInterface {

  public TargetAheadOfEntityStrategy(GameMapInterface map, Map<String, Object> config) {
  }

  @Override
  public int[] getTargetPosition() {
    return new int[0];
  }
}
