package oogasalad.player.model.strategies.collision;

import static oogasalad.engine.records.CollisionContextRecord.StrategyAppliesTo.ENTITY1;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.exceptions.EntityNotFoundException;
import oogasalad.engine.records.CollisionContextRecord;

/**
 * When the collision between the two provided entities occurs, teleport the first entity provided
 * in the collision back to its spawn location (initial x and y location).
 *
 * @author Owen Jennings
 */
public class ReturnToSpawnLocationStrategy implements CollisionStrategyInterface {

  @Override
  public void handleCollision(CollisionContextRecord collisionContext)
      throws EntityNotFoundException {
    if (collisionContext.strategyAppliesTo() == ENTITY1) {
      EntityPlacement entity1Placement = collisionContext.entity1().getEntityPlacement();
      entity1Placement.setX(entity1Placement.getInitialX());
      entity1Placement.setY(entity1Placement.getInitialY());
    } else {
      EntityPlacement entity2Placement = collisionContext.entity2().getEntityPlacement();
      entity2Placement.setX(entity2Placement.getInitialX());
      entity2Placement.setY(entity2Placement.getInitialY());
    }
  }
}
