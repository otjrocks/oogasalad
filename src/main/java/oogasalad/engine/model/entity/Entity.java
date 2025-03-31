package oogasalad.engine.model.entity;

import oogasalad.engine.model.EntityData;

public abstract class Entity {
  private final EntityData myEntityData;

  protected Entity(EntityData entityData) {
    myEntityData = entityData;
  }

  public EntityData getEntityData() {
    return myEntityData;
  }

  // feel free to rename this, currently just updates the position
  public abstract void update();
}
