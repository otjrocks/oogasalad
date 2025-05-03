/**
 * Provides configuration records and interfaces for representing game win conditions.
 *
 * <p>This package defines various
 * {@link oogasalad.engine.records.config.model.wincondition.WinConditionInterface} implementations
 * that can be parsed from JSON game configuration files. Each win condition record can be converted
 * into a corresponding {@link oogasalad.player.model.strategies.gameoutcome.GameOutcomeStrategyInterface}
 * to drive in-game logic.</p>
 *
 * <p>Included win condition records:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.config.model.wincondition.SurviveForTimeConditionRecord}
 *   – Win by surviving for a specified duration of time.</li>
 *   <li>{@link oogasalad.engine.records.config.model.wincondition.EntityBasedConditionRecord}
 *   – Win by eliminating all entities of a certain type (e.g., all dots).</li>
 *   <li>{@link oogasalad.engine.records.config.model.wincondition.ScoreBasedConditionRecord}
 *   – Win by reaching a minimum score threshold.</li>
 * </ul>
 *
 * <p>The {@link oogasalad.engine.records.config.model.wincondition.WinConditionInterface} uses
 * Jackson annotations to support polymorphic JSON deserialization,
 * allowing flexible and extensible win condition specification via external files.</p>
 */
package oogasalad.engine.records.config.model.wincondition;
