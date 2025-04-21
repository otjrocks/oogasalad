package oogasalad.engine.records;

import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.GameStateInterface;

/**
 * A record that encapsulates the context of a collision event in the game.
 * <p>
 * This record is used to provide relevant information to collision handlers, such as the two
 * entities involved, the game map in which the collision occurred, and the current state of the
 * game.
 * </p>
 *
 * @param entity1           The first entity involved in the collision.
 * @param entity2           The second entity involved in the collision.
 * @param gameMap           The current game map where the collision takes place.
 * @param gameState         The overall state of the game at the time of the collision.
 * @param strategyAppliesTo Indicates which entity the collision strategy should be applied to
 *                          (ENTITY1 or ENTITY2).
 */
public record CollisionContextRecord(
    Entity entity1,
    Entity entity2,
    GameMapInterface gameMap,
    GameStateInterface gameState,
    StrategyAppliesTo strategyAppliesTo
) {

  public CollisionContextRecord {
    if (entity1 == null || entity2 == null || gameMap == null || gameState == null
        || strategyAppliesTo == null) {
      throw new IllegalArgumentException("CollisionContext can't contain null values.");
    }
  }

  /**
   * Enum to indicate which entity the collision strategy applies to. For example, for a consume
   * strategy, if strategy applies to is set to entity 1, then entity 1 should be consumed.
   */
  public enum StrategyAppliesTo {
    ENTITY1,
    ENTITY2
  }
}
