package oogasalad.player.model.strategies.collision;

import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.engine.records.CollisionContextRecord.StrategyAppliesTo;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.model.Entity;

/**
 * An implementation of the collision strategy that stops the movement of the entity to which the
 * strategy applies when it collides with the other entity.
 * <p>
 * The entity is "pushed back" by one unit to avoid overlap and its direction is set to NONE.
 * <p>
 * Author: Owen Jennings
 */
public class StopStrategy implements CollisionStrategyInterface {

  @Override
  public void handleCollision(CollisionContextRecord collisionContext) {
    // I used ChatGPT to refactor this method to use strategy applies to enum data.
    Entity target = collisionContext.strategyAppliesTo() == StrategyAppliesTo.ENTITY1
        ? collisionContext.entity1()
        : collisionContext.entity2();

    Entity other = collisionContext.strategyAppliesTo() == StrategyAppliesTo.ENTITY1
        ? collisionContext.entity2()
        : collisionContext.entity1();

    double targetX = target.getEntityPlacement().getX();
    double targetY = target.getEntityPlacement().getY();
    double otherX = other.getEntityPlacement().getX();
    double otherY = other.getEntityPlacement().getY();

    if (targetX > otherX) {
      target.getEntityPlacement().setX(otherX + 1);
    } else if (targetX < otherX) {
      target.getEntityPlacement().setX(otherX - 1);
    }

    if (targetY > otherY) {
      target.getEntityPlacement().setY(otherY + 1);
    } else if (targetY < otherY) {
      target.getEntityPlacement().setY(otherY - 1);
    }

    target.setEntityDirection(Direction.NONE);
  }
}
