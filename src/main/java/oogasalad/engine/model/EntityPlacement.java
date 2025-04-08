package oogasalad.engine.model;

/**
 * Represents a specific instance of an {@link EntityType} placed at a location on the map. Each
 * placement includes an (x, y) coordinate and a mode (e.g., "Default", "PoweredUp"). Used to
 * instantiate and track the position and state of individual entities.
 * <p>
 * The `type` field is used during deserialization, while `resolvedEntityType` is populated later
 * when matching string types to actual {@link EntityType} objects.
 *
 * @author Will He, Angela Predolac
 */
public class EntityPlacement {

  private String type;
  private EntityType resolvedEntityType;
  private double x;
  private double y;
  private String mode;
  private int currentFrame;

  /**
   * A default constructor is required for the file parsing API.
   */
  public EntityPlacement() {
    // Empty constructor for file parsing API.
  }

  /**
   * Constructs a new EntityPlacement with a given type, position, and mode.
   *
   * @param type the {@link EntityType} of the entity
   * @param x    the X-coordinate of the entity on the map
   * @param y    the Y-coordinate of the entity on the map
   * @param mode the initial mode of the entity (e.g., "Default", "PoweredUp")
   */
  public EntityPlacement(EntityType type, double x, double y, String mode) {
    this.resolvedEntityType = type;
    this.type = type.type();
    this.x = x;
    this.y = y;
    this.mode = mode;
    this.currentFrame = 0;
  }

  /**
   * Returns the resolved {@link EntityType} of this placement.
   *
   * @return the actual {@link EntityType} instance associated with this placement
   */
  public EntityType getType() {
    return resolvedEntityType;
  }

  /**
   * Returns the unresolved string name of the entity type (used before resolution).
   *
   * @return the type string as defined in the config file
   */
  public String getTypeString() {
    return type;
  }

  /**
   * Sets the unresolved type string for this entity (typically used during JSON parsing).
   *
   * @param type the raw entity type name string
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Sets the resolved {@link EntityType} after matching the type string.
   *
   * @param resolvedEntityType the actual {@link EntityType} object for this placement
   */
  public void setResolvedEntityType(EntityType resolvedEntityType) {
    this.resolvedEntityType = resolvedEntityType;
  }

  /**
   * Returns the X-coordinate of this entity on the map.
   *
   * @return the X position
   */
  public double getX() {
    return x;
  }

  /**
   * Sets the X-coordinate of this entity on the map.
   *
   * @param x the new X position
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * Returns the Y-coordinate of this entity on the map.
   *
   * @return the Y position
   */
  public double getY() {
    return y;
  }

  /**
   * Sets the Y-coordinate of this entity on the map.
   *
   * @param y the new Y position
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   * Returns the mode of this entity (e.g., "Default", "PoweredUp").
   *
   * @return the current mode string
   */
  public String getMode() {
    return mode;
  }

  /**
   * Sets the mode of this entity (e.g., "Default", "PoweredUp").
   *
   * @param mode the new mode string
   */
  public void setMode(String mode) {
    this.mode = mode;
  }

  /**
   * Gets the current frame of animation the entity is on
   */
  public int getCurrentFrame() {
    return currentFrame;
  }

  /**
   * Increments the current frame of the animation by 1
   */
  public void increaseCurrentFrame() {
    currentFrame++;
  }
  /**
   * Moves the entity to a new (x, y) location on the map.
   *
   * @param x the new X-coordinate
   * @param y the new Y-coordinate
   */
  public void moveTo(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns a string representation of this EntityPlacement, including resolved type and position
   * info.
   *
   * @return a string summary of the entity placement
   */
  @Override
  public String toString() {
    return "EntityPlacement{" +
        "entityData=" + resolvedEntityType.type() +
        ", x=" + x +
        ", y=" + y +
        '}';
  }
}
