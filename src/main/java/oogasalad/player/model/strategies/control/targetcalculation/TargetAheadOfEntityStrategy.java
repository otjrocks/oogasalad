package oogasalad.player.model.strategies.control.targetcalculation;

import java.util.Map;
import java.util.Optional;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.Entity;

/**
 * The TargetAheadOfEntityStrategy class implements the TargetStrategy interface and provides a
 * mechanism to calculate a target position based on a specified number of tiles ahead of an entity
 * on the game map. The direction of movement is determined by the entity's current direction.
 *
 * <p>This strategy is useful in scenarios where an entity needs to target a position ahead
 * of its current location, such as in games involving movement or navigation logic.</p>
 *
 * <p>Key features of this class include:</p>
 * <ul>
 *   <li>Validation of configuration parameters to ensure proper initialization.</li>
 *   <li>Calculation of target positions based on the entity's direction and a configurable
 *       number of tiles ahead.</li>
 *   <li>Fallback behavior when no entity of the specified type is found on the game map.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * Map<String, Object> strategyConfig = new HashMap<>();
 * strategyConfig.put("targetType", "player");
 * strategyConfig.put("tilesAhead", 3);
 *
 * TargetStrategy strategy = new TargetAheadOfEntityStrategy(gameMap, strategyConfig);
 * int[] targetPosition = strategy.getTargetPosition();
 * }
 * </pre>
 *
 * <p>Note: The behavior when no entity is found can be customized in the future, such as
 * implementing random movement or other fallback strategies. Right now it just
 * sits still</p>
 *
 * @author Jessica Chen
 * @author Chatgpt for javadoc
 */
public class TargetAheadOfEntityStrategy implements TargetStrategyInterface {

  private final GameMapInterface myGameMap;
  private final String myTargetType;
  private final int myTilesAhead;
  private final String myType;

  /**
   * Constructs a TargetAheadOfEntityStrategy object. This strategy calculates the target based on
   * the tiles ahead of an entity on the game map.
   *
   * @param gameMap        the game map used to determine the positions of entities and tiles
   * @param strategyConfig a map containing configuration parameters for the strategy, including the
   *                       target type and the number of tiles ahead to consider
   * @param typeOfCaller   type of caller to use in checking if target is not a blocked position
   * @throws IllegalArgumentException if the strategyConfig is invalid or missing required
   *                                  parameters
   */
  public TargetAheadOfEntityStrategy(GameMapInterface gameMap, Map<String, Object> strategyConfig,
      String typeOfCaller) {
    myGameMap = gameMap;
    myTargetType = TargetStrategyHelperMethods.validateAndGetKeyString(strategyConfig,
        "targetType");
    myTilesAhead = TargetStrategyHelperMethods.validateAndGetKeyInt(strategyConfig, "tilesAhead");
    myType = typeOfCaller;
  }

  @Override
  public int[] getTargetPosition() {
    Optional<Entity> entity = TargetStrategyHelperMethods.findFirstEntityOfType(myGameMap,
        myTargetType);

    return entity.map(
        value -> TargetStrategyHelperMethods.calcTargetPosition(myGameMap, value, myType,
            myTilesAhead)).orElseGet(() -> new int[]{0, 0});

  }

}
