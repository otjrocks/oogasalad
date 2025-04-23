package oogasalad.player.model.strategies.gameoutcome;

import oogasalad.engine.records.GameContextRecord;

/**
 * The {@code TimeBasedOutcomeStrategy} class implements the {@link GameOutcomeStrategyInterface}
 * interface to determine whether the game has ended based on time.
 * <p>
 * If the game's time elapsed is at least the specified time limit, the game is considered over.
 * </p>
 *
 * @author Austin Huang
 */
public class TimeBasedOutcomeStrategy implements GameOutcomeStrategyInterface {

  private final int timeLimit;

  /**
   * Constructs a {@code TimeBasedOutcomeStrategy} with a specified time limit.
   *
   * @param timeLimit the time limit of the game
   */
  public TimeBasedOutcomeStrategy(int timeLimit) {
    this.timeLimit = timeLimit;
  }

  /**
   * Determines if the game has ended based on the gameState's time elapsed.
   *
   * @param gameContext contains gameScore and gameMap
   * @return {@code true} if the elapsed time is at least the specified time limit, {@code false}
   *                           otherwise
   */
  @Override
  public boolean hasGameEnded(GameContextRecord gameContext) {
    return gameContext.gameState().getTimeElapsed() >= timeLimit;
  }

  /**
   * Returns the outcome of the game based on the time elapsed. If the game has ended due to
   * reaching the time limit, it returns a game over message. Otherwise, it indicates the game is
   * still ongoing.
   *
   * @param gameContext contains gameScore and gameMap
   * @return a string representing the game outcome
   */
  @Override
  public String getGameOutcome(GameContextRecord gameContext) {
    return hasGameEnded(gameContext) ? "Game Over" : "Game ongoing";
  }
}
