/**
 * Contains configuration records for handling collision events between game entities.
 *
 * <p>Each class in this package implements the {@link oogasalad.engine.records.config.model.CollisionEventInterface},
 * allowing it to be deserialized from JSON and mapped to a corresponding collision strategy in the game engine.</p>
 *
 * <p>Collision events define what should happen when two entities interact—whether an entity should
 * be removed, teleported, affect the game state, or trigger other behaviors.</p>
 *
 * <p>Examples of included collision event records:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.config.model.collisionevent.ConsumeCollisionEventRecord}
 *   – Removes the entity upon collision.</li>
 *   <li>{@link oogasalad.engine.records.config.model.collisionevent.ReturnToSpawnLocationCollisionEventRecord}
 *   – Teleports the entity back to its spawn point.</li>
 *   <li>{@link oogasalad.engine.records.config.model.collisionevent.ResetTimeElapsedCollisionEventRecord}
 *   – Resets the elapsed game time.</li>
 *   <li>{@link oogasalad.engine.records.config.model.collisionevent.RemoveAllEntitiesOfTypeCollisionEventRecord}
 *   – Removes all entities of a given type from the map.</li>
 * </ul>
 *
 * <p>This package is extensible and supports many more collision event types. These records serve
 * as the configuration layer for runtime collision strategy behavior.</p>
 */
package oogasalad.engine.records.config.model.collisionevent;
