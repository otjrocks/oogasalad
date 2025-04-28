package oogasalad.player.model.api;

/**
 * <h1>Overview</h1>
 * This package provides factory classes that dynamically create various game strategies, entities,
 * and models based on configuration files. These factories use reflection to instantiate classes
 * with flexible constructor requirements.
 *
 * <h2>Factories</h2>
 *
 * <h3>CollisionStrategyFactory</h3>
 * Creates collision strategies based on a provided
 * {@link oogasalad.engine.records.config.model.CollisionEventInterface}. This factory uses
 * reflection and parameters parsed from the configuration file.
 *
 * <h3>ControlStrategyFactory</h3>
 * Dynamically creates instances of
 * {@link oogasalad.player.model.strategies.control.ControlStrategyInterface} based on the control
 * type specified in an {@link oogasalad.engine.config.EntityPlacement}. It uses reflection to
 * support constructors with varying arguments.
 *
 * <h3>EntityFactory</h3>
 * Creates game entities based on {@link oogasalad.engine.config.EntityPlacement} and
 * {@link oogasalad.engine.records.config.ConfigModelRecord} information.
 *
 * <h3>GameMap API</h3>
 * Provides a hybrid model for managing entity locations within the game, combining grid-based logic
 * with coordinate-based rendering for smooth animations. The GameMap is extendable and interacts
 * closely with entity classes.
 *
 * <h3>GameOutcomeFactory</h3>
 * Creates appropriate game outcome strategies.
 *
 * <h3>GameStateFactory</h3>
 * Loads a new game state from a configuration file.
 *
 * <h3>ModeChangeEventStrategyFactory</h3>
 * Creates strategies related to game mode changes.
 *
 * <h3>PathFindingStrategyFactory</h3>
 * Dynamically loads and creates instances of
 * {@link oogasalad.player.model.strategies.control.pathfinding.PathFindingStrategyInterface} using
 * reflection based on their names.
 *
 * <h3>SpawnEventStrategyFactory</h3>
 * Creates spawn event strategies based on the provided type string.
 *
 * <h3>TargetStrategyFactory</h3>
 * Creates various targeting strategies for game entities.
 *
 * @author Owen Jennings, Jessica Chen, Luke Fu
 * @version 1.0
 * @since 1.0
 */
