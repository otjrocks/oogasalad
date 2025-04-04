package oogasalad.player.model.movement.targetcalculation;

import java.util.Map;
import java.util.Optional;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.exceptions.TargetStrategyException;

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
public class TargetAheadOfEntityStrategy implements TargetStrategy {

  private final GameMap myGameMap;
  private final String myTargetType;
  private final int myTilesAhead;

  /**
   * Constructs a TargetAheadOfEntityStrategy object. This strategy calculates the target based on
   * the tiles ahead of an entity on the game map.
   *
   * @param gameMap        the game map used to determine the positions of entities and tiles
   * @param strategyConfig a map containing configuration parameters for the strategy, including the
   *                       target type and the number of tiles ahead to consider
   * @throws IllegalArgumentException if the strategyConfig is invalid or missing required
   *                                  parameters
   */
  public TargetAheadOfEntityStrategy(GameMap gameMap, Map<String, Object> strategyConfig) {
    myGameMap = gameMap;
    myTargetType = TargetStrategyHelperMethods.validateAndGetTargetType(strategyConfig);
    myTilesAhead = validateAndGetTilesAhead(strategyConfig);
  }

  @Override
  public int[] getTargetPosition() {
    Optional<Entity> entity = TargetStrategyHelperMethods.findFirstEntityOfType(myGameMap,
        myTargetType);

    // if no entity, no target so stay where you are
    // or random movement (design choice for later)
    return entity.map(value -> calcTargetPosition(value.getEntityDirection(),
        (int) value.getEntityPlacement().getX(),
        (int) value.getEntityPlacement().getY())).orElseGet(() -> new int[]{0, 0});

  }

  private int[] calcTargetPosition(char dir, int x, int y) {
    int targetX = x;
    int targetY = y;

    switch (dir) {

      case 'R':
        targetX += myTilesAhead;
        break;
      case 'D':
        targetY += myTilesAhead;
        break;
      case 'L':
        targetX -= myTilesAhead;
        break;
      default: // default up
        targetY -= myTilesAhead;
        break;
    }
    return new int[]{targetX, targetY};
  }

  private static int validateAndGetTilesAhead(Map<String, Object> strategyConfig) {
    final String TILES_AHEAD_KEY = "tilesAhead";
    
    if (strategyConfig.containsKey(TILES_AHEAD_KEY) && strategyConfig.get(TILES_AHEAD_KEY) != null) {
      try {
      return Integer.parseInt(strategyConfig.get(TILES_AHEAD_KEY).toString());
      } catch (NumberFormatException e) {
      throw new TargetStrategyException("tilesAhead must be an integer", e);
      }
    }

    // can log a warning, but may be annoying
    return 0;
  }
}
