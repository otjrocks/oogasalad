package oogasalad.player.model.strategies.control;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.player.controller.GameInputManager;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;

public class JumpControlStrategy implements ControlStrategyInterface {

  private final GameMapInterface myGameMap;
  private final EntityPlacement myEntityPlacement;
  private final GameInputManager myInputManager;
  private boolean isJumping = false;
  private boolean jumpKeyPressed = false;
  private double verticalVelocity = 0;
  private final double jumpForce = -2; // NOTE: need to unhard code
  private final double gravity = .05;

  public JumpControlStrategy(GameInputManager input,
      GameMapInterface gameMap, EntityPlacement entityPlacement) {
    myEntityPlacement = entityPlacement;
    myInputManager = input;
    myGameMap = gameMap;
  }

  @Override
  public void update(Entity entity) {
    handleJumpInput();
    applyGravity();
    moveEntity(entity);
  }

  private void handleJumpInput() {
    if (myInputManager.isMovingUp()) {
      if (!jumpKeyPressed && !isJumping) {
        verticalVelocity = jumpForce;
        isJumping = true;
        myInputManager.removeUpKey();
      }
      jumpKeyPressed = true; // Mark key as handled
    } else {
      // Player has released UP key; allow new jump next time
      jumpKeyPressed = false;
    }
  }


  private void applyGravity() {
    verticalVelocity += gravity;
  }

  private void moveEntity(Entity entity) {
    double tentativeY = myEntityPlacement.getY() + verticalVelocity;

    if (myGameMap.isNotBlocked(myEntityPlacement.getTypeString(),
        (int) myEntityPlacement.getX(), (int) tentativeY)) {
      myEntityPlacement.setY(tentativeY);
    } else {
      // Hit ground or ceiling
      if (verticalVelocity > 0) { // Falling down
        isJumping = false; // Now on the ground
      }
      verticalVelocity = 0;
    }
  }
}
