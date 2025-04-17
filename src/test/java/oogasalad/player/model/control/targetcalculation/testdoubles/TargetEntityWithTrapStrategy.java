package oogasalad.player.model.control.targetcalculation.testdoubles;

import java.util.Map;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.control.targetcalculation.TargetStrategyInterface;

public class TargetEntityWithTrapStrategy implements TargetStrategyInterface {

  public TargetEntityWithTrapStrategy(GameMap map, Map<String, Object> config, String typeString) {
  }

  @Override
  public int[] getTargetPosition() {
    return new int[0];
  }
}
