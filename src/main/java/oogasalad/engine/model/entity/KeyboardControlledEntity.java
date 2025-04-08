package oogasalad.engine.model.entity;

import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;

/**
 * An entity that can use keyboard input.
 *
 * @author Troy Ludwig
 */
public class KeyboardControlledEntity extends Entity {
  private final GameInputManager inputManager;
  private final GameMap gameMap;


  /**
   * Create a keyboard controlled entity.
   *
   * @param entityPlacement The entity data.
   */
  public KeyboardControlledEntity(GameInputManager input, EntityPlacement entityPlacement,
      GameMap gameMap) {
    super(entityPlacement);
    this.inputManager = input;
    this.gameMap = gameMap;
  }

  @Override
  public void update() {
    updateCurrentDirectionFromKeyboardInput();
  }

  private void updateCurrentDirectionFromKeyboardInput() {
    int myX = (int) this.getEntityPlacement().getX();
    int myY = (int) this.getEntityPlacement().getY();
    setEntityDirection(myX, myY);
  }

  private void setEntityDirection(int myX, int myY) {
    setUpDirection(myX, myY);
    setDownDirection(myX, myY);
    setLeftDirection(myX, myY);
    setRightDirection(myX, myY);
  }

  private void setRightDirection(int myX, int myY) {
    if (canMove('R') && inputManager.isMovingRight() && checkNoWall(myX + 1, myY)) {
      this.setEntityDirection('R');
    }
  }

  private void setLeftDirection(int myX, int myY) {
    if (canMove('L') && inputManager.isMovingLeft() && checkNoWall(myX - 1, myY)) {
      this.setEntityDirection('L');
    }
  }

  private void setDownDirection(int myX, int myY) {
    if (canMove('D') && inputManager.isMovingDown() && checkNoWall(myX, myY + 1)) {
      this.setEntityDirection('D');
    }
  }

  private void setUpDirection(int myX, int myY) {
    if (canMove('U') && inputManager.isMovingUp() && checkNoWall(myX, myY - 1)) {
      this.setEntityDirection('U');
    }
  }

  private boolean checkNoWall(int x, int y) {
    return gameMap.getEntityAt(x, y)
        .filter(entity -> entity.getEntityPlacement().getType().type().equals("Wall"))
        .isEmpty();
  }


  private boolean canMove(char direction) {
    if (direction == 'R' || direction == 'L') {
      return this.getEntityPlacement().getY() - (int) this.getEntityPlacement().getY() < ENTITY_SPEED;
    } else {
      return this.getEntityPlacement().getX() - (int) this.getEntityPlacement().getX() < ENTITY_SPEED;
    }
  }

}
