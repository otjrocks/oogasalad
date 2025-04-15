package oogasalad.player.model.control;

import java.util.Map;
import oogasalad.player.model.exceptions.TargetStrategyException;

/**
 * The {@code ControlStrategyHelperMethods} class provides utility methods to assist
 * in validating and retrieving configuration values for control strategies.
 *
 * <p>This class includes methods to ensure the presence of required keys in a strategy
 * configuration map and to retrieve their corresponding values as strings. If a required
 * key is missing or its value is null, an exception is thrown to indicate the issue.
 */
public class ControlStrategyHelperMethods {

  /**
   * Validates the presence of a specified key in the given strategy configuration map
   * and retrieves its corresponding value as a string. If the key is missing or its value
   * is null, a {@link TargetStrategyException} is thrown.
   *
   * @param strategyConfig the map containing strategy configuration key-value pairs
   * @param key the key to validate and retrieve the value for
   * @return the value associated with the specified key as a string
   * @throws TargetStrategyException if the key is missing or its value is null
   */
  static String validateAndGetPathFindingStrategy(Map<String, Object> strategyConfig, String key) {
    if (!strategyConfig.containsKey(key)
        || strategyConfig.get(key) == null) {
      throw new TargetStrategyException("Type " + key + " is required");
    }

    return strategyConfig.get(key).toString();
  }

}
