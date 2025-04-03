package oogasalad.engine.model.strategies;

import oogasalad.engine.model.GameState;

/**
 * The {@code EntityBasedOutcomeStrategy} class implements the {@link GameOutcomeStrategy}
 * interface to determine whether the game has ended based on the entities present
 * in the game state, such as remaining pellets.
 * <p>
 * This strategy typically checks if all necessary entities (e.g., pellets) have been
 * consumed to declare a victory.
 * </p>
 *
 * @author Austin Huang
 */
public class EntityBasedOutcomeStrategy implements GameOutcomeStrategy {

  /**
   * Determines if the game has ended based on the current {@link GameState}.
   * Check conditions such as whether all pellets have been consumed.
   *
   * @param gameState the current state of the game
   * @return {@code true} if the game has ended, {@code false} otherwise
   */
  @Override
  public boolean hasGameEnded(GameState gameState) {
//    return gameState.getPellets() <= 0;
    return true;
  }

  /**
   * Returns the outcome of the game based on the current {@link GameState}.
   *
   * @param gameState the current state of the game
   * @return a string representing the game outcome
   */
  @Override
  public String getGameOutcome(GameState gameState) {
    return hasGameEnded(gameState) ? "Victory!" : "Game ongoing";
  }
}
