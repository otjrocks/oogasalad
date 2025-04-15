package oogasalad.player.model.control.testdoubles;

import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.control.ControlStrategy;

public class KeyboardControlStrategy implements ControlStrategy {

  public KeyboardControlStrategy(GameInputManager input, GameMap map, EntityPlacement placement) {
  }

  @Override
  public void update(Entity entity) {
    // dummy class
  }
}
