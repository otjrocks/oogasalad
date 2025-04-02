package oogasalad.engine.model.entity;

import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.strategies.collision.StopStrategy;

/**
 * An entity that can use keyboard input.
 *
 * @author Troy Ludwig
 */
public class KeyboardControlledEntity extends Entity {

  private final GameInputManager inputManager;



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
  }

  private void updateCurrentDirectionFromKeyboardInput() {
    if (inputManager.isMovingUp()) {
      this.setEntityDirection('U');
    } else if (inputManager.isMovingDown()) {
      this.setEntityDirection('D');
    } else if (inputManager.isMovingLeft()) {
      this.setEntityDirection('L');
    } else if (inputManager.isMovingRight()) {
      this.setEntityDirection('R');
    }
  }

}
