/**
 * <p>
 * Provides various pathfinding strategies for navigating entities through the game map.
 * These strategies determine how entities move from one location to another based on different algorithms or heuristics.
 * </p>
 *
 * <p><b>Strategy Classes:</b></p>
 * <ul>
 *   <li>{@code BfsPathFindingStrategy} - Implements breadth-first search for shortest path navigation.</li>
 *   <li>{@code EuclideanPathFindingStrategy} - Selects paths based on minimizing Euclidean distance to the target.</li>
 *   <li>{@code RandomPathFindingStrategy} - Chooses random valid directions for movement.</li>
 *   <li>{@code PathFindingStrategyHelper} - Contains shared utilities to support pathfinding logic.</li>
 * </ul>
 */
package oogasalad.player.model.strategies.control.pathfinding;