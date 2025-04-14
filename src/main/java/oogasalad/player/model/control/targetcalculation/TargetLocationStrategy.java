package oogasalad.player.model.control.targetcalculation;

import java.util.Map;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.exceptions.TargetStrategyException;

/**
 * A strategy for determining the target location based on the configuration. This class is
 * responsible for setting the location to one specified in the configuration.
 */
public class TargetLocationStrategy implements TargetStrategy {

  private final int myTargetX;
  private final int myTargetY;

  /**
   * Constructs a TargetEntityStrategy object that determines the target based on where that target
   * is on the map
   *
   * @param gameMap        the game map on which the target calculation is performed
   * @param strategyConfig strategy configuration containing the target type
   */
  public TargetLocationStrategy(GameMap gameMap, Map<String, Object> strategyConfig) {
    myTargetX = validateAndGetTargetLocation(strategyConfig, "targetX");
    myTargetY = validateAndGetTargetLocation(strategyConfig, "targetY");
  }


  @Override
  public int[] getTargetPosition() {
    return new int[]{myTargetX, myTargetY};
  }

  private int validateAndGetTargetLocation(Map<String, Object> strategyConfig, String key) {
    if (!strategyConfig.containsKey(key) || strategyConfig.get(key) == null) {
      throw new TargetStrategyException("Target location is required for " + key);
    }

    Object value = strategyConfig.get(key);

    if (value instanceof Number) {
      return ((Number) value).intValue();
    } else if (value instanceof String) {
      try {
        return Integer.parseInt((String) value);
      } catch (NumberFormatException e) {
        throw new TargetStrategyException(
            "Target location must be a valid number for " + key + ", currently: " + value, e);
      }
    } else {
      throw new TargetStrategyException(
          "Target location must be a number for " + key + ", currently: " + value + " ("
              + value.getClass().getSimpleName() + ")");
    }
  }
}
