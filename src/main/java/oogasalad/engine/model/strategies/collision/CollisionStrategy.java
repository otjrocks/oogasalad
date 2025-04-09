package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.records.CollisionContext;

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
 *  @see CollisionContext
 *  @see Entity
 *  @see GameMap
 *  @see GameState
 *  @see EntityNotFoundException
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
   * @param collisionContext the context of the collision, containing both entities,
   *                         the game map, and the current game state
   * @throws EntityNotFoundException if an entity involved in the collision cannot
   *                                  be found in the game map during processing
   */
  void handleCollision(CollisionContext collisionContext) throws EntityNotFoundException;
}
