package oogasalad.engine.model.entity;

import oogasalad.engine.model.EntityData;

public class BasicEntity extends Entity {
  public BasicEntity(EntityData entityData) {
    super(entityData);
  }

  @Override
  public void update() {
    // Intentionaly does nothing because it is basic
  }


}
