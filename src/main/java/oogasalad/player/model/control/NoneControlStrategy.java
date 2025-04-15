package oogasalad.player.model.control;

import oogasalad.engine.model.entity.Entity;

/**
 * A control strategy that performs no actions. This class is used when no control behavior
 * is required for an entity. The {@code update} method is intentionally left empty.
 * 
 * <p>Implements the {@link ControlStrategyInterface} interface.</p>
 * 
 * @author Jessica Chen
 */
public class NoneControlStrategy implements ControlStrategyInterface {

  @Override
  public void update(Entity entity) {
    // intentionally does nothing
  }
}