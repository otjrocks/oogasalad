/**
 * Provides strategies for determining target positions used in pathfinding and AI decision-making.
 *
 * <p>All classes in this package implement the {@link oogasalad.player.model.strategies.control.targetcalculation.TargetStrategyInterface}, which defines a common
 * method for returning a target position as an (x, y) coordinate. These strategies are useful in scenarios
 * such as chasing, fleeing, or coordinating movement among entities.</p>
 *
 * <p>Included strategies:</p>
 * <ul>
 *   <li>{@link oogasalad.player.model.strategies.control.targetcalculation.TargetLocationStrategy}
 *   – Uses a fixed (x, y) coordinate from configuration.</li>
 *   <li>{@link oogasalad.player.model.strategies.control.targetcalculation.TargetEntityStrategy}
 *   – Targets the current position of an entity of a given type.</li>
 *   <li>{@link oogasalad.player.model.strategies.control.targetcalculation.TargetAheadOfEntityStrategy}
 *   – Predicts a position a set number of tiles ahead of an entity.</li>
 *   <li>{@link oogasalad.player.model.strategies.control.targetcalculation.TargetEntityWithTrapStrategy}
 *   – Combines a lead-ahead calculation with a teammate-based trap logic.</li>
 * </ul>
 *
 * <p>Utilities:</p>
 * <ul>
 *   <li>{@link oogasalad.player.model.strategies.control.targetcalculation.TargetStrategyHelperMethods} – Provides shared logic for entity searching, configuration validation,
 *       and directional position calculation.</li>
 * </ul>
 *
 * <p>This package supports composable, configurable, and extensible targeting logic to drive AI and scripted behavior
 * in complex multi-entity environments.</p>
 */
package oogasalad.player.model.strategies.control.targetcalculation;
