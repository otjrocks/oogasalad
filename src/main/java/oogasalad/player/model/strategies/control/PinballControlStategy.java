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
}
