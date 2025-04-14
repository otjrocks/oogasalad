package oogasalad.engine.records.newconfig.model.wincondition;

/**
 * A record that encapsulates information about the entity-based win condition.
 *
 * @param entityType The type of entity to track (e.g., "dot")
 * @param amount     The number of entities that must be removed to win
 */
public record EntityBasedCondition(String entityType, int amount) implements WinCondition {
}