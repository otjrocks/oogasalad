package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.records.CollisionContextRecord;

/**
 * A {@link CollisionStrategy} that changes the mode of all entities of a specified type
 * on the game map when a collision occurs.
 *
 * <p>This strategy can be used to trigger global behavior changes, such as putting all
 * ghosts into "Frightened" mode when Pac-Man eats a power pellet.</p>
 *
 * <p>The affected entity type and the new mode are specified at construction time and
 * remain constant for the lifetime of the strategy instance.</p>
 *
 * @author Austin Huang
 */
public class ChangeModeForTypeStrategy implements CollisionStrategy {
  private String entityType;
  private String mode;

  /**
   * Constructs a new {@code ChangeModeForTypeStrategy} with the specified target type and mode.
   *
   * @param entityType the type of entities whose mode should be changed
   * @param mode       the new mode to assign to matching entities
   */
  public ChangeModeForTypeStrategy(String entityType, String mode) {
    this.entityType = entityType;
    this.mode = mode;
  }

  /**
   * Applies the mode change to all entities of the specified type on the game map.
   *
   * @param collisionContext the context of the collision, including access to the game map
   */
  @Override
  public void handleCollision(CollisionContextRecord collisionContext) {
    for (Entity entity : collisionContext.gameMap()) {
      if (entity.getEntityPlacement().getTypeString().equals(entityType)) {
        entity.getEntityPlacement().setMode(mode);
      }
    }
  }
}
