package oogasalad.player.model.strategies.control;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.records.config.model.controlConfig.PinballControlConfigRecord;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.controller.GameInputManager;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;


/**
 * The PinballLaunchControlStrategy class implements a pinball-like launch behavior.
 * Holding a direction key will build up launch power, and releasing launches the entity
 * in that direction with a velocity proportional to how long the key was held.
 */
public class PinballControlStategy implements ControlStrategyInterface {
    private final GameMapInterface myGameMap;
    private final EntityPlacement myEntityPlacement;
    private final GameInputManager myInputManager;

    // Configuration parameters for the pinball launch mechanics
    private final double myMaxLaunchForce;
    private final double myChargeRate;
    private final double myFriction;
    private final double myMaxDistance;
    
    // State variables
    private double myCurrentCharge = 0.0;
    private boolean myIsCharging = false;
    private boolean myIsMoving = false;
    private double myXVelocity = 0.0;
    private double myYVelocity = 0.0;
    private double myTraveledDistance = 0.0;
    private int myChargeDirection = 0; // 0:none, 1:up, 2:down, 3:left, 4:right

    /**
   * PinballLaunchControlStrategy is responsible for handling the pinball-like launch control
   * logic for an entity in the game. It allows the entity to be launched in a direction
   * with a force proportional to how long the direction key was held.
   *
   * @param input           the GameInputManager used to handle input for the launch action
   * @param gameMap         the GameMapInterface representing the game map
   * @param entityPlacement the EntityPlacement used to manage the entity's position
   * @param controlConfig   the ControlConfigInterface containing configuration for the pinball control
   * @throws ClassCastException if the provided ControlConfigInterface cannot be cast to
   *                           PinballControlConfigRecord
   */

   public PinballLaunchControlStrategy(GameInputManager input,
      GameMapInterface gameMap, EntityPlacement entityPlacement,
      ControlConfigInterface controlConfig) {
    myEntityPlacement = entityPlacement;
    myInputManager = input;
    myGameMap = gameMap;
    
    try {
      PinballControlConfigRecord config = (PinballControlConfigRecord) controlConfig;
      myMaxLaunchForce = config.maxLaunchForce();
      myChargeRate = config.chargeRate();
      myFriction = config.friction();
      myMaxDistance = config.maxDistance();
    } catch (ClassCastException e) {
      LoggingManager.LOGGER.warn(
          "Error instantiating PinballLaunchControlStrategy, unable to cast provided ControlConfigInterface to PinballControlConfigRecord",
          e);
      throw e;
    }
  }

  public void update(Entity entity){

  }

  private void handleChargeInput(){

  }
  private void handleDirectionCharge(){

  }

  private void launchEntity() {

  }

  private void moveEntity(){

  }

  /**
   * Returns the current charge value as a percentage of the maximum launch force.
   * This can be used by the UI to display a charge meter.
   * 
   * @return percentage of maximum charge (0.0 to 1.0)
   */
  public double getChargePercentage() {
    return myCurrentCharge / myMaxLaunchForce;
  }

  /**
   * Returns whether the entity is currently in charging state.
   * 
   * @return true if charging, false otherwise
   */
  public boolean isCharging() {
    return myIsCharging;
  }

  /**
   * Returns whether the entity is currently moving from a launch.
   * 
   * @return true if moving, false otherwise
   */
  public boolean isMoving() {
    return myIsMoving;
  }
}





   

}
