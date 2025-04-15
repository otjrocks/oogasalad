package oogasalad.player.model.control.targetcalculation.testdoubles;

import java.util.Map;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.control.targetcalculation.TargetStrategy;

// Dummy strategy classes for testing
public class TargetLocationStrategy implements TargetStrategy {

  public TargetLocationStrategy(GameMap map, Map<String, Object> config) {
  }

  @Override
  public int[] getTargetPosition() {
    return new int[0];
  }
}
