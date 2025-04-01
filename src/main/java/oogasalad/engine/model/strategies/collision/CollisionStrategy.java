package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;

/**
 * Represents a strategy for handling the outcome of collisions between entities in the game.
 *
 * <p>Implementations of this interface define specific behaviors that occur when two entities
 * collide, such as removing an entity, updating the score, or triggering a power-up.</p>
 *
 * <p>Example implementations:</p>
 * <ul>
 *     <li>{@code UpdateScoreStrategy} - Increases the game score upon collision.</li>
 *     <li>{@code ConsumeStrategy} - Removes an entity from the game map.</li>
 *     <li>{@code PowerUpStrategy} - Grants a power-up to an entity after a collision.</li>
 * </ul>
 *
 * <p>To apply a collision strategy, use:</p>
 * <pre>
 *     CollisionStrategy strategy = new UpdateScoreStrategy(100);
 *     strategy.handleCollision(entity1, entity2, gameMap, gameState);
 * </pre>
 *
 * @author Austin Huang
 */
public interface CollisionStrategy {

  /**
   * Defines the behavior that should occur after a collision between two entities.
   *
   * <p>This method is called whenever two entities collide, allowing for different
   * collision outcomes depending on the strategy implementation.</p>
   *
   * @param entity1 the entity that initiates the collision
   * @param entity2 the entity that is collided against
   * @param gameMap the game map containing all entities
   * @param gameState the current state of the game, used to apply collision effects
   * @throws EntityNotFoundException if an error occurs while processing the collision
   */
  void handleCollision(Entity entity1, Entity entity2, GameMap gameMap, GameState gameState)
      throws EntityNotFoundException;
}
