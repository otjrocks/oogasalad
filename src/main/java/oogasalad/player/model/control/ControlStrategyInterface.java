package oogasalad.player.model.control;

import oogasalad.engine.model.entity.Entity;

/**
 * Interface representing a control strategy for updating an entity.
 * Implementations of this interface define specific behaviors for how an entity should be updated.
 * 
 * @author Jessica Chen
 */
public interface ControlStrategyInterface {

  /**
   * Updates the state of the given entity based on the specific control strategy.
   *
   * @param entity the entity to be updated
   */
  void update(Entity entity);

}
