package oogasalad.engine.config;

import oogasalad.engine.records.model.EntityTypeRecord;

/**
 * Represents a specific instance of an {@link EntityTypeRecord} placed at a location on the map.
 * Each placement includes an (x, y) coordinate and a mode (e.g., "Default", "PoweredUp"). Used to
 * instantiate and track the position and state of individual entities.
 * <p>
 * The `type` field is used during deserialization, while `resolvedEntityType` is populated later
 * when matching string types to actual {@link EntityTypeRecord} objects.
 *
 * @author Will He, Angela Predolac
 */
public class EntityPlacement {

  private String type;
  private EntityTypeRecord resolvedEntityType;
  private double x;
  private double y;
  private double initialX;
  private double initialY;
  private int initialTileX; // For authoring purposes
  private int initialTileY; // For authoring purposes
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
   * @param type the {@link EntityTypeRecord} of the entity
   * @param x    the X-coordinate of the entity on the map
   * @param y    the Y-coordinate of the entity on the map
   * @param mode the initial mode of the entity (e.g., "Default", "PoweredUp")
   */
  public EntityPlacement(EntityTypeRecord type, double x, double y, String mode) {
    this.resolvedEntityType = type;
    this.type = type.type();
    this.x = x;
    this.y = y;
    this.initialX = x;
    this.initialY = y;
    this.mode = mode;
    this.currentFrame = 0;
  }

  /**
   * Returns the resolved {@link EntityTypeRecord} of this placement.
   *
   * @return the actual {@link EntityTypeRecord} instance associated with this placement
   */
  public EntityTypeRecord getType() {
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
   * Sets the resolved {@link EntityTypeRecord} after matching the type string.
   *
   * @param resolvedEntityType the actual {@link EntityTypeRecord} object for this placement
   */
  public void setResolvedEntityType(EntityTypeRecord resolvedEntityType) {
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
   * Returns the initial X-coordinate of this entity when it was initialized. Not updated when setX
   * is called.
   *
   * @return A double representing the initial x value of this entity.
   */
  public double getInitialX() {
    return initialX;
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
   * Returns the initial Y-coordinate of this entity when it was initialized. Not updated when setY
   * is called.
   *
   * @return A double representing the initial y value of this entity.
   */
  public double getInitialY() {
    return initialY;
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
   * Get tile x coordinate.
   *
   * @return x coord
   */
  public int getInitialTileX() {
    return initialTileX;
  }

  /**
   * Set tile x coordinate.
   *
   * @param initialTileX x coord.
   */
  public void setInitialTileX(int initialTileX) {
    this.initialTileX = initialTileX;
  }

  /**
   * Get tile y coord.
   *
   * @return y coord.
   */
  public int getInitialTileY() {
    return initialTileY;
  }

  /**
   * Set tile y coord.
   *
   * @param initialTileY y coord.
   */
  public void setInitialTileY(int initialTileY) {
    this.initialTileY = initialTileY;
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

  /**
   * Gets the image path for the current entity based on its type and mode. Retrieves the image path
   * from the mode configuration of the entity's current mode.
   *
   * @return the string path to the entity's image
   */
  public String getEntityImagePath() {
    return this.getType()
        .modes()
        .get(this.getMode())
        .image().imagePath();
  }

  /**
   * Returns the width of the entity's associated image in pixels.
   */
  public int getEntityImageWidth() {
    return this.getType()
        .modes()
        .get(this.getMode())
        .image().tileWidth();
  }

  /**
   * Returns the height of the entity's associated image in pixels
   */
  public int getEntityImageHeight() {
    return this.getType()
        .modes()
        .get(this.getMode())
        .image().tileHeight();
  }

  /**
   * Gets the number of frames for the current entity mode sprite.
   */
  public int getEntityFrameNumber() {
    return this.getType()
        .modes()
        .get(this.getMode())
        .image().tilesToCycle();
  }

  /**
   * Gets the animation speed for the current entity
   */
  public double getAnimationSpeed(){
    return this.getType()
            .modes()
            .get(this.getMode())
            .image().animationSpeed();
  }
}
