/**
 * Defines strategies for triggering in-game mode change events for entities based on game context.
 *
 * <p>Each strategy in this package implements the
 * {@link oogasalad.player.model.strategies.modechangeevent.ModeChangeEventStrategyInterface}, which determines
 * whether a mode change (e.g., switching entity behavior, appearance, or type) should occur at a given moment.</p>
 *
 * <p>These strategies are used to drive dynamic entity behaviors and transitions, such as timed transformations
 * or conditional power-ups.</p>
 *
 * <p>Included strategy:</p>
 * <ul>
 *   <li>{@link oogasalad.player.model.strategies.modechangeevent.TimeElapsedModeChangeEventStrategy}
 *   – Triggers a mode change when a specified amount of game time has passed.</li>
 * </ul>
 *
 * <p>These strategies support event-driven game mechanics and are typically configured through mode change event records
 * provided in the game's data model.</p>
 */
package oogasalad.player.model.strategies.modechangeevent;
