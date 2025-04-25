package oogasalad.player.model.strategies.control;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.controlConfig.ConstantDirectionControlConfigRecord;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;

/**
 * A control strategy that allows the entity to travel in a set constant direction, based on a
 * provided dx and dy.
 *
 * @author Owen Jennings
 */
public class ConstantDirectionControlStrategy implements ControlStrategyInterface {

  private final int myDx;
  private final int myDy;

  /**
   * Create a constant direction control strategy. The dx and dy combination must correspond with a
   * valid Direction object or else the entity will not move.
   *
   * @param gameMap       The game map of this entity.
   * @param placement     The placement of the entity.
   * @param controlConfig The control config object with the required fields of this control
   *                      strategy. It should be a ConstantDirectionControlConfigRecord.
   */
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
