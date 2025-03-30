package oogasalad.engine.model.api;

import oogasalad.engine.model.Entity;
import oogasalad.engine.model.EntityData;

public class EntityFactory {

  public static Entity createEntity(EntityData entityData) {
    return new Entity(entityData);
  }

}
