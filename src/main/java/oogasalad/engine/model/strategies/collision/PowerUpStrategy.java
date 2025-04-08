package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.records.CollisionContext;

/**
 * The {@code PowerUpStrategy} class implements the {@link CollisionStrategy} interface
 * to apply a power-up effect when a collision occurs.
 *
 * <p>When two entities collide, this strategy grants a power-up to {@code entity1}.
 * The specific power-up behavior should be implemented within the {@code handleCollision}
 * method.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     CollisionStrategy strategy = new PowerUpStrategy();
 *     strategy.handleCollision(entity1, entity2, gameMap, gameState);
 * </pre>
 *
 * @author Austin Huang
 */
public class PowerUpStrategy implements CollisionStrategy {

  /**
   * Handles a collision by granting a power-up to {@code entity1}.
   *
   * <p>The exact effect of the power-up depends on the implementation.</p>
   *
   * @param collisionContext the context of the collision, containing both entities,
   *                         the game map, and the current game state
   */
  @Override
  public void handleCollision(CollisionContext collisionContext) {
    // implement power up of entity 1
  }
}
