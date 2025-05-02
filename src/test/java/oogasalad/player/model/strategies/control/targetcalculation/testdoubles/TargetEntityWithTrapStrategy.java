package oogasalad.player.model.strategies.control.targetcalculation.testdoubles;

import java.util.Map;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.strategies.control.targetcalculation.TargetStrategyInterface;

public class TargetEntityWithTrapStrategy implements TargetStrategyInterface {

  public TargetEntityWithTrapStrategy(GameMapInterface map, Map<String, Object> config,
      String typeString) {
  }

  @Override
  public int[] getTargetPosition() {
    return new int[0];
  }
}
