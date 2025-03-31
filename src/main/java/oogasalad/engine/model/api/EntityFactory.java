package oogasalad.engine.model.api;

import oogasalad.engine.model.entity.BfsEntity;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.EntityData;

public class EntityFactory {

  public static Entity createEntity(EntityData entityData) {
    return new BfsEntity(entityData);
  }

}
