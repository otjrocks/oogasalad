package oogasalad.engine.config;

/**
 * Represents a specific configuration mode for an entity, such as appearance and speed.
 * For example, this can define how an entity behaves and looks when it is in "PoweredUp" mode.
 */
public class ModeConfig {

  private int movementSpeed;
  private String imagePath;

  /**
   * Constructs a ModeConfig object with the specified movement speed and image path.
   *
   * @param movementSpeed the speed of movement, which will be converted to an integer
   * @param imagePath the file path to the image associated with this mode
   */
  public ModeConfig(Double movementSpeed, String imagePath) {
    this.movementSpeed = movementSpeed.intValue();
    this.imagePath = imagePath;
  }

  /**
   * Default constructor necessary for AuthoringController
   */
  public ModeConfig() {
    // needs to be here
  }

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
}
