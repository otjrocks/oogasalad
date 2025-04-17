package oogasalad.engine.model.strategies.collision;


import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.records.CollisionContextRecord;

public class ResetTimeElapsedStrategy implements CollisionStrategy {

  @Override
  public void handleCollision(CollisionContextRecord collisionContext)
      throws EntityNotFoundException {
    collisionContext.gameState().resetTimeElapsed();
  }
}
