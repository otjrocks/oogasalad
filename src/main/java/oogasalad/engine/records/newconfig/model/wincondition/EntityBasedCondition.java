package oogasalad.engine.records.newconfig.model.wincondition;

/**
 * A record that encapsulates information about the entity-based win condition.
 *
 * @param entityType The type of entity to track (e.g., "dot")
 */
public record EntityBasedCondition(String entityType) implements WinCondition {
}