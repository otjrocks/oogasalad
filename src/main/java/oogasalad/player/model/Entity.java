package oogasalad.player.model;

import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.controller.GameInputManager;
import oogasalad.engine.records.model.EntityTypeRecord;
import oogasalad.player.model.strategies.control.ControlStrategyInterface;
import oogasalad.player.model.api.ControlStrategyFactory;

/**
 * An abstract class to represent an Entity in the game.
 *
 * @author Jessica Chen
 */
public class Entity {

  private final EntityPlacement myEntityPlacement;
  private final GameInputManager inputManager;
  private final GameMapInterface gameMap;
  private double dx;
  private double dy;
  private final double speed;
  private Direction currentDirection;
  public static final double ENTITY_SPEED_MULTIPLIER = 0.12;
  public static final double MIN_SPEED = 0;
  public static final double MAX_SPEED = 0.5;

  /**
   * Initialize the entity with the provided entity data.
   *
   * @param entityPlacement The data used to initialize this entity.
   * @
   */
  public Entity(GameInputManager input, EntityPlacement entityPlacement,
      GameMapInterface gameMap) {
    myEntityPlacement = entityPlacement;
    this.inputManager = input;
    this.gameMap = gameMap;
    speed = setSpeedFromConfig(entityPlacement);
  }

  private double setSpeedFromConfig(EntityPlacement entityPlacement) {
    final double speed;
    if (myEntityPlacement != null && myEntityPlacement.getType() != null) {
      speed = getTransformedSpeed(entityPlacement);
    } else {
      speed = 0;
    }
    return speed;
  }

  private static double getTransformedSpeed(EntityPlacement entityPlacement) {
    // Enforce speed is in range [0, 0.5] and multiply by a constant to transform to a reasonable amount
    return Math.max(MIN_SPEED,
        Math.min(ENTITY_SPEED_MULTIPLIER * entityPlacement.getType().speed(), MAX_SPEED));
  }

  /**
   * Get the speed associated with this entity.
   *
   * @return A double representing the transformed and validated speed of this entity. The speed
   * will be in the range [MIN_SPEED, MAX_SPEED]
   */
  double getSpeed() {
    return speed;
  }

  /**
   * Get the entity data object for this entity.
   *
   * @return An EntityType object
   * @see EntityTypeRecord
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
   * Set the movement direction of this entity and if changing directions snap it to the nearest int
   * if it is close enough.
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
    setDx(currentDirection.getDx() * speed);
    setDy(currentDirection.getDy() * speed);
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
   * Determines if the entity can move in the specified direction based on its current position and
   * the defined movement speed.
   *
   * @param direction the direction to check for movement ('R' for right, 'L' for left, or other
   *                  characters for vertical movement)
   * @return true if the entity can move in the specified direction, false otherwise
   */
  public boolean canMove(Direction direction) {
    if (direction == Direction.R || direction == Direction.L) {
      return this.getEntityPlacement().getY() - (int) this.getEntityPlacement().getY()
          < speed;
    } else {
      return this.getEntityPlacement().getX() - (int) this.getEntityPlacement().getX()
          < speed;
    }
  }

}
