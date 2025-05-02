/**
 * <p>
 * Provides strategies for calculating target locations or entities during gameplay.
 * These strategies guide entity behavior by determining where or what they should target.
 * </p>
 *
 * <p><b>Strategy Classes:</b></p>
 * <ul>
 *   <li>{@code TargetAheadOfEntityStrategy} - Targets a location in front of a moving entity.</li>
 *   <li>{@code TargetEntityStrategy} - Targets a specific entity in the game.</li>
 *   <li>{@code TargetEntityWithTrapStrategy} - Targets an entity while factoring in traps or indirect approaches.</li>
 *   <li>{@code TargetLocationStrategy} - Targets a static location on the map.</li>
 *   <li>{@code TargetStrategyHelperMethod} - Provides utility methods for assisting targeting strategies.</li>
 * </ul>
 */
package oogasalad.player.model.strategies.control.targetcalculation;