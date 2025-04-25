package oogasalad.player.model.strategies.collision;

import static oogasalad.engine.records.CollisionContextRecord.StrategyAppliesTo.ENTITY1;

import oogasalad.engine.exceptions.EntityNotFoundException;
import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.engine.records.CollisionContextRecord.StrategyAppliesTo;
import oogasalad.player.model.GameMapInterface;

/**
 * The {@code ConsumeStrategy} class implements the {@link CollisionStrategyInterface} interface to
 * handle collisions where one entity "consumes" another.
 *
 * <p>When a collision occurs, this strategy removes the second entity from the
 * {@link GameMapInterface}. If the entity is not found, an {@link EntityNotFoundException} is
 * thrown.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     CollisionStrategy strategy = new ConsumeStrategy();
 *     strategy.handleCollision(entity1, entity2, gameMap, gameState);
 * </pre>
 *
 * @author Austin Huang
 */
public class ConsumeStrategy implements CollisionStrategyInterface {

  /**
   * Handles a collision event where {@code entity2} is removed from the game map.
   *
   * <p>If {@code entity2} is not found in the {@link GameMapInterface}, an
   * {@link EntityNotFoundException} is thrown.</p>
   *
   * @param collisionContext the context of the collision, containing both entities, the game map,
   *                         and the current game state
   * @throws EntityNotFoundException if {@code entity2} does not exist in {@code gameMap}
   */
  @Override
  public void handleCollision(CollisionContextRecord collisionContext)
      throws EntityNotFoundException {
    try {
      if (collisionContext.strategyAppliesTo() == ENTITY1) {
        collisionContext.gameMap().removeEntity(collisionContext.entity1());
      } else {
        collisionContext.gameMap().removeEntity(collisionContext.entity2());
      }
    } catch (EntityNotFoundException e) {
      throw new EntityNotFoundException(
          "Cannot remove requested entity, because it does not exist in the game map!", e);
    }
  }
}
