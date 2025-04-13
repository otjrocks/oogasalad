package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.records.CollisionContextRecord;

public class ChangeModeForTypeStrategy implements CollisionStrategy {
  private String entityType;
  private String mode;

  public ChangeModeForTypeStrategy(String entityType, String mode) {
    this.entityType = entityType;
    this.mode = mode;
  }

  @Override
  public void handleCollision(CollisionContextRecord collisionContext) {

  }
}
