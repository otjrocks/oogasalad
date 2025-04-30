/**
 * <h1>Overview</h1>
 * This package defines runtime exceptions used throughout the player model module of the game.
 * These custom exceptions provide informative error handling for invalid configurations and runtime
 * failures related to control, pathfinding, targeting, and file saving strategies.
 *
 * <h2>Exceptions</h2>
 *
 * <h3>ControlStrategyException</h3>
 * Thrown when an invalid control strategy is specified or fails during instantiation or execution
 * in the {@link oogasalad.player.model.api.ControlStrategyFactory}.
 *
 * <h3>PathFindingStrategyException</h3>
 * Raised when a pathfinding strategy is incorrectly specified or fails to load in the
 * {@link oogasalad.player.model.api.PathFindingStrategyFactory}.
 *
 * <h3>TargetStrategyException</h3>
 * Indicates a failure in initializing or using a targeting strategy, typically within the
 * {@link oogasalad.player.model.api.TargetStrategyFactory}
 *
 * <h3>SaveFileException</h3>
 * Represents issues encountered during game state saving operations, such as writing to a save
 * configuration file.
 *
 * <p>All exceptions extend {@link java.lang.RuntimeException} and may include support for chaining
 * causes to preserve full error traces during debugging and logging.</p>
 *
 * @version 1.0
 * @since 1.0
 * @author Jessica Chen
 */
package oogasalad.player.model.exceptions;