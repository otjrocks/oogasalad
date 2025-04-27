package oogasalad.player.model.strategies.collision;


import oogasalad.engine.exceptions.EntityNotFoundException;
import oogasalad.engine.records.CollisionContextRecord;

/**
 * A Collision strategy that resets the game time elapsed when the collision occurs.
 *
 * @author Owen Jennings
 */
public class ResetTimeElapsedStrategy implements CollisionStrategyInterface {

  @Override
  public void handleCollision(CollisionContextRecord collisionContext) {
    collisionContext.gameState().resetTimeElapsed();
  }
}
