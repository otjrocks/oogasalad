/**
 * Provides core game context records used across the engine for runtime logic, event handling, and game state tracking.
 *
 * <p>This package defines fundamental records that encapsulate the state of gameplay during critical moments,
 * such as collisions or frame updates. These records are used throughout the engine to share consistent,
 * structured data between systems.</p>
 *
 * <p>Included records:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.GameContextRecord}
 *   – Represents the full context of the current game state, including map, state, and input.</li>
 *   <li>{@link oogasalad.engine.records.CollisionContextRecord}
 *   – Encapsulates all relevant data for handling a collision between two entities.</li>
 * </ul>
 *
 * <p>Subpackages:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.model} – Defines lower-level data structures like map
 *   metadata, entity types, and mode change events used to construct game state and configuration.</li>
 *   <li>{@link oogasalad.engine.records.config} – Contains higher-level game configuration records
 *   parsed from JSON, including game settings, levels, entities, and collision definitions.</li>
 * </ul>
 *
 * <p>Together, this package and its subpackages form the backbone of the game engine’s data
 * representation layer, supporting both configuration and runtime execution.</p>
 */
package oogasalad.engine.records;
