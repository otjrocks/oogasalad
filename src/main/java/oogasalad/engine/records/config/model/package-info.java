/**
 * Contains core configuration records used to define the structure, rules, and metadata of a game.
 *
 * <p>This package provides serializable records that represent parsed game settings, levels,
 * entity properties, collision events, save data, and more. These records are typically loaded
 * from JSON configuration files and used to construct the initial game state and behavior.</p>
 *
 * <p>Subpackages:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.config.model.wincondition}
 *   – Defines win condition records,
 *       such as score thresholds or entity elimination requirements.</li>
 *   <li>{@link oogasalad.engine.records.config.model.losecondition}
 *   – Defines lose condition records,
 *       such as time expiration or running out of lives.</li>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig}
 *   – Defines control strategy configuration,
 *       including both high-level control types and sub-configurations like target strategies.</li>
 *   <li>{@link oogasalad.engine.records.config.model.collisionevent}
 *   – Contains records for specifying what should
 *       happen when entities collide (e.g., consume, teleport, remove).</li>
 * </ul>
 *
 * <p>Representative records in this package:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.config.model.SettingsRecord}
 *   – Game-level configuration including speed, lives, and win/lose logic.</li>
 *   <li>{@link oogasalad.engine.records.config.model.MetadataRecord}
 *   – Describes the game's title, author, and description.</li>
 *   <li>{@link oogasalad.engine.records.config.model.ParsedLevelRecord}
 *   – Stores all level-specific information such as map layout and events.</li>
 *   <li>{@link oogasalad.engine.records.config.model.SpawnEventRecord}
 *   – Defines how and when entities should appear or disappear during gameplay.</li>
 *   <li>{@link oogasalad.engine.records.config.model.SaveConfigRecord}
 *   – Used for persisting game state to disk.</li>
 * </ul>
 *
 * <p>This package and its subpackages serve as the structured bridge between raw configuration files
 * and in-game runtime logic.</p>
 */
package oogasalad.engine.records.config.model;
