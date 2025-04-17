package oogasalad.engine.records;

import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.GameStateInterface;
import oogasalad.player.model.Entity;

/**
 * A record that encapsulates the context of a collision event in the game.
 * <p>
 * This record is used to provide relevant information to collision handlers,
 * such as the two entities involved, the game map in which the collision occurred,
 * and the current state of the game.
 * </p>
 *
 * @param entity1     The first entity involved in the collision.
 * @param entity2     The second entity involved in the collision.
 * @param gameMap     The current game map where the collision takes place.
 * @param gameState   The overall state of the game at the time of the collision.
 */
public record CollisionContextRecord(Entity entity1, Entity entity2, GameMapInterface gameMap, GameStateInterface gameState) {
  public CollisionContextRecord {
    if (entity1 == null || entity2 == null || gameMap == null || gameState == null) {
      throw new IllegalArgumentException("CollisionContext can't contain null values.");
    }
  }
}
