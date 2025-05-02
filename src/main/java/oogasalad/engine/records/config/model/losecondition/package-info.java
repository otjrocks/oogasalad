/**
 * Provides configuration records and interfaces for representing game lose conditions.
 *
 * <p>This package defines implementations of the
 * {@link oogasalad.engine.records.config.model.losecondition.LoseConditionInterface}, which can be
 * parsed from game configuration files and converted into corresponding
 * {@link oogasalad.player.model.strategies.gameoutcome.GameOutcomeStrategyInterface} strategies.</p>
 *
 * <p>Included loss condition records:</p>
 * <ul>
 *   <li>{@link oogasalad.engine.records.config.model.losecondition.LivesBasedConditionRecord}
 *   – Triggers a loss when the player's lives reach zero.</li>
 *   <li>{@link oogasalad.engine.records.config.model.losecondition.TimeBasedConditionRecord}
 *   – Triggers a loss when the game time reaches a specified limit.</li>
 * </ul>
 *
 * <p>The interface uses Jackson annotations to support polymorphic deserialization from JSON,
 * enabling flexible condition specification in configuration files.</p>
 */
package oogasalad.engine.records.config.model.losecondition;
