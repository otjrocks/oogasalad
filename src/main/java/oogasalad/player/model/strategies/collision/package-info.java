/**
 * <p>
 * Contains classes that define different collision strategies for handling entity interactions during gameplay.
 * Each strategy modifies the game state based on specific collision outcomes, such as updating lives,
 * changing modes, or teleporting entities.
 * </p>
 *
 * <p><b>Strategy Classes:</b></p>
 * <ul>
 *   <li>{@code AddTailStrategy} - Adds a tail segment to the entity upon collision.</li>
 *   <li>{@code ChangeModeForEntityStrategy} - Changes the mode of a specific entity.</li>
 *   <li>{@code ChangeModeForTypeStrategy} - Changes the mode of all entities of a given type.</li>
 *   <li>{@code ConsumeStrategy} - Removes the collided entity from the game.</li>
 *   <li>{@code RemoveAllEntitiesOfTypeStrategy} - Removes all entities of a specified type.</li>
 *   <li>{@code ResetTimeElapsedStrategy} - Resets time tracking for an entity or system.</li>
 *   <li>{@code ReturnToSpawnLocationStrategy} - Returns an entity to its original spawn location.</li>
 *   <li>{@code StopStrategy} - Stops the entity’s movement.</li>
 *   <li>{@code TeleportToPartnerPortalStrategy} - Teleports the entity to its linked portal partner.</li>
 *   <li>{@code TemporaryModeChangeStrategy} - Temporarily changes the entity’s mode for a duration.</li>
 *   <li>{@code UpdateLivesStrategy} - Modifies the number of lives.</li>
 *   <li>{@code UpdateScoreStrategy} - Modifies the player’s score.</li>
 * </ul>
 */
package oogasalad.player.model.strategies.collision;