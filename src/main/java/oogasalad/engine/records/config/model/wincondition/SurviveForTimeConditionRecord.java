package oogasalad.engine.records.config.model.wincondition;

import java.util.Optional;
import oogasalad.player.model.strategies.gameoutcome.GameOutcomeStrategyInterface;
import oogasalad.player.model.strategies.gameoutcome.ScoreBasedOutcomeStrategy;

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

  @Override
  public String getConditionType() {
    return "SurviveForTime";
  }

  @Override
  public Optional<String> getConditionValue() {
    return Optional.of(String.valueOf(amount));
  }
}
