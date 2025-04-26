package oogasalad.player.model.strategies.control;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.controlConfig.ConstantDirectionControlConfigRecord;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.records.config.model.controlConfig.JumpControlConfigRecord;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.controller.GameInputManager;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;

/**
 * The JumpControlStrategy class implements a control strategy for handling jumping mechanics in a
 * game. It manages the vertical movement of an entity by responding to user input, applying
 * gravity, and ensuring collision detection with the game map.
 *
 * <p>This class uses the following components:
 * <ul>
 *   <li>{@link GameInputManager} to handle user input for jumping.</li>
 *   <li>{@link GameMapInterface} to check for collisions with the game map.</li>
 *   <li>{@link EntityPlacement} to update the entity's position.</li>
 * </ul>
 *
 * <p>Key features include:
 * <ul>
 *   <li>Handling jump input and initiating a jump when the jump key is pressed.</li>
 *   <li>Applying gravity to simulate realistic falling behavior.</li>
 *   <li>Collision detection to stop movement when hitting the ground or ceiling.</li>
 * </ul>
 */
public class JumpControlStrategy implements ControlStrategyInterface {

  private final GameMapInterface myGameMap;
  private final EntityPlacement myEntityPlacement;
  private final GameInputManager myInputManager;
  private boolean isJumping = false;
  private double verticalVelocity = 0;
  private final double myJumpForce;
  private final double myGravity;

  /**
   * A strategy for controlling the jump behavior of an entity in the game. This class interacts
   * with the game input manager, game map, and entity placement to manage and execute jump-related
   * actions.
   *
   * @param input           the game input manager responsible for handling user inputs
   * @param gameMap         the game map interface providing access to the game's map data
   * @param entityPlacement the entity placement manager responsible for managing entity positions
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
          "Error instantiating ConstantDirectionControlConfigRecord, unable to cast provided ControlConfigInterface to ConstantDirectionControlConfigRecord",
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
    if (myInputManager.isMovingUp() && !isJumping) {
      verticalVelocity = myJumpForce;
      isJumping = true;
      myInputManager.removeUpKey();
    }
  }


  private void applyGravity() {
    verticalVelocity += myGravity;
  }

  private void moveEntity(Entity entity) {
    double tentativeY = myEntityPlacement.getY() + verticalVelocity;

    if (myGameMap.isNotBlocked(myEntityPlacement.getTypeString(),
        (int) myEntityPlacement.getX(), (int) tentativeY)) {
      myEntityPlacement.setY(tentativeY);
    } else {
      isJumping = false;
      verticalVelocity = 0;
    }
  }
}
