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

  @Override
  public void update(Entity entity) {
    if (myIsMoving) {
      moveEntity(entity);
    } else {
      handleChargeInput();
    }
  }

  private void handleChargeInput() {
    // Check if a direction key is being pressed
    if (myInputManager.isMovingUp()) {
      handleDirectionCharge(1);
    } else if (myInputManager.isMovingDown()) {
      handleDirectionCharge(2);
    } else if (myInputManager.isMovingLeft()) {
      handleDirectionCharge(3);
    } else if (myInputManager.isMovingRight()) {
      handleDirectionCharge(4);
    } else {
      // No key is pressed, release if we were charging
      if (myIsCharging) {
        launchEntity();
      }
    }
  }

  private void handleDirectionCharge(int direction) {
    // Start or continue charging in the current direction
    if (!myIsCharging || myChargeDirection != direction) {
      myIsCharging = true;
      myChargeDirection = direction;
      myCurrentCharge = 0.0;
    } else {
      // Increase charge up to max
      myCurrentCharge = Math.min(myCurrentCharge + myChargeRate, myMaxLaunchForce);
    }
  }

  private void launchEntity() {
    myIsCharging = false;
    myIsMoving = true;
    myTraveledDistance = 0.0;

    // Set velocity based on direction and charge amount
    switch (myChargeDirection) {
        case 1: // Up
          myXVelocity = 0;
          myYVelocity = -myCurrentCharge;
          break;
        case 2: // Down
          myXVelocity = 0;
          myYVelocity = myCurrentCharge;
          break;
        case 3: // Left
          myXVelocity = -myCurrentCharge;
          myYVelocity = 0;
          break;
        case 4: // Right
          myXVelocity = myCurrentCharge;
          myYVelocity = 0;
          break;
        default:
          myIsMoving = false;
          break;
      }
      
      myChargeDirection = 0;
      myCurrentCharge = 0.0;
  }

  private void moveEntity(){
    if (Math.abs(myXVelocity) < 0.1 && Math.abs(myYVelocity) < 0.1 || myTraveledDistance >= myMaxDistance) {
        // Stop moving if velocity is very low or max distance reached
        myIsMoving = false;
        myXVelocity = 0.0;
        myYVelocity = 0.0;
        myTraveledDistance = 0.0;
        return;
    }

    // Calculate new position
    double tentativeX = myEntityPlacement.getX() + myXVelocity;
    double tentativeY = myEntityPlacement.getY() + myYVelocity;
    
    // Check if the new position is valid
    boolean canMoveX = myGameMap.isNotBlocked(myEntityPlacement.getTypeString(),
        (int) tentativeX, (int) myEntityPlacement.getY());
    boolean canMoveY = myGameMap.isNotBlocked(myEntityPlacement.getTypeString(),
        (int) myEntityPlacement.getX(), (int) tentativeY);

    // Update position
    double oldX = myEntityPlacement.getX();
    double oldY = myEntityPlacement.getY();
    
    if (canMoveX) {
      myEntityPlacement.setX(tentativeX);
    } else {
      // Bounce off wall in X direction
      myXVelocity = -myXVelocity * 0.5; // Reduce velocity on bounce
    }

    if (canMoveY) {
        myEntityPlacement.setY(tentativeY);
    } else {
    // Bounce off wall in Y direction
    myYVelocity = -myYVelocity * 0.5; // Reduce velocity on bounce
    }

    // Calculate distance traveled in this step
    double stepDistance = Math.sqrt(
        Math.pow(myEntityPlacement.getX() - oldX, 2) + 
        Math.pow(myEntityPlacement.getY() - oldY, 2)
    );
    myTraveledDistance += stepDistance;

    // Apply friction
    myXVelocity *= (1.0 - myFriction);
    myYVelocity *= (1.0 - myFriction);

    
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
