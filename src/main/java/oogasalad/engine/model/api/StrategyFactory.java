package oogasalad.engine.model.api;

import oogasalad.engine.LoggingManager;
import oogasalad.engine.model.strategies.collision.CollisionStrategy;
import oogasalad.engine.model.strategies.collision.ConsumeStrategy;
import oogasalad.engine.model.strategies.collision.StopStrategy;
import oogasalad.engine.model.strategies.collision.UpdateLivesStrategy;
import oogasalad.engine.model.strategies.collision.UpdateScoreStrategy;

/**
 * A factory design pattern to create strategies based a provided name and arguments.
 *
 * @author Owen Jennings.
 */
public class StrategyFactory {

  /**
   * Create a collision strategy from the provided name. If the provided collision strategy name
   * does not exist, returns a consume strategy by default.
   *
   * @param strategyString The name of the strategy you are requesting.
   * @return The collision strategy requested.
   */
  public static CollisionStrategy createCollisionStrategy(String strategyString) {
    int parameter = 0;
    if (strategyString.contains("(")) {
      try {
        parameter = Integer.parseInt(
            strategyString.substring(strategyString.indexOf("(") + 1, strategyString.indexOf(")")));
      } catch (NumberFormatException ex) {
        LoggingManager.LOGGER.warn("Could not parse parameter for: {}", strategyString, ex);
        throw ex;
      }
      strategyString = strategyString.substring(0, strategyString.indexOf("("));
    }
    return switch (strategyString) {
      case "Stop" -> new StopStrategy();
      case "UpdateLives" -> new UpdateLivesStrategy(parameter);
      case "UpdateScore" -> new UpdateScoreStrategy(parameter);
      default -> new ConsumeStrategy();
    };
  }
}