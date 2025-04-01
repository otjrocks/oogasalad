package oogasalad.engine.model.entity;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;

/**
 * An abstract class to represent an Entity in the game.
 *
 * @author Jessica Chen
 */
public abstract class Entity {

  private final EntityPlacement myEntityPlacement;

  /**
   * Initialize the entity with the provided entity data.
   *
   * @param entityPlacement The data used to initialize this entity.
   */
  protected Entity(EntityPlacement entityPlacement) {
    myEntityPlacement = entityPlacement;
  }

  /**
   * Get the entity data object for this entity.
   *
   * @return An EntityType object
   * @see EntityType
   */
  public EntityPlacement getEntityPlacement() {
    return myEntityPlacement;
  }

  // feel free to rename this, currently just updates the position

  /**
   * Handle the update of an Entity.
   */
  public abstract void update();
}
