package oogasalad.player.model.strategies.control.testdoubles;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.player.controller.GameInputManager;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.strategies.control.ControlStrategyInterface;

public class KeyboardControlStrategy implements ControlStrategyInterface {

  public KeyboardControlStrategy(GameInputManager input, GameMapInterface map,
      EntityPlacement placement) {
  }

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}
