package oogasalad.engine.config;

/**
 * Represents a specific configuration mode for an entity, such as appearance and speed.
 * For example, this can define how an entity behaves and looks when it is in "PoweredUp" mode.
 */
public class ModeConfig {

  private String modeName;
  private int movementSpeed;
  private String imagePath;

  /**
   * Returns the movement speed associated with this mode.
   *
   * @return the movement speed value
   */
  public int getMovementSpeed() {
    return movementSpeed;
  }

  /**
   * Sets the movement speed for this mode.
   *
   * @param movementSpeed the new movement speed value
   */
  public void setMovementSpeed(int movementSpeed) {
    this.movementSpeed = movementSpeed;
  }

  /**
   * Returns the file path to the image used for this mode.
   *
   * @return the image file path as a string
   */
  public String getImagePath() {
    return imagePath;
  }

  /**
   * Sets the image file path for this mode.
   *
   * @param imagePath the image file path string
   */
  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  /**
   * Get mode name
   * @return mode name
   */
  public String getModeName() {
    return modeName;
  }

  /**
   * Set mode name
   * @param modeName name of mode
   */
  public void setModeName(String modeName) {
    this.modeName = modeName;
  }
}
