package oogasalad.player.model.strategies.control.targetcalculation;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.exceptions.TargetStrategyException;

/**
 * A utility class that provides helper methods for target calculation strategies in a game. This
 * class contains static methods to assist in finding and processing entities within a game map
 * based on specific criteria.
 *
 * <p>TargetStrategyHelperMethods is designed to be used as a helper class and
 * should not be instantiated. It provides reusable functionality to simplify target-related
 * operations in the game logic.</p>
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
public class TargetStrategyHelperMethods {

  /**
   * Finds the first entity of a specified type within the given game map.
   *
   * @param gameMap    the game map to search through
   * @param targetType the type of entity to search for, specified as a string
   * @return an {@code Optional} containing the first entity of the specified type if found, or an
   * empty {@code Optional} if no such entity exists
   */
  static Optional<Entity> findFirstEntityOfType(GameMapInterface gameMap, String targetType) {
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
   * Validates the provided strategy configuration map to ensure it contains a "targetType" key and
   * retrieves its corresponding value as a string.
   *
   * @param strategyConfig a map containing the configuration for the target strategy. It must
   *                       include a "targetType" key.
   * @return the value associated with the "targetType" key as a string.
   * @throws TargetStrategyException if the "targetType" key is not present in the strategyConfig
   *                                 map.
   */
  static String validateAndGetKeyString(Map<String, Object> strategyConfig, String key) {
    if (!strategyConfig.containsKey(key)
        || strategyConfig.get(key) == null) {
      throw new TargetStrategyException("Type " + key + " is required");
    }

    return strategyConfig.get(key).toString();
  }

  static int validateAndGetKeyInt(Map<String, Object> strategyConfig, String key) {
    if (strategyConfig.containsKey(key) && strategyConfig.get(key) != null) {
      Object value = strategyConfig.get(key);
      if (value instanceof Number num) {
        return num.intValue();
      } else {
        throw new TargetStrategyException(key + " must be a number convertible to int");
      }
    }

    throw new TargetStrategyException("Type " + key + " is required");
  }


  static int[] calcTargetPosition(GameMapInterface map, Entity entity, String type,
      int tilesAhead) {
    int[] potentialTarget = potentialTargetPosition(
        entity.getEntityDirection(),
        (int) entity.getEntityPlacement().getX(),
        (int) entity.getEntityPlacement().getY(),
        tilesAhead
    );

    if (map.isValidPosition(potentialTarget[0], potentialTarget[1]) &&
        map.isNotBlocked(type, potentialTarget[0], potentialTarget[1])) {
      return potentialTarget;
    } else {
      return new int[]{
          (int) entity.getEntityPlacement().getX(),
          (int) entity.getEntityPlacement().getY()
      };
    }
  }

  private static int[] potentialTargetPosition(Direction dir, int x, int y, int tilesAhead) {

    if (dir == null) {
      return new int[]{x, y};
    }
    int targetX = x + dir.getDx() * tilesAhead;
    int targetY = y + dir.getDy() * tilesAhead;
    return new int[]{targetX, targetY};
  }
}
