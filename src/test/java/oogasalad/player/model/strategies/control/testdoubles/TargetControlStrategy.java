package oogasalad.player.model.strategies.control.testdoubles;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.strategies.control.ControlStrategyInterface;

public class TargetControlStrategy implements ControlStrategyInterface {

  public TargetControlStrategy(GameMapInterface map, EntityPlacement placement, ControlConfigInterface config) {
  }

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}
