package oogasalad.player.model.strategies.control.testdoubles;

import oogasalad.player.model.Entity;
import oogasalad.player.model.strategies.control.ControlStrategyInterface;

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
