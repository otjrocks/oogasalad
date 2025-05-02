/**
 * Contains strategies for determining how a game ends and how the outcome should be interpreted.
 *
 * <p>Each class in this package implements the
 * {@link oogasalad.player.model.strategies.gameoutcome.GameOutcomeStrategyInterface}, providing a
 * way to evaluate game-ending conditions and return outcome messages.</p>
 *
 * <p>Strategies are designed to be modular and extensible, allowing game designers to plug in
 * different ending conditions without modifying core game logic.</p>
 *
 * <p>Included strategies:</p>
 * <ul>
 *   <li>{@link oogasalad.player.model.strategies.gameoutcome.TimeBasedOutcomeStrategy}
 *   – Ends the game after a specified time limit.</li>
 *   <li>{@link oogasalad.player.model.strategies.gameoutcome.ScoreBasedOutcomeStrategy}
 *   – Ends the game when the player's score reaches a target threshold.</li>
 *   <li>{@link oogasalad.player.model.strategies.gameoutcome.LivesBasedOutcomeStrategy}
 *   – Ends the game when the player runs out of lives.</li>
 *   <li>{@link oogasalad.player.model.strategies.gameoutcome.EntityBasedOutcomeStrategy}
 *   – Ends the game when entities of a specific type (e.g., pellets) are gone.</li>
 * </ul>
 *
 * <p>These strategies promote separation of concerns and align with the Strategy design pattern
 * for flexible, runtime-configurable game flow control.</p>
 */
package oogasalad.player.model.strategies.gameoutcome;