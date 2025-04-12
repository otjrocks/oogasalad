package oogasalad.player.model.control;

import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;

public class KeyboardControlStrategy implements ControlStrategy {

  private final GameMap myGameMap;
  private final EntityPlacement myEntityPlacement;
  private final GameInputManager myInputManager;

  public KeyboardControlStrategy(GameInputManager input,
      GameMap gameMap, EntityPlacement entityPlacement) {
    myEntityPlacement = entityPlacement;
    myInputManager = input;
    myGameMap = gameMap;
  }


  @Override
  public void update(Entity entity) {
    updateCurrentDirectionFromKeyboardInput(entity);
  }

  private void updateCurrentDirectionFromKeyboardInput(Entity entity) {
    int myX = (int) myEntityPlacement.getX();
    int myY = (int) myEntityPlacement.getY();
    setEntityDirection(myX, myY, entity);
  }

  private void setEntityDirection(int myX, int myY, Entity entity) {
    setUpDirection(myX, myY, entity);
    setDownDirection(myX, myY, entity);
    setLeftDirection(myX, myY, entity);
    setRightDirection(myX, myY, entity);
  }

  private void setRightDirection(int myX, int myY, Entity entity) {
    if (entity.canMove('R') && myInputManager.isMovingRight() && checkNoWall(myX + 1, myY)) {
      entity.setEntityDirection('R');
    }
  }

  private void setLeftDirection(int myX, int myY, Entity entity) {
    if (entity.canMove('L') && myInputManager.isMovingLeft() && checkNoWall(myX - 1, myY)) {
      entity.setEntityDirection('L');
    }
  }

  private void setDownDirection(int myX, int myY, Entity entity) {
    if (entity.canMove('D') && myInputManager.isMovingDown() && checkNoWall(myX, myY + 1)) {
      entity.setEntityDirection('D');
    }
  }

  private void setUpDirection(int myX, int myY, Entity entity) {
    if (entity.canMove('U') && myInputManager.isMovingUp() && checkNoWall(myX, myY - 1)) {
      entity.setEntityDirection('U');
    }
  }

  private boolean checkNoWall(int x, int y) {
    return myGameMap.getEntityAt(x, y)
        .filter(entity -> entity.getEntityPlacement().getType().type().equals("Wall"))
        .isEmpty();
  }
}