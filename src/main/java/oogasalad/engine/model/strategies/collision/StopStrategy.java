package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.records.CollisionContext;

/**
 * An implementation of the collision strategy that stops the first entity's movement when it
 * collides with the second entity.
 *
 * @author Owen Jennings
 */
public class StopStrategy implements CollisionStrategy {

  @Override
  public void handleCollision(CollisionContext collisionContext)
      throws EntityNotFoundException {
    double pacmanX = collisionContext.entity1().getEntityPlacement().getX();
    double pacmanY = collisionContext.entity1().getEntityPlacement().getY();
    double wallX = collisionContext.entity2().getEntityPlacement().getX();
    double wallY = collisionContext.entity2().getEntityPlacement().getY();

    if (pacmanX > wallX) {
      collisionContext.entity1().getEntityPlacement().setX(collisionContext.entity2().getEntityPlacement().getX() + 1);
    }
    if (pacmanX < wallX) {
      collisionContext.entity1().getEntityPlacement().setX(collisionContext.entity2().getEntityPlacement().getX() - 1);
    }
    if (pacmanY > wallY) {
      collisionContext.entity1().getEntityPlacement().setY(collisionContext.entity2().getEntityPlacement().getY() + 1);
    }
    if (pacmanY < wallY) {
      collisionContext.entity1().getEntityPlacement().setY(collisionContext.entity2().getEntityPlacement().getY() - 1);
    }
    collisionContext.entity1().setEntityDirection(' ');
  }
}
