/**
 * <p>
 * Provides strategies for determining when entities should be spawned during gameplay based on different criteria.
 * </p>
 *
 * <p><b>Available Spawn Event Strategies:</b></p>
 * <ul>
 *   <li>{@code AlwaysSpawnEventStrategy} - Spawns entities continuously or at fixed intervals regardless of game state.</li>
 *   <li>{@code ScoreBasedSpawnEventStrategy} - Triggers spawns when a certain score threshold is reached.</li>
 *   <li>{@code TimeElapsedSpawnEventStrategy} - Spawns entities after a specified amount of time has passed.</li>
 * </ul>
 */
package oogasalad.player.model.strategies;