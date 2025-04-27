package oogasalad.engine.records.config.model.losecondition;

import java.util.Optional;
import oogasalad.player.model.strategies.gameoutcome.GameOutcomeStrategyInterface;
import oogasalad.player.model.strategies.gameoutcome.TimeBasedOutcomeStrategy;

/**
 * A record that encapsulates information about the time based lose condition.
 *
 * @author William He
 */
public record TimeBasedConditionRecord(int amount) implements LoseConditionInterface {

  @Override
  public GameOutcomeStrategyInterface toStrategy() {
    return new TimeBasedOutcomeStrategy(amount);
  }

  @Override
  public String getConditionType() {
    return "TimeBased";
  }

  @Override
  public Optional<String> getConditionValue() {
    return Optional.of(String.valueOf(amount));
  }
}
