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

  // just static values because these help give it the effect of moving vertically
  private static final double FALL_MULTIPLIER = 1.1;
  private static final double LOW_JUMP_MULTIPLIER = 0.8;

  private final double myJumpForce;
  private final double myGravity;

  /**
   * JumpControlStrategy is responsible for handling the jump control logic for an entity in the game.
   * It utilizes the provided input manager, game map, entity placement, and control configuration
   * to determine the behavior of the jump action.
   *
   * <p>This strategy applies a jump force and gravity to simulate realistic jumping mechanics.
   * The control configuration must be of type {@code JumpControlConfigRecord}, otherwise a 
   * {@code ClassCastException} will be thrown.
   *
   * @param input the {@code GameInputManager} used to handle input for the jump action
   * @param gameMap the {@code GameMapInterface} representing the game map
   * @param entityPlacement the {@code EntityPlacement} used to manage the entity's position
   * @param controlConfig the {@code ControlConfigInterface} containing configuration for the jump control
   * @throws ClassCastException if the provided {@code ControlConfigInterface} cannot be cast to {@code JumpControlConfigRecord}
   */
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

  // written with help from chatGPT to make the jump feel floaty
  private void applyGravity() {
    double velocity = myEntityPlacement.getVerticalVelocity();

    if (velocity > 0) {
      myEntityPlacement.setVerticalVelocity(
          velocity + myGravity * FALL_MULTIPLIER
      );
    } else if (velocity < 0) {
      myEntityPlacement.setVerticalVelocity(
          velocity + myGravity * LOW_JUMP_MULTIPLIER
      );
    } else {
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
        myEntityPlacement.setJumping(false);
      }
      myEntityPlacement.setVerticalVelocity(0);
    }
  }
}
