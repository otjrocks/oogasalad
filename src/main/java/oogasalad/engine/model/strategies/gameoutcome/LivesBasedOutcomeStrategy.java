package oogasalad.engine.model.strategies.gameoutcome;

import oogasalad.engine.records.GameContextRecord;

/**
 * The {@code LivesBasedOutcome} class implements the {@link GameOutcomeStrategyInterface}
 * interface to determine whether the game has ended based on the player's remaining lives.
 * <p>
 * If the player's lives reach zero or below, the game is considered over.
 * </p>
 *
 * @author Austin Huang
 */
public class LivesBasedOutcomeStrategy implements GameOutcomeStrategyInterface {

  /**
   * Determines if the game has ended based on the player's remaining lives.
   *
   * @param gameContext contains gameScore and gameMap
   * @return {@code true} if the player's lives are zero or below, {@code false}
   *                           otherwise
   */
  @Override
  public boolean hasGameEnded(GameContextRecord gameContext) {
    return gameContext.gameState().getLives() <= 0;
  }

  /**
   * Returns the outcome of the game based on the player's remaining lives. If the game has ended
   * due to running out of lives, it returns a game-over message. Otherwise, it indicates the game
   * is still ongoing.
   *
   * @param gameContext contains gameScore and gameMap
   * @return a string representing the game outcome
   */
  @Override
  public String getGameOutcome(GameContextRecord gameContext) {
    return hasGameEnded(gameContext) ? "Game Over" : "Game ongoing";
  }
}
