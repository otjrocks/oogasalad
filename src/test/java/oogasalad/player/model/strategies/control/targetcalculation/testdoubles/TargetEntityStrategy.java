package oogasalad.player.model.strategies.control.targetcalculation.testdoubles;

import java.util.Map;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.strategies.control.targetcalculation.TargetStrategyInterface;

public class TargetEntityStrategy implements TargetStrategyInterface {

  public TargetEntityStrategy(GameMapInterface map, Map<String, Object> config) {
  }

  @Override
  public int[] getTargetPosition() {
    return new int[0];
  }
}
