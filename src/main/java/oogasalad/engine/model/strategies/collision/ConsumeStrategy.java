package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;

/**
 * The {@code ConsumeStrategy} class implements the {@link CollisionStrategy} interface
 * to handle collisions where one entity "consumes" another.
 *
 * <p>When a collision occurs, this strategy removes the second entity from the {@link GameMap}.
 * If the entity is not found, an {@link EntityNotFoundException} is thrown.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     CollisionStrategy strategy = new ConsumeStrategy();
 *     strategy.handleCollision(entity1, entity2, gameMap, gameState);
 * </pre>
 *
 * @author Austin Huang
 */
public class ConsumeStrategy implements CollisionStrategy {

  /**
   * Handles a collision event where {@code entity2} is removed from the game map.
   *
   * <p>If {@code entity2} is not found in the {@link GameMap}, an {@link EntityNotFoundException}
   * is thrown.</p>
   *
   * @param entity1 the first entity involved in the collision
   * @param entity2 the second entity involved in the collision (which will be removed)
   * @param gameMap the game map that tracks entities
   * @param gameState the current state of the game
   * @throws EntityNotFoundException if {@code entity2} does not exist in {@code gameMap}
   */
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
