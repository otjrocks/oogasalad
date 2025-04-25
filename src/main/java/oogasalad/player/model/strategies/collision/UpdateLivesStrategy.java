package oogasalad.player.model.strategies.collision;

import oogasalad.engine.records.CollisionContextRecord;

/**
 * The {@code UpdateLivesStrategy} class implements the {@link CollisionStrategyInterface} interface
 * to update the player's lives when a collision occurs.
 *
 * <p>When two entities collide, this strategy modifies the number of lives in the
 * {@code GameState} by a specified amount. The increment value can be positive (to add lives) or
 * negative (to decrease lives).</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     CollisionStrategy strategy = new UpdateLivesStrategy(-1); // Decrease lives by 1
 *     strategy.handleCollision(entity1, entity2, gameMap, gameState);
 * </pre>
 *
 * @author Austin Huang
 */
public class UpdateLivesStrategy implements CollisionStrategyInterface {

  private final int livesIncrement;

  /**
   * Constructs an {@code UpdateLivesStrategy} with a specified lives increment.
   *
   * @param livesIncrement the number of lives to be added or removed upon collision
   */
  public UpdateLivesStrategy(int livesIncrement) {
    this.livesIncrement = livesIncrement;
  }

  /**
   * Handles a collision by updating the number of lives in the game state.
   *
   * <p>This method modifies the player's remaining lives based on the
   * {@code livesIncrement} value. A positive value increases lives, while a negative value
   * decreases them.</p>
   *
   * @param collisionContext the context of the collision, containing both entities, the game map,
   *                         and the current game state
   */
  @Override
  public void handleCollision(CollisionContextRecord collisionContext) {
    collisionContext.gameState().updateLives(livesIncrement);
  }
}
