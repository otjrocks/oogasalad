package oogasalad.engine.records.config.model.wincondition;

import oogasalad.engine.model.strategies.gameoutcome.GameOutcomeStrategyInterface;
import oogasalad.engine.model.strategies.gameoutcome.ScoreBasedOutcomeStrategy;

/**
 * A record that encapsulates information about the survive for time win condition.
 *
 * @param amount Amount of time the player needs to survive.
 */
public record SurviveForTimeConditionRecord(int amount) implements WinConditionInterface {

  /**
   * Converts winCondition to ScoreBasedOutcomeStrategy.
   *
   * @return ScoreBasedOutcomeStrategy
   */
  @Override
  public GameOutcomeStrategyInterface toStrategy() {
    return new ScoreBasedOutcomeStrategy(amount);
  }
}
