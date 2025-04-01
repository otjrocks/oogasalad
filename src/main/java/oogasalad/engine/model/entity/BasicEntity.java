package oogasalad.engine.model.entity;

import oogasalad.engine.model.EntityData;

/**
 * An basic entity.
 *
 * @author Jessica Chen
 */
public class BasicEntity extends Entity {

  /**
   * The constructor for a basic entity.
   *
   * @param entityData The data used to create this entity.
   */
  public BasicEntity(EntityData entityData) {
    super(entityData);
  }

  @Override
  public void update() {
    // Intentionally does nothing because it is basic
  }


}
