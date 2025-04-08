package oogasalad.engine.records;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;

public record CollisionContext(Entity entity1, Entity entity2, GameMap gameMap, GameState gameState) {
  public CollisionContext {
    if (entity1 == null || entity2 == null || gameMap == null || gameState == null) {
      throw new IllegalArgumentException("CollisionContext can't contain null values.");
    }
  }
}
