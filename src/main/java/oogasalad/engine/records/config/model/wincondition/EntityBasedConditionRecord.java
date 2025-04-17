package oogasalad.engine.records.config.model.wincondition;

import oogasalad.engine.model.strategies.gameoutcome.EntityBasedOutcomeStrategy;
import oogasalad.engine.model.strategies.gameoutcome.GameOutcomeStrategyInterface;

/**
 * A record that encapsulates information about the entity-based win condition.
 *
 * @param entityType The type of entity to track (e.g., "dot")
 */
public record EntityBasedConditionRecord(String entityType) implements WinConditionInterface {

  /**
   * Converts winCondition to EntityBasedOutcome strategy.
   *
   * @return EntityBasedOutcome strategy
   */
  @Override
  public GameOutcomeStrategyInterface toStrategy() {
    return new EntityBasedOutcomeStrategy(entityType);
  }
}