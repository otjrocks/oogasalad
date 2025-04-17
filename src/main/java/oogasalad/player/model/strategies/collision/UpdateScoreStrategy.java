package oogasalad.player.model.strategies.collision;

import oogasalad.player.model.GameStateInterface;
import oogasalad.engine.records.CollisionContextRecord;

/**
 * The {@code UpdateScore} class implements the {@link CollisionStrategyInterface} interface to update the
 * game score when a collision occurs. When two entities collide, this strategy increments the score
 * by a specified amount.
 *
 * @author Austin Huang
 */
public class UpdateScoreStrategy implements CollisionStrategyInterface {

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
   * {@link GameStateInterface} by the configured {@code scoreIncrement} value.</p>
   *
   * @param collisionContext the context of the collision, containing both entities, the game map,
   *                         and the current game state
   */
  @Override
  public void handleCollision(CollisionContextRecord collisionContext) {
    collisionContext.gameState().updateScore(scoreIncrement);
  }
}
