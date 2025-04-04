package oogasalad.player.model.movement.targetcalculation;

import java.util.Iterator;
import java.util.Optional;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;

/**
 * A utility class that provides helper methods for target calculation strategies
 * in a game. This class contains static methods to assist in finding and processing
 * entities within a game map based on specific criteria.
 *
 * <p>TargetStrategyHelperMethods is designed to be used as a helper class and
 * should not be instantiated. It provides reusable functionality to simplify
 * target-related operations in the game logic.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * Optional<Entity> targetEntity = TargetStrategyHelperMethods.findFirstEntityOfType(gameMap, "Enemy");
 * targetEntity.ifPresent(entity -> {
 *     // Perform actions with the found entity
 * });
 * }
 * </pre>
 * 
 * @author Jessica Chen
 * @author Chatgpt for javadoc
 */
class TargetStrategyHelperMethods {

  /**
   * Finds the first entity of a specified type within the given game map.
   *
   * @param gameMap   the game map to search through
   * @param targetType the type of entity to search for, specified as a string
   * @return an {@code Optional} containing the first entity of the specified type if found,
   *         or an empty {@code Optional} if no such entity exists
   */
  static Optional<Entity> findFirstEntityOfType(GameMap gameMap, String targetType) {
    Iterator<Entity> iterator = gameMap.iterator();
    while (iterator.hasNext()) {
      Entity entity = iterator.next();
      if (entity.getEntityPlacement().getTypeString()
          .equalsIgnoreCase(targetType)) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }
}
