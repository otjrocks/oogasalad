package oogasalad.player.model.control.testdoubles;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.control.ControlStrategyInterface;

public class TargetControlStrategy implements ControlStrategyInterface {

  public TargetControlStrategy(GameMap map, EntityPlacement placement, ControlConfig config) {
  }

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}
