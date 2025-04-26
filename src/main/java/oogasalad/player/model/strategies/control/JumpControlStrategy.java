package oogasalad.player.model.strategies.control;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.records.config.model.controlConfig.JumpControlConfigRecord;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.controller.GameInputManager;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;

/**
 * The JumpControlStrategy class implements a hovering-style jump behavior,
 * with only vertical motion. The entity does not move horizontally during jumps.
 */
public class JumpControlStrategy implements ControlStrategyInterface {

  private final GameMapInterface myGameMap;
  private final EntityPlacement myEntityPlacement;
  private final GameInputManager myInputManager;

  // Softer multipliers for more floaty feeling
  private static final double FALL_MULTIPLIER = 1.1;
  private static final double LOW_JUMP_MULTIPLIER = 0.8;

  private final double myJumpForce;
  private final double myGravity;

  public JumpControlStrategy(GameInputManager input,
      GameMapInterface gameMap, EntityPlacement entityPlacement,
      ControlConfigInterface controlConfig) {
    myEntityPlacement = entityPlacement;
    myInputManager = input;
    myGameMap = gameMap;
    try {
      JumpControlConfigRecord config = (JumpControlConfigRecord) controlConfig;
      myJumpForce = -config.jumpforce();
      myGravity = config.gravity();
    } catch (ClassCastException e) {
      LoggingManager.LOGGER.warn(
          "Error instantiating JumpControlStrategy, unable to cast provided ControlConfigInterface to JumpControlConfigRecord",
          e);
      throw e;
    }
  }

  @Override
  public void update(Entity entity) {
    handleJumpInput();
    applyGravity();
    moveEntity(entity);
  }

  private void handleJumpInput() {
    if (myInputManager.isMovingUp()) {
      if (!myEntityPlacement.isJumping()) {
        myEntityPlacement.setVerticalVelocity(myJumpForce);
        myEntityPlacement.setJumping(true);
      }
      myInputManager.removeUpKey();
    }
  }

  private void applyGravity() {
    double velocity = myEntityPlacement.getVerticalVelocity();

    if (velocity > 0) {
      // Falling: slightly boosted but gentle gravity
      myEntityPlacement.setVerticalVelocity(
          velocity + myGravity * FALL_MULTIPLIER
      );
    } else if (velocity < 0) {
      // Going up: very gentle gravity
      myEntityPlacement.setVerticalVelocity(
          velocity + myGravity * LOW_JUMP_MULTIPLIER
      );
    } else {
      // Neutral: apply normal gravity
      myEntityPlacement.setVerticalVelocity(
          velocity + myGravity
      );
    }
  }

  private void moveEntity(Entity entity) {
    double tentativeY = myEntityPlacement.getY() + myEntityPlacement.getVerticalVelocity();

    if (myGameMap.isNotBlocked(myEntityPlacement.getTypeString(),
        (int) myEntityPlacement.getX(), (int) tentativeY)) {
      myEntityPlacement.setY(tentativeY);
    } else {
      if (myEntityPlacement.getVerticalVelocity() > 0) {
        // Only reset jump if falling down
        myEntityPlacement.setJumping(false);
      }
      myEntityPlacement.setVerticalVelocity(0);
    }
  }
}
