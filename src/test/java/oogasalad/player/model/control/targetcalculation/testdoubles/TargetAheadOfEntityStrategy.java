package oogasalad.player.model.control.targetcalculation.testdoubles;

import java.util.Map;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.control.targetcalculation.TargetStrategy;

public class TargetAheadOfEntityStrategy implements TargetStrategy {

  public TargetAheadOfEntityStrategy(GameMap map, Map<String, Object> config) {
  }

  @Override
  public int[] getTargetPosition() {
    return new int[0];
  }
}
