package oogasalad.engine.model.strategies.gameoutcome;

import oogasalad.engine.model.GameState;

/**
 * The {@code ScoreBasedOutcomeStrategy} class implements the {@link GameOutcomeStrategy}
 * interface to determine whether the game has ended based on the player's score.
 * <p>
 * If the player's score reaches or exceeds a specified winning score, the game is considered won.
 * </p>
 *
 * @author Austin Huang
 */
public class ScoreBasedOutcomeStrategy implements GameOutcomeStrategy{
  private final int winningScore;

  /**
   * Constructs a {@code ScoreBasedOutcomeStrategy} with a specified winning score.
   *
   * @param winningScore the score required to win the game
   */
  public ScoreBasedOutcomeStrategy(int winningScore) {
    this.winningScore = winningScore;
  }

  /**
   * Determines if the game has ended based on the player's score.
   *
   * @param gameState the current state of the game
   * @return {@code true} if the player's score is greater than or equal to the winning score, {@code false} otherwise
   */
  @Override
  public boolean hasGameEnded(GameState gameState) {
    return gameState.getScore() >= winningScore;
  }

  /**
   * Returns the outcome of the game based on the player's score.
   * If the game has ended due to reaching the winning score, it returns a victory message.
   * Otherwise, it indicates the game is still ongoing.
   *
   * @param gameState the current state of the game
   * @return a string representing the game outcome
   */
  @Override
  public String getGameOutcome(GameState gameState) {
    return hasGameEnded(gameState) ? "Victory!" : "Game ongoing";
  }
}
