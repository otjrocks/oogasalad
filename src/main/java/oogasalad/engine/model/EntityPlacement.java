package oogasalad.engine.model;

/**
 * EntityPlacement defines a single placed instance of an entityType on the map.
 */
public class EntityPlacement {

  private EntityType type;
  private double x;
  private double y;
  private String mode;

  public EntityPlacement(EntityType type, double x, double y, String mode) {
    this.type = type;
    this.x = x;
    this.y = y;
    this.mode = mode;
  }

  public EntityType getType() {
    return type;
  }

  public void setType(EntityType type) {
    this.type = type;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public void moveTo(double x, double y) {
    this.x = x;
    this.y = y;
  }
  @Override
  public String toString() {
    return "EntityPlacement{" +
        "entityData=" + type.getType() +
        ", x=" + x +
        ", y=" + y +
        '}';
  }
}
