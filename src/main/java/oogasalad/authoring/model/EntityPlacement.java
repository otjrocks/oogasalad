package oogasalad.authoring.model;

import oogasalad.engine.model.EntityData;

/**
 * Represents the placement of an entity on the game canvas. Stores both the entity data and its
 * position coordinates.
 */
public class EntityPlacement {

  private EntityData entityData;
  private double x;
  private double y;

  /**
   * Creates a new EntityPlacement with the specified entity data and coordinates.
   *
   * @param entityData The EntityData defining the entity's properties
   * @param x          The x-coordinate of the entity on the canvas
   * @param y          The y-coordinate of the entity on the canvas
   */
  public EntityPlacement(EntityData entityData, double x, double y) {
    this.entityData = entityData;
    this.x = x;
    this.y = y;
  }

  /**
   * @return The EntityData associated with this placement
   */
  public EntityData getEntityData() {
    return entityData;
  }

  /**
   * Sets a new EntityData for this placement.
   *
   * @param entityData The new EntityData
   */
  public void setEntityData(EntityData entityData) {
    this.entityData = entityData;
  }

  /**
   * @return The x-coordinate of this entity placement
   */
  public double getX() {
    return x;
  }

  /**
   * Sets the x-coordinate of this entity placement.
   *
   * @param x The new x-coordinate
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * @return The y-coordinate of this entity placement
   */
  public double getY() {
    return y;
  }

  /**
   * Sets the y-coordinate of this entity placement.
   *
   * @param y The new y-coordinate
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   * Moves this entity placement to the specified coordinates.
   *
   * @param x The new x-coordinate
   * @param y The new y-coordinate
   */
  public void moveTo(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Updates the initial position in the EntityData to match the current placement coordinates.
   */
  public void updateInitialPosition() {
    this.entityData.setInitialX(this.x);
    this.entityData.setInitialY(this.y);
  }

  /**
   * @return A string representation of this EntityPlacement
   */
  @Override
  public String toString() {
    return "EntityPlacement{" +
        "entityData=" + entityData.getType() +
        ", x=" + x +
        ", y=" + y +
        '}';
  }
}
