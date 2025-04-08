package oogasalad.engine.model.entity;

import oogasalad.engine.model.EntityPlacement;

/**
 * A basic entity.
 *
 * @author Jessica Chen
 */
public class BasicEntity extends Entity {

  /**
   * The constructor for a basic entity.
   *
   * @param entityPlacement The data used to create this entity.
   */
  public BasicEntity(EntityPlacement entityPlacement) {
    super(entityPlacement);
  }

  @Override
  public void update() {
    // Intentionally does nothing because it is basic
  }


}
