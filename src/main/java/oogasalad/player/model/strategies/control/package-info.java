/**
 * Provides control strategies for managing entity behavior, including player input, AI movement,
 * and navigation toward dynamic or static targets.
 *
 * <p>This package contains the core logic that drives how game entities make movement decisions or respond to input.
 * Strategies implement the {@link oogasalad.player.model.strategies.control.ControlStrategyInterface}, allowing
 * flexible, interchangeable control logic.</p>
 *
 * <p>Example included control strategies:</p>
 * <ul>
 *   <li>{@link oogasalad.player.model.strategies.control.KeyboardControlStrategy}
 *   – Allows entity movement via keyboard input.</li>
 *   <li>{@link oogasalad.player.model.strategies.control.NoneControlStrategy}
 *   – A no-op strategy for stationary or passive entities.</li>
 *   <li>{@link oogasalad.player.model.strategies.control.TargetControlStrategy}
 *   – Moves entities toward calculated targets using configurable pathfinding and targeting.</li>
 * </ul>
 *
 * <p>Nested subpackages:</p>
 * <ul>
 *   <li><b>pathfinding</b> – Defines algorithms like BFS or Euclidean for navigating game maps
 *       ({@link oogasalad.player.model.strategies.control.pathfinding}).</li>
 *   <li><b>targetcalculation</b> – Determines target positions using heuristics or entity positions
 *       ({@link oogasalad.player.model.strategies.control.targetcalculation}).</li>
 * </ul>
 *
 * <p>Utilities:</p>
 * <ul>
 *   <li>{@link oogasalad.player.model.strategies.control.ControlManager}
 *   – Reflection-based utility for retrieving available strategy types and their configuration requirements.</li>
 * </ul>
 *
 * <p>This package and its subpackages are designed to be extensible and configuration-driven,
 * promoting modular AI and user control logic within the game engine.</p>
 */
package oogasalad.player.model.strategies.control;
