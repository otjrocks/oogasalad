package oogasalad.engine.model.strategies.collision;


import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.records.CollisionContextRecord;

/**
 * A Collision strategy that resets the game time elapsed when the collision occurs.
 *
 * @author Owen Jennings
 */
public class ResetTimeElapsedStrategy implements CollisionStrategy {

  @Override
  public void handleCollision(CollisionContextRecord collisionContext)
      throws EntityNotFoundException {
    collisionContext.gameState().resetTimeElapsed();
  }
}
