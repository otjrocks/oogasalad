package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.records.CollisionContext;

/**
 * When the collision between the two provided entities occurs, teleport the first entity provided
 * in the collision back to its spawn location (initial x and y location).
 *
 * @author Owen Jennings
 */
public class ReturnToSpawnLocationStrategy implements CollisionStrategy {

  @Override
  public void handleCollision(CollisionContext collisionContext) throws EntityNotFoundException {
    EntityPlacement entity1Placement = collisionContext.entity1().getEntityPlacement();
    entity1Placement.setX(entity1Placement.getInitialX());
    entity1Placement.setY(entity1Placement.getInitialY());
  }
}
