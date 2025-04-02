package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;

public class StopStrategy implements CollisionStrategy {

  @Override
  public void handleCollision(Entity entity1, Entity entity2, GameMap gameMap, GameState gameState)
      throws EntityNotFoundException {
    if (entity1.getDx() > 0 && entity1.getEntityDirection() == 'R') {
      entity1.setEntityDirection(' ');
    }
    if (entity1.getDx() < 0 && entity1.getEntityDirection() == 'L') {
      entity1.setEntityDirection(' ');
    }
    if (entity1.getDy() > 0 && entity1.getEntityDirection() == 'D') {
      entity1.setEntityDirection(' ');
    }
    if (entity1.getDy() < 0 && entity1.getEntityDirection() == 'U') {
      entity1.setEntityDirection(' ');
    }
  }
}
