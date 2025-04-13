package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.entity.Entity;
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
    for (Entity entity : collisionContext.gameMap()) {
      if (entity.getEntityPlacement().getTypeString().equals(entityType)) {
        entity.getEntityPlacement().setMode(mode);
      }
    }
  }
}
