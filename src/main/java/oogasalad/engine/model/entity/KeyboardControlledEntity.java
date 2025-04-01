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
  private static final double SPEED = 1.0;

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
    double dx = 0, dy = 0;

    if (inputManager.isMovingUp()) dy -= SPEED;
    if (inputManager.isMovingDown()) dy += SPEED;
    if (inputManager.isMovingLeft()) dx -= SPEED;
    if (inputManager.isMovingRight()) dx += SPEED;

    getEntityPlacement().setX(getEntityPlacement().getX() + dx);
    getEntityPlacement().setY(getEntityPlacement().getY() + dy);
  }
}
