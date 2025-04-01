package oogasalad.engine.model.Strategies;

/**
 * An interface to handle the result of collisions
 *
 * @author Austin Huang
 */
public interface CollisionStrategy {

  /**
   * Get the collision strategy.
   *
   * @return A collision strategy.
   */
  CollisionStrategy getCollisionStrategy();

  /**
   * Set the action on collision for this strategy.
   */
  void actionOnCollision();
}
