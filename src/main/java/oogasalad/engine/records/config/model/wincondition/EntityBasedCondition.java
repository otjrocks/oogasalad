package oogasalad.engine.records.config.model.wincondition;

import oogasalad.engine.model.strategies.gameoutcome.EntityBasedOutcomeStrategy;
import oogasalad.engine.model.strategies.gameoutcome.GameOutcomeStrategy;

/**
 * A record that encapsulates information about the entity-based win condition.
 *
 * @param entityType The type of entity to track (e.g., "dot")
 */
public record EntityBasedCondition(String entityType) implements WinCondition {

  /**
   * Converts winCondition to EntityBasedOutcome strategy.
   *
   * @return EntityBasedOutcome strategy
   */
  @Override
  public GameOutcomeStrategy toStrategy() {
    return new EntityBasedOutcomeStrategy(entityType);
  }
}