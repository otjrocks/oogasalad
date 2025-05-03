/**
 * Contains configuration records for specifying target calculation strategies in entity control logic.
 *
 * <p>All classes in this package implement the
 * {@link oogasalad.engine.records.config.model.controlConfig.targetStrategy.TargetCalculationConfigInterface},
 * which is used to deserialize JSON-formatted control strategy configurations. These records define
 * how an entity determines its target position during gameplay (e.g., for pathfinding or AI
 * behavior).</p>
 *
 * <p>Included configuration records:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.targetStrategy.TargetEntityConfigRecord}
 *   – Targets the position of the first entity of a specified type.</li>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.targetStrategy.TargetAheadOfEntityConfigRecord}
 *   – Targets a position a fixed number of tiles ahead of a target entity.</li>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.targetStrategy.TargetEntityWithTrapConfigRecord}
 *   – Targets a reflected position using a teammate and lead-ahead logic.</li>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.targetStrategy.TargetLocationConfigRecord}
 *   – Targets a fixed (x, y) coordinate specified directly in the config.</li>
 * </ul>
 *
 * <p>This package supports polymorphic deserialization using Jackson annotations, enabling flexible
 * control behavior configuration via external files.</p>
 */
package oogasalad.engine.records.config.model.controlConfig.targetStrategy;
