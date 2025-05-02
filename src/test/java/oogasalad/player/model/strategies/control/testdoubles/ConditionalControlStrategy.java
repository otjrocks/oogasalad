package oogasalad.player.model.strategies.control.testdoubles;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.strategies.control.ControlStrategyInterface;

public class ConditionalControlStrategy implements ControlStrategyInterface {

  public ConditionalControlStrategy(GameMapInterface map, EntityPlacement placement,
      ControlConfigInterface config) {
  }

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}
