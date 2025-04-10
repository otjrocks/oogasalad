package oogasalad.engine.model.api;

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
   * @param name The name of the strategy you are requesting.
   * @param args The required arguments of the initialization of the strategy.
   * @return The collision strategy requested.
   */
  public static CollisionStrategy createCollisionStrategy(String name, Object... args) {
    return switch (name) {
      case "Stop" -> new StopStrategy();
      case "UpdateLives" -> new UpdateLivesStrategy((Integer) args[0]);
      case "UpdateScore" -> new UpdateScoreStrategy((Integer) args[0]);
      default -> new ConsumeStrategy();
    };
  }
}