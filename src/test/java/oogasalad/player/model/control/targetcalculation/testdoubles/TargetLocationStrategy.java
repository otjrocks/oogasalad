package oogasalad.player.model.control.targetcalculation.testdoubles;

import java.util.Map;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.control.targetcalculation.TargetStrategyInterface;

// Dummy strategy classes for testing
public class TargetLocationStrategy implements TargetStrategyInterface {

  public TargetLocationStrategy(GameMap map, Map<String, Object> config) {
  }

  @Override
  public int[] getTargetPosition() {
    return new int[0];
  }
}
