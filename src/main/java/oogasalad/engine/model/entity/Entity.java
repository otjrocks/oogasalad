package oogasalad.engine.model.entity;

import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.control.ControlStrategyInterface;
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
    ControlStrategyInterface strategy = ControlStrategyFactory.createControlStrategy(inputManager,
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
   * Set the movement direction of this entity and if changing directions snap it to the nearest
   * int if it is close enough.
   *
   * @param direction The new movement direction.
   */
  public void setEntitySnapDirection(Direction direction) {
    boolean isChangingFromHorizontalToVertical =
        isHorizontal(currentDirection) && isVertical(direction);
    boolean isChangingFromVerticalToHorizontal =
        isVertical(currentDirection) && isHorizontal(direction);

    if (isChangingFromHorizontalToVertical) {
      snapToNearestWhole(myEntityPlacement.getX(), myEntityPlacement::setX);
    } else if (isChangingFromVerticalToHorizontal) {
      snapToNearestWhole(myEntityPlacement.getY(), myEntityPlacement::setY);
    }

    currentDirection = direction;
    updateEntityVelocity();
  }

  private boolean isHorizontal(Direction dir) {
    return dir == Direction.L || dir == Direction.R;
  }

  private boolean isVertical(Direction dir) {
    return dir == Direction.U || dir == Direction.D;
  }

  private void snapToNearestWhole(double value, java.util.function.DoubleConsumer setter) {
    double nearest = Math.round(value);
    if (Math.abs(value - nearest) <= 0.2) {
      setter.accept(nearest);
    }
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
