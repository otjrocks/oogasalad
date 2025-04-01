package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;

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
   * @param entity1 the entity that receives the power-up
   * @param entity2 the entity that triggers the power-up effect
   * @param gameMap the game map containing all entities
   * @param gameState the current state of the game, which may be modified by the power-up
   */
  @Override
  public void handleCollision(Entity entity1, Entity entity2, GameMap gameMap,
      GameState gameState) {
    // implement power up of entity 1
  }
}
