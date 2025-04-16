package oogasalad.player.model.control.testdoubles;

import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.control.ControlStrategyInterface;

// Dummy strategy classes for testing
public class NoneControlStrategy implements ControlStrategyInterface {

  public NoneControlStrategy() {
    // empty constructor
  }

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}
