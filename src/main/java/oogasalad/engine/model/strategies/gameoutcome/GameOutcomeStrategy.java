package oogasalad.engine.model.strategies.gameoutcome;

import oogasalad.engine.records.GameContext;

/**
 * A strategy interface that determines if the game has ended and the outcome.
 *
 * @author Austin Huang
 */
public interface GameOutcomeStrategy {

  /**
   * Determines whether the game has ended based on the current game state.
   *
   * @param gameContext contains gameScore and gameMap
   * @return true if the game has ended, false otherwise
   */
  boolean hasGameEnded(GameContext gameContext);

  /**
   * Gets the outcome of the game if it has ended.
   *
   * @param gameContext contains gameScore and gameMap
   * @return a string describing the game outcome
   */
  String getGameOutcome(GameContext gameContext);
}
