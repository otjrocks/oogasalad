package oogasalad.player.model.control.targetcalculation.testdoubles;

import java.util.Map;
import oogasalad.engine.model.GameMapInterface;
import oogasalad.player.model.control.targetcalculation.TargetStrategyInterface;

public class TargetEntityWithTrapStrategy implements TargetStrategyInterface {

  public TargetEntityWithTrapStrategy(GameMapInterface map, Map<String, Object> config, String typeString) {
  }

  @Override
  public int[] getTargetPosition() {
    return new int[0];
  }
}
