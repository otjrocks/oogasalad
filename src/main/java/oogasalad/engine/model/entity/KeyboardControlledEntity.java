package oogasalad.engine.model.entity;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityData;
import java.util.HashSet;
import java.util.Set;

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
   * @param entityData The entity data.
   */
  public KeyboardControlledEntity(GameInputManager input, EntityData entityData) {
    super(entityData);
    this.inputManager = input;
  }

  @Override
  public void update() {
    double dx = 0, dy = 0;

    if (inputManager.isMovingUp()) dy -= SPEED;
    if (inputManager.isMovingDown()) dy += SPEED;
    if (inputManager.isMovingLeft()) dx -= SPEED;
    if (inputManager.isMovingRight()) dx += SPEED;

    getEntityData().setInitialX(getEntityData().getInitialX() + dx);
    getEntityData().setInitialY(getEntityData().getInitialY() + dy);
  }
}
