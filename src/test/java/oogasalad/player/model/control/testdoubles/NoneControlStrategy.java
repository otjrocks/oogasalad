package oogasalad.player.model.control.testdoubles;

import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.control.ControlStrategy;

// Dummy strategy classes for testing
public class NoneControlStrategy implements ControlStrategy {

  public NoneControlStrategy() {
    // empty constructor
  }

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}
