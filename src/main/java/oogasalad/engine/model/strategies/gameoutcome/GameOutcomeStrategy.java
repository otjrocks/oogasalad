package oogasalad.engine.model.strategies.gameoutcome;

import oogasalad.engine.model.GameState;

/**
 * A strategy interface that determines if the game has ended and the outcome.
 *
 * @author Austin Huang
 */
public interface GameOutcomeStrategy {

  /**
   * Determines whether the game has ended based on the current game state.
   *
   * @param gameState the current state of the game
   * @return true if the game has ended, false otherwise
   */
  boolean hasGameEnded(GameState gameState);

  /**
   * Gets the outcome of the game if it has ended.
   *
   * @param gameState the current state of the game
   * @return a string describing the game outcome
   */
  String getGameOutcome(GameState gameState);
}
