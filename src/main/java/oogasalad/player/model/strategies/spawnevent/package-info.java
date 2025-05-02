/**
 * Provides strategies for determining whether game entities should be spawned or despawned during gameplay.
 *
 * <p>All classes in this package implement the
 * {@link oogasalad.player.model.strategies.spawnevent.SpawnEventStrategyInterface}, enabling a flexible,
 * configurable system for dynamic entity lifecycle management. Strategies evaluate the current game context
 * and user-defined parameters to decide whether a spawn or despawn event should occur.</p>
 *
 * <p>Included strategies:</p>
 * <ul>
 *   <li>{@link oogasalad.player.model.strategies.spawnevent.TimeElapsedSpawnEventStrategy}
 *   – Spawns or despawns entities based on elapsed game time.</li>
 *   <li>{@link oogasalad.player.model.strategies.spawnevent.ScoreBasedSpawnEventStrategy}
 *   – Spawns or despawns entities based on the player's score.</li>
 *   <li>{@link oogasalad.player.model.strategies.spawnevent.AlwaysSpawnEventStrategy}
 *   – Disables spawning and despawning by always returning false.</li>
 * </ul>
 *
 * <p>These strategies support modular, event-driven gameplay by controlling entity availability through
 * condition-based logic. They are often used in coordination with {@code SpawnEventRecord} configurations.</p>
 */
package oogasalad.player.model.strategies.spawnevent;
