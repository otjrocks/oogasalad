package oogasalad.player.model.strategies.control;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.controlConfig.ConstantDirectionControlConfigRecord;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;

public class ConstantDirectionControlStrategy implements ControlStrategyInterface {

  private final int myDx;
  private final int myDy;

  public ConstantDirectionControlStrategy(GameMapInterface gameMap,
      EntityPlacement placement,
      ControlConfigInterface controlConfig) {
    try {
      ConstantDirectionControlConfigRecord config = (ConstantDirectionControlConfigRecord) controlConfig;
      myDx = config.dx();
      myDy = config.dy();
    } catch (ClassCastException e) {
      LoggingManager.LOGGER.warn(
          "Error instantiating ConstantDirectionControlConfigRecord, unable to cast provided ControlConfigInterface to ConstantDirectionControlConfigRecord",
          e);
      throw e;
    }
  }

  @Override
  public void update(Entity entity) {
    ControlStrategyHelperMethods.setEntityDirection(myDx, myDy, entity);
  }
}
