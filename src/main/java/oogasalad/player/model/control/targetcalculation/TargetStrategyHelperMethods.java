package oogasalad.player.model.control.targetcalculation;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.exceptions.TargetStrategyException;

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

  /**
   * Validates the provided strategy configuration map to ensure it contains a "targetType" key
   * and retrieves its corresponding value as a string.
   *
   * @param strategyConfig a map containing the configuration for the target strategy.
   *                       It must include a "targetType" key.
   * @return the value associated with the "targetType" key as a string.
   * @throws TargetStrategyException if the "targetType" key is not present in the strategyConfig map.
   */
   static String validateAndGetTargetType(Map<String, Object> strategyConfig) {
    final String TARGET_TYPE_KEY = "targetType";
    if (!strategyConfig.containsKey(TARGET_TYPE_KEY) || strategyConfig.get(TARGET_TYPE_KEY) == null) {
      throw new TargetStrategyException("Target type is required");
    }

    return strategyConfig.get("targetType").toString();
  }
}
