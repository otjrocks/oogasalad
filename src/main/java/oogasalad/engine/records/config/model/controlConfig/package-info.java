/**
 * Provides configuration records and interfaces for specifying control strategies in entity behavior.
 *
 * <p>This package defines implementations of
 * {@link oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface}, which are
 * used to deserialize control strategy configurations from JSON. These records encapsulate
 * parameters required by different control strategies (e.g., keyboard, AI pathfinding, conditional
 * control).</p>
 *
 * <p>Included control configuration records:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.NoneControlConfigRecord}
 *   – Represents an entity with no control behavior.</li>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.KeyboardControlConfigRecord}
 *   – Specifies control via player keyboard input.</li>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.JumpControlConfigRecord}
 *   – Adds jump and gravity-based movement logic.</li>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.TargetControlConfigRecord}
 *   – Enables AI movement toward a calculated target using a pathfinding strategy.</li>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.ConditionalControlConfigRecord}
 *   – Switches pathfinding logic based on proximity to a target.</li>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.ConstantDirectionControlConfigRecord}
 *   – Moves the entity in a constant direction.</li>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.FollowEntityControlConfigRecord}
 *   – Follows another entity on the map.</li>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.PinballControlConfigRecord}
 *   – Adds pinball-style charging and launching movement behavior.</li>
 * </ul>
 *
 * <p>Subpackage:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.config.model.controlConfig.targetStrategy} – Defines configuration records
 *       for specifying how AI-controlled entities determine target positions.</li>
 * </ul>
 *
 * <p>This package supports flexible, data-driven control specification and integrates with the controller
 * and strategy subsystems of the game engine.</p>
 */
package oogasalad.engine.records.config.model.controlConfig;
