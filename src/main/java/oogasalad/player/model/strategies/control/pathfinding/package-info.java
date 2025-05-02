/**
 * Contains implementations of pathfinding strategies for navigating game maps.
 *
 * <p>This package defines various algorithms that determine how an entity moves from its current
 * position to a target position. All strategies implement the {@link oogasalad.player.model.strategies.control.pathfinding.PathFindingStrategyInterface},
 * enabling pluggable AI navigation logic through the Strategy design pattern.</p>
 *
 * <p>Included strategies:</p>
 * <ul>
 *   <li>{@link oogasalad.player.model.strategies.control.pathfinding.BfsPathFindingStrategy}
 *   – Finds the shortest path using the Breadth-First Search algorithm.</li>
 *   <li>{@link oogasalad.player.model.strategies.control.pathfinding.EuclideanPathFindingStrategy}
 *   – Moves in the direction that minimizes Euclidean distance to the target.</li>
 *   <li>{@link oogasalad.player.model.strategies.control.pathfinding.RandomPathFindingStrategy}
 *   – Chooses a random valid direction, useful for non-deterministic movement.</li>
 * </ul>
 *
 * <p>Utilities:</p>
 * <ul>
 *   <li>{@link oogasalad.player.model.strategies.control.pathfinding.PathFindingStrategyHelperMethods}
 *   – Provides shared helper methods for computing valid neighbors and directional preferences.</li>
 * </ul>
 *
 * <p>This package enables both deterministic and probabilistic movement behaviors, supporting
 * varied AI designs and gameplay dynamics.</p>
 */
package oogasalad.player.model.strategies.control.pathfinding;
