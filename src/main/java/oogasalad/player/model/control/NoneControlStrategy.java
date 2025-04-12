package oogasalad.player.model.control;

import oogasalad.engine.model.entity.Entity;

public class NoneControlStrategy implements ControlStrategy {

  @Override
  public void update(Entity entity) {
    // intentionally does nothing
  }
}