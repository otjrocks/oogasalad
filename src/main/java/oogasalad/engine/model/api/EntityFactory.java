package oogasalad.engine.model.api;

import javafx.scene.Scene;
import oogasalad.engine.model.Entity;
import oogasalad.engine.model.EntityData;

public class EntityFactory {

  public static Entity createEntity(Scene scene, EntityData entityData) {
    return new Entity(scene, entityData);
  }

}
