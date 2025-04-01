package oogasalad.engine.model.strategies;

import oogasalad.engine.model.entity.Entity;

public class UpdateScore implements CollisionStrategy {
  private int scoreIncrement;

  public UpdateScore(int increment) {
    this.scoreIncrement = increment;
  }

  @Override
  public void handleCollision(Entity entity1, Entity entity2) {
//    GameState.updateScore(scoreIncrement);
  }
}
