package oogasalad.engine.records.config.model.wincondition;

import oogasalad.engine.model.strategies.gameoutcome.GameOutcomeStrategy;
import oogasalad.engine.model.strategies.gameoutcome.ScoreBasedOutcomeStrategy;

/**
 * A record that encapsulates information about the survive for time win condition.
 *
 * @param amount Amount of time the player needs to survive.
 */
public record SurviveForTimeCondition(int amount) implements WinCondition {

  /**
   * Converts winCondition to ScoreBasedOutcomeStrategy.
   *
   * @return ScoreBasedOutcomeStrategy
   */
  @Override
  public GameOutcomeStrategy toStrategy() {
    return new ScoreBasedOutcomeStrategy(amount);
  }
}
