package oogasalad.player.model.control.targetcalculation.testdoubles;

import java.util.Map;
import oogasalad.engine.model.GameMapInterface;
import oogasalad.player.model.control.targetcalculation.TargetStrategyInterface;

public class TargetEntityStrategy implements TargetStrategyInterface {

  public TargetEntityStrategy(GameMapInterface map, Map<String, Object> config) {
  }

  @Override
  public int[] getTargetPosition() {
    return new int[0];
  }
}
