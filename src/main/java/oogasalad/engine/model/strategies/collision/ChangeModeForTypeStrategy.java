package oogasalad.engine.model.strategies.collision;

import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.records.CollisionContextRecord;

/**
 * A {@link CollisionStrategy} that changes the newMode of all entities of a specified type
 * on the game map when a collision occurs.
 *
 * <p>This strategy can be used to trigger global behavior changes, such as putting all
 * ghosts into "Frightened" newMode when Pac-Man eats a power pellet.</p>
 *
 * <p>The affected entity type and the new newMode are specified at construction time and
 * remain constant for the lifetime of the strategy instance.</p>
 *
 * @author Austin Huang
 */
public class ChangeModeForTypeStrategy implements CollisionStrategy {
  private final String entityType;
  private final String newMode;

  /**
   * Constructs a new {@code ChangeModeForTypeStrategy} with the specified target type and newMode.
   *
   * @param entityType the type of entities whose newMode should be changed
   * @param newMode       the new newMode to assign to matching entities
   */
  public ChangeModeForTypeStrategy(String entityType, String newMode) {
    this.entityType = entityType;
    this.newMode = newMode;
  }

  /**
   * Applies the newMode change to all entities of the specified type on the game map.
   *
   * @param collisionContext the context of the collision, including access to the game map
   */
  @Override
  public void handleCollision(CollisionContextRecord collisionContext) {
    for (Entity entity : collisionContext.gameMap()) {
      if (entity.getEntityPlacement().getTypeString().equals(entityType)) {
        entity.getEntityPlacement().setMode(newMode);
      }
    }
  }
}
