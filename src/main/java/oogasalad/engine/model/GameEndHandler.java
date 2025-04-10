package oogasalad.engine.model;

/**
 * A functional interface for handling game end events, such as a player winning,
 * losing, or reaching a victory condition.
 *
 * <p>This interface allows components such as controllers or views to register a callback
 * that will be triggered when the game concludes. It promotes decoupled design by
 * allowing the game logic to notify interested components without hardcoding behavior.</p>
 *
 * @author Austin Huang
 */
@FunctionalInterface
public interface GameEndHandler {

  /**
   * Called when the game ends. The implementing method should define
   * the behavior that occurs after the end condition is met.
   *
   * @param status specifies reason for stopping game.
   */
  void onGameEnd(GameEndStatus status);
}
