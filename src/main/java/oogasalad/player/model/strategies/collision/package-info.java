/**
 * Provides a set of collision strategies used to define specific outcomes when entities in the game interact.
 *
 * <p>This package is part of the Strategy design pattern that allows game designers to customize entity behaviors
 * without modifying existing code. Each strategy implements the
 * {@link oogasalad.player.model.strategies.collision.CollisionStrategyInterface} and defines a unique
 * response to a collision event. These responses can include teleportation, halting movement, resetting game state,
 * or removing entities from the game map.</p>
 *
 * <p>Example strategies include:</p>
 * <ul>
 *   <li>{@link oogasalad.player.model.strategies.collision.StopStrategy}
 *   – Stops the movement of an entity upon collision.</li>
 *   <li>{@link oogasalad.player.model.strategies.collision.ReturnToSpawnLocationStrategy}
 *   – Sends the entity back to its spawn location.</li>
 *   <li>{@link oogasalad.player.model.strategies.collision.RemoveAllEntitiesOfTypeStrategy}
 *   – Clears all entities of a specified type from the map.</li>
 * </ul>
 *
 * <p>This extensible architecture allows the game engine to remain open for extension and closed for modification,
 * promoting clean and modular design.</p>
 */
package oogasalad.player.model.strategies.collision;