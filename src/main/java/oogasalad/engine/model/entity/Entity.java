package oogasalad.engine.model.entity;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.control.ControlStrategy;
import oogasalad.player.model.control.ControlStrategyFactory;

/**
 * An abstract class to represent an Entity in the game.
 *
 * @author Jessica Chen
 */
public class Entity {

  private final EntityPlacement myEntityPlacement;
  private final GameInputManager inputManager;
  private final GameMap gameMap;
  private double dx;
  private double dy;
  private Direction currentDirection;
  public static final double ENTITY_SPEED = 0.12;

  /**
   * Initialize the entity with the provided entity data.
   *
   * @param entityPlacement The data used to initialize this entity.
   * @
   */
  public Entity(GameInputManager input, EntityPlacement entityPlacement,
      GameMap gameMap) {
    myEntityPlacement = entityPlacement;
    this.inputManager = input;
    this.gameMap = gameMap;
  }

  /**
   * Get the entity data object for this entity.
   *
   * @return An EntityType object
   * @see EntityType
   */
  public EntityPlacement getEntityPlacement() {
    return myEntityPlacement;
  }

  // feel free to rename this, currently just updates the position

  /**
   * Handle the update of an Entity.
   */
  public void update() {
    ControlStrategy strategy = ControlStrategyFactory.createControlStrategy(inputManager,
        myEntityPlacement, gameMap);

    strategy.update(this);
  }

  /**
   * Set the movement direction of this entity.
   *
   * @param direction The new movement direction.
   */
  public void setEntityDirection(Direction direction) {
    currentDirection = direction;
    updateEntityVelocity();
  }

  /**
   * Get the entity direction
   *
   * @return The direction character for this entity.
   */
  public Direction getEntityDirection() {
    return currentDirection;
  }

  private void updateEntityVelocity() {
    setDx(currentDirection.getDx() * ENTITY_SPEED);
    setDy(currentDirection.getDy() * ENTITY_SPEED);
  }

  /**
   * Get the movement velocity, in the x direction.
   *
   * @return A double representing the current entity's x velocity.
   */
  public double getDx() {
    return dx;
  }

  /**
   * Get the movement velocity, in the y direction.
   *
   * @return A double representing the current entity's y velocity.
   */
  public double getDy() {
    return dy;
  }

  /**
   * Set the movement velocity, in the x direction.
   *
   * @param dx The new velocity in the x direction.
   */
  public void setDx(double dx) {
    this.dx = dx;
  }

  /**
   * Set the movement velocity in the y direction.
   *
   * @param dy The new movement velocity.
   */
  public void setDy(double dy) {
    this.dy = dy;
  }

  /**
   * Determines if the entity can move in the specified direction based on its current position
   * and the defined movement speed.
   *
   * @param direction the direction to check for movement ('R' for right, 'L' for left, or other
   *                  characters for vertical movement)
   * @return true if the entity can move in the specified direction, false otherwise
   */
  public boolean canMove(Direction direction) {
    if (direction == Direction.R || direction == Direction.L) {
      return this.getEntityPlacement().getY() - (int) this.getEntityPlacement().getY()
          < ENTITY_SPEED;
    } else {
      return this.getEntityPlacement().getX() - (int) this.getEntityPlacement().getX()
          < ENTITY_SPEED;
    }
  }

}
