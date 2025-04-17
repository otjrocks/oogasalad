package oogasalad.engine.records.config.model.losecondition;

import oogasalad.engine.model.strategies.gameoutcome.GameOutcomeStrategy;
import oogasalad.engine.model.strategies.gameoutcome.LivesBasedOutcomeStrategy;

/**
 * A record that encapsulates information about the entity-based win condition.
 */
public record LivesBasedCondition() implements LoseCondition {

  /**
   * Converts loseCondition to LivesBasedOutcomeStrategy.
   *
   * @return LivesBasedOutcomeStrategy
   */
  @Override
  public GameOutcomeStrategy toStrategy() {
    return new LivesBasedOutcomeStrategy();
  }
}
