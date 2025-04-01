package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;

/**
 * The {@code UpdateScore} class implements the {@link CollisionStrategy} interface
 *  to update the game score when a collision occurs. When two entities collide,
 *  this strategy increments the score by a specified amount.
 *
 * @author Austin Huang
 */
public class UpdateScoreStrategy implements CollisionStrategy {
  private final int scoreIncrement;

  /**
   * Constructs an {@code UpdateScore} strategy with a specified score increment.
   *
   * @param increment the amount by which the score should be increased on collision
   */
  public UpdateScoreStrategy(int increment) {
    this.scoreIncrement = increment;
  }

  /**
   * Handles a collision between two entities by updating the game score.
   *
   * <p>When a collision occurs, this method updates the score in the provided
   * {@link GameState} by the configured {@code scoreIncrement} value.</p>
   *
   * @param entity1 the first entity involved in the collision
   * @param entity2 the second entity involved in the collision
   * @param gameMap the game map containing all entities (unused in this strategy)
   * @param gameState the current state of the game, where the score will be updated
   */
  @Override
  public void handleCollision(Entity entity1, Entity entity2, GameMap gameMap,
      GameState gameState) {
    gameState.updateScore(scoreIncrement);
  }
}
