package oogasalad.player.model.strategies.control;

import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.controller.GameInputManager;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.Entity;

/**
 * The KeyboardControlStrategy class implements the ControlStrategy interface and provides
 * functionality for controlling an entity's movement based on keyboard input. It uses a 
 * GameInputManager to detect keyboard actions, a GameMap to check for obstacles, and an 
 * EntityPlacement to determine the entity's current position.
 *
 * <p>This class updates the direction of an entity based on the current keyboard input, ensuring
 * that the entity can only move in valid directions (e.g., no walls blocking the path).
 *
 * @author Jessica Chen
 */
public class KeyboardControlStrategy implements ControlStrategyInterface {

  private final GameMapInterface myGameMap;
  private final EntityPlacement myEntityPlacement;
  private final GameInputManager myInputManager;

  /**
   * Constructs a KeyboardControlStrategy with the specified input manager, game map, and
   * entity placement.
   *
   * @param input           the GameInputManager that handles keyboard input
   * @param gameMap         the GameMap representing the current game state
   * @param entityPlacement the EntityPlacement representing the entity's position
   */
  public KeyboardControlStrategy(GameInputManager input,
      GameMapInterface gameMap, EntityPlacement entityPlacement) {
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
    if (entity.canMove(Direction.R) && myInputManager.isMovingRight() && checkNoWall(myX + 1, myY)) {
      entity.setEntityDirection(Direction.R);
    }
  }

  private void setLeftDirection(int myX, int myY, Entity entity) {
    if (entity.canMove(Direction.L) && myInputManager.isMovingLeft() && checkNoWall(myX - 1, myY)) {
      entity.setEntityDirection(Direction.L);
    }
  }

  private void setDownDirection(int myX, int myY, Entity entity) {
    if (entity.canMove(Direction.D) && myInputManager.isMovingDown() && checkNoWall(myX, myY + 1)) {
      entity.setEntityDirection(Direction.D);
    }
  }

  private void setUpDirection(int myX, int myY, Entity entity) {
    if (entity.canMove(Direction.U) && myInputManager.isMovingUp() && checkNoWall(myX, myY - 1)) {
      entity.setEntityDirection(Direction.U);
    }
  }

  private boolean checkNoWall(int x, int y) {
    return myGameMap.getEntityAt(x, y)
        .filter(entity -> entity.getEntityPlacement().getType().type().equals("Wall"))
        .isEmpty();
  }
}