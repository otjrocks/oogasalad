package oogasalad.engine.records.config.model.losecondition;

import oogasalad.player.model.strategies.gameoutcome.GameOutcomeStrategyInterface;
import oogasalad.player.model.strategies.gameoutcome.LivesBasedOutcomeStrategy;

/**
 * A record that encapsulates information about the entity-based win condition.
 */
public record LivesBasedConditionRecord() implements LoseConditionInterface {

  /**
   * Converts loseCondition to LivesBasedOutcomeStrategy.
   *
   * @return LivesBasedOutcomeStrategy
   */
  @Override
  public GameOutcomeStrategyInterface toStrategy() {
    return new LivesBasedOutcomeStrategy();
  }
}
