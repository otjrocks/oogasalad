package oogasalad.engine.records.newconfig.model;

import oogasalad.engine.model.Condition;
import oogasalad.engine.model.EntityType;

/**
 * Represents an entity to spawn during gameplay based on specific conditions.
 *
 * @param entityType       the type of entity to spawn (e.g., "cherry", "redghost")
 * @param spawnCondition   condition under which the entity should appear
 * @param x                x-position on the grid
 * @param y                y-position on the grid
 * @param mode             integer representing the initial behavior mode
 * @param despawnCondition condition under which the entity should be removed
 * @author Owen Jennings, Will He
 */
public record SpawnEvent(EntityType entityType,
                         Condition spawnCondition,
                         double x,
                         double y,
                         String mode,
                         Condition despawnCondition) {

}
