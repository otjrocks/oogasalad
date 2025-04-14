package oogasalad.player.model.control;

import java.util.Map;
import oogasalad.player.model.exceptions.TargetStrategyException;

public class ControlStrategyHelperMethods {

  static String validateAndGetPathFindingStrategy(Map<String, Object> strategyConfig, String key) {
    if (!strategyConfig.containsKey(key)
        || strategyConfig.get(key) == null) {
      throw new TargetStrategyException("Type " + key + " is required");
    }

    return strategyConfig.get(key).toString();
  }

}
