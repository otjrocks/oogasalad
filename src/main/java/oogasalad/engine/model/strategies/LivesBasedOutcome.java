package oogasalad.engine.model.strategies;

import oogasalad.engine.model.GameState;

/**
 * The {@code LivesBasedOutcome} class implements the {@link GameOutcomeStrategy}
 * interface to determine whether the game has ended based on the player's remaining lives.
 * <p>
 * If the player's lives reach zero or below, the game is considered over.
 * </p>
 *
 * @author [Your Name]
 */
public class LivesBasedOutcome implements GameOutcomeStrategy {

  /**
   * Determines if the game has ended based on the player's remaining lives.
   *
   * @param gameState the current state of the game
   * @return {@code true} if the player's lives are zero or below, {@code false} otherwise
   */
  @Override
  public boolean hasGameEnded(GameState gameState) {
    return gameState.getLives() <= 0;
  }

  /**
   * Returns the outcome of the game based on the player's remaining lives.
   * If the game has ended due to running out of lives, it returns a game-over message.
   * Otherwise, it indicates the game is still ongoing.
   *
   * @param gameState the current state of the game
   * @return a string representing the game outcome
   */
  @Override
  public String getGameOutcome(GameState gameState) {
    return hasGameEnded(gameState) ? "Game Over! You ran out of lives!" : "Game ongoing";
  }
}
