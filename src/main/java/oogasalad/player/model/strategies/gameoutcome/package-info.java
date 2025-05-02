/**
 * <p>
 * Contains strategies for determining when a game ends and whether a player has won or lost.
 * These outcome strategies evaluate different aspects of gameplay to assess completion or failure.
 * </p>
 *
 * <p><b>Available Outcome Strategies:</b></p>
 * <ul>
 *   <li>{@code EntityBasedOutcomeStrategy} - Ends the game based on the presence or absence of specific entities.</li>
 *   <li>{@code LivesBasedOutcomeStrategy} - Triggers a game over when a player's lives reach zero.</li>
 *   <li>{@code ScoreBasedOutcomeStrategy} - Determines outcomes based on a player's score reaching a defined threshold.</li>
 *   <li>{@code TimeBasedOutcomeStrategy} - Ends the game when a specified time limit is reached.</li>
 * </ul>
 */
package oogasalad.player.model.strategies.gameoutcome;