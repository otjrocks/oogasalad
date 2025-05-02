/**
 * Defines the core configuration structure for game setup, including metadata, levels, collisions,
 * entity modes, and visual assets.
 *
 * <p>This package contains records that are parsed from external JSON configuration files and used
 * to construct a {@link oogasalad.engine.records.config.GameConfigRecord} or
 * {@link oogasalad.engine.records.config.ConfigModelRecord} — the central data models
 * representing the structure and rules of a game.</p>
 *
 * <p>Key configuration components:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.config.GameConfigRecord}
 *   – Contains metadata, settings, levels, and collision definitions.</li>
 *   <li>{@link oogasalad.engine.records.config.ConfigModelRecord}
 *   – Enriched runtime-ready config model including parsed entity types,
 *       level data, and win/lose conditions.</li>
 *   <li>{@link oogasalad.engine.records.config.EntityConfigRecord}
 *   – Maps an entity type to its properties and available behavior modes.</li>
 *   <li>{@link oogasalad.engine.records.config.ModeConfigRecord}
 *   – Defines behavior and appearance for a single entity mode.</li>
 *   <li>{@link oogasalad.engine.records.config.ImageConfigRecord}
 *   – Represents sprite sheet information for animated entities.</li>
 *   <li>{@link oogasalad.engine.records.config.CollisionConfigRecord}
 *   – Encapsulates collision rules and corresponding events between entities.</li>
 * </ul>
 *
 * <p>Subpackage:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.config.model} – Contains supporting model-level records
 *   such as level structure, spawn conditions, game settings, and win/lose logic. This subpackage
 *   also includes:
 *     <ul>
 *       <li>{@code wincondition} – Defines win condition configuration records</li>
 *       <li>{@code losecondition} – Defines lose condition configuration records</li>
 *       <li>{@code controlConfig} – Configurations for entity control behavior, including {@code targetStrategy}</li>
 *       <li>{@code collisionevent} – Describes behavior that occurs on entity collisions</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p>Together, these records support a declarative, data-driven design that allows games to be built
 * and customized through configuration alone, without changing core engine logic.</p>
 */
package oogasalad.engine.records.config;
