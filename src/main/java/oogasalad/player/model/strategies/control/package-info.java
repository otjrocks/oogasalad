/**
 * <p>
 * Contains control strategies that define how entities behave or move within the game.
 * This includes both user-input-based controls and autonomous behavior driven by AI strategies
 * such as pathfinding and targeting.
 * </p>
 *
 * <p><b>Main Strategy Classes:</b></p>
 * <ul>
 *   <li>{@code ConditionalControlStrategy} - Chooses behavior based on specific in-game conditions.</li>
 *   <li>{@code ConstantDirectionControlStrategy} - Moves an entity in a fixed direction continuously.</li>
 *   <li>{@code ControlManager} - Oversees and coordinates control strategies for all entities.</li>
 *   <li>{@code ControlStrategyHelperMethods} - Utility methods used across different control strategies.</li>
 *   <li>{@code FollowEntityControlStrategy} - Follows a specified target entity around the map.</li>
 *   <li>{@code JumpControlStrategy} - Allows entities to move in large discrete steps ("jumps").</li>
 *   <li>{@code KeyboardControlStrategy} - Accepts keyboard input for user-controlled entity movement.</li>
 *   <li>{@code NoneControlStrategy} - Represents entities with no control or fixed/static behavior.</li>
 *   <li>{@code PinballLaunchControlStrategy} - Launches entities with specific physics-style control (e.g., pinball launcher).</li>
 *   <li>{@code TargetControlStrategy} - Moves entities toward a target computed by a targeting strategy.</li>
 * </ul>
 *
 * <p><b>Sub-packages:</b></p>
 * <ul>
 *   <li>{@code pathfinding} - Contains strategies for determining movement paths toward targets using algorithms like BFS and Euclidean distance.</li>
 *   <li>{@code targetcalculation} - Provides strategies for selecting and computing target locations or entities.</li>
 * </ul>
 */
package oogasalad.player.model.strategies.control;