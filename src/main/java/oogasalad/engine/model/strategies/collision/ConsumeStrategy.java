package oogasalad.engine.model.strategies;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;

public class ConsumeStrategy implements CollisionStrategy {

  @Override
  public void handleCollision(Entity entity1, Entity entity2, GameMap gameMap, GameState gameState)
      throws EntityNotFoundException {
    try {
      gameMap.removeEntity(entity2);
    }
    catch (EntityNotFoundException e) {
      throw new EntityNotFoundException(
          "Cannot remove requested entity, because it does not exist in the game map!");
    }
  }
}
