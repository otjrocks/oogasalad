package oogasalad.player.model.control.testdoubles;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMapInterface;
import oogasalad.engine.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.control.ControlStrategyInterface;

public class TargetControlStrategy implements ControlStrategyInterface {

  public TargetControlStrategy(GameMapInterface map, EntityPlacement placement, ControlConfigInterface config) {
  }

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}
