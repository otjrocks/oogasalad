package oogasalad.engine.model.entity;

import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;

/**
 * An entity that can use keyboard input.
 *
 * @author Troy Ludwig
 */
public class KeyboardControlledEntity extends Entity {

  private final GameInputManager inputManager;
  private static final double SPEED = 0.15;

  private char currentDirection = ' ';

  /**
   * Create a keyboard controlled entity.
   *
   * @param entityPlacement The entity data.
   */
  public KeyboardControlledEntity(GameInputManager input, EntityPlacement entityPlacement) {
    super(entityPlacement);
    this.inputManager = input;
  }

  @Override
  public void update() {
    updateCurrentDirectionFromKeyboardInput();
    updateEntityLocation();
  }

  private void updateEntityLocation() {
    double dx = 0, dy = 0;
    if (currentDirection == 'U') {
      dy -= SPEED;
      dx = 0;
    }
    if (currentDirection == 'D') {
      dy += SPEED;
      dx = 0;
    }
    if (currentDirection == 'L') {
      dx -= SPEED;
      dy = 0;
    }
    if (currentDirection == 'R') {
      dx += SPEED;
      dy = 0;
    }

    getEntityPlacement().setX(getEntityPlacement().getX() + dx);
    getEntityPlacement().setY(getEntityPlacement().getY() + dy);
  }

  private void updateCurrentDirectionFromKeyboardInput() {
    if (inputManager.isMovingUp()) {
      currentDirection = 'U';
    } else if (inputManager.isMovingDown()) {
      currentDirection = 'D';
    } else if (inputManager.isMovingLeft()) {
      currentDirection = 'L';
    } else if (inputManager.isMovingRight()) {
      currentDirection = 'R';
    }
  }
}
