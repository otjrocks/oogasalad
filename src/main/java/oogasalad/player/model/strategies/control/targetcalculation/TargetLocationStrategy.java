package oogasalad.player.model.strategies.control.targetcalculation;

import java.util.Map;
import oogasalad.player.model.GameMapInterface;

/**
 * A strategy for determining the target location based on the configuration. This class is
 * responsible for setting the location to one specified in the configuration.
 */
public class TargetLocationStrategy implements TargetStrategyInterface {

  private final int myTargetX;
  private final int myTargetY;

  /**
   * Constructs a TargetEntityStrategy object that determines the target based on where that target
   * is on the map
   *
   * @param gameMap        the game map on which the target calculation is performed
   * @param strategyConfig strategy configuration containing the target type
   */
  public TargetLocationStrategy(GameMapInterface gameMap, Map<String, Object> strategyConfig) {
    myTargetX = TargetStrategyHelperMethods.validateAndGetKeyInt(strategyConfig, "targetX");
    myTargetY = TargetStrategyHelperMethods.validateAndGetKeyInt(strategyConfig, "targetY");
  }


  @Override
  public int[] getTargetPosition() {
    return new int[]{myTargetX, myTargetY};
  }
}
