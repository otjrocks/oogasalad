package oogasalad.engine.model.entity;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;

/**
 * An abstract class to represent an Entity in the game.
 *
 * @author Jessica Chen
 */
public abstract class Entity {

  private final EntityPlacement myEntityPlacement;
  private double dx;
  private double dy;
  private char currentDirection;
  public static final double ENTITY_SPEED = 0.15;

  /**
   * Initialize the entity with the provided entity data.
   *
   * @param entityPlacement The data used to initialize this entity.
   */
  protected Entity(EntityPlacement entityPlacement) {
    myEntityPlacement = entityPlacement;
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
  public abstract void update();

  /**
   * Set the movement direction of this entity.
   *
   * @param direction The new movement direction.
   */
  public void setEntityDirection(char direction) {
    currentDirection = direction;
    updateEntityVelocity();
  }

  /**
   * Get the entity direction
   * @return The direction character for this entity.
   */
  public char getEntityDirection() {
    return currentDirection;
  }

  private void updateEntityVelocity() {
    if (currentDirection == 'U') {
      setDy(-ENTITY_SPEED);
      setDx(0);
    }
    if (currentDirection == 'D') {
      setDy(ENTITY_SPEED);
      setDx(0);
    }
    if (currentDirection == 'L') {
      setDx(-ENTITY_SPEED);
      setDy(0);
    }
    if (currentDirection == 'R') {
      setDx(ENTITY_SPEED);
      setDy(0);
    }
    if (currentDirection == ' ') {
      setDx(0);
      setDy(0);
    }
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
}
