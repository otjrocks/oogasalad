package oogasalad.engine.config;

/**
 * ModeConfig defines a visual and movement variant for an entity, e.g., Pacman in PoweredUp mode.
 */
public class ModeConfig {

  private int movementSpeed;
  private String imagePath;

  public ModeConfig() {}

  public int getMovementSpeed() {
    return movementSpeed;
  }

  public void setMovementSpeed(int movementSpeed) {
    this.movementSpeed = movementSpeed;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }
}
