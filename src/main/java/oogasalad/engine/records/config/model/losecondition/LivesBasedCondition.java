package oogasalad.engine.records.config.model.losecondition;

import oogasalad.engine.model.strategies.gameoutcome.GameOutcomeStrategy;
import oogasalad.engine.model.strategies.gameoutcome.LivesBasedOutcome;
import oogasalad.engine.records.GameContextRecord;

/**
 * A record that encapsulates information about the entity-based win condition.
 */
public record LivesBasedCondition() implements LoseCondition {

  /**
   * Converts loseCondition to LivesBasedOutcome strategy.
   *
   * @return LivesBasedOutcome strategy
   */
  @Override
  public GameOutcomeStrategy toStrategy() {
    return new LivesBasedOutcome();
  }
}
