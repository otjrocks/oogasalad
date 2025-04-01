package oogasalad.engine.model.strategies;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;

/**
 * An interface to handle the result of collisions.
 *
 * @author Austin Huang
 */
public interface CollisionStrategy {

  /**
   * Handles events that happen after a collision.
   *
   * @param entity1 entity that initiates collision
   * @param entity2 entity that is collided against
   */
  void handleCollision(Entity entity1, Entity entity2, GameMap gameMap, GameState gameState)
      throws Exception;
}
