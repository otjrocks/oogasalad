package oogasalad.engine.model.entity;

import oogasalad.engine.model.EntityData;

/**
 * An abstract class to represent an Entity in the game.
 *
 * @author Jessica Chen
 */
public abstract class Entity {

  private final EntityData myEntityData;

  /**
   * Initialize the entity with the provided entity data.
   *
   * @param entityData The data used to initialize this entity.
   */
  protected Entity(EntityData entityData) {
    myEntityData = entityData;
  }

  /**
   * Get the entity data object for this entity.
   *
   * @return An EntityData object
   * @see EntityData
   */
  public EntityData getEntityData() {
    return myEntityData;
  }

  // feel free to rename this, currently just updates the position

  /**
   * Handle the update of an Entity.
   */
  public abstract void update();
}
