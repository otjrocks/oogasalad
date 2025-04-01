package oogasalad.engine.model;

/**
 * A class to store all the pertinent information about an Entity
 *
 * @author Will He
 */
public class EntityData {

  private String type;
  private String imagePath;
  private String controlType;
  private String effect;
  private double initialX;
  private double initialY;

  /**
   * Get the type string for this entity.
   *
   * @return A string representing the type.
   */
  public String getType() {
    return type;
  }

  /**
   * Set the type of this entity.
   *
   * @param type The type of this entity as a string.
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Get the image path for this entity.
   *
   * @return A string representing the path.
   */
  public String getImagePath() {
    return imagePath;
  }

  /**
   * Set the image path.
   *
   * @param imagePath A string representing the path to the image.
   */
  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  /**
   * Get the control type as a string.
   *
   * @return The control type string.
   */
  public String getControlType() {
    return controlType;
  }

  /**
   * Set the control type for this entity.
   *
   * @param controlType The control type for the entity.
   */
  public void setControlType(String controlType) {
    this.controlType = controlType;
  }

  /**
   * Get the initial x coordinate for this entity.
   *
   * @return The double representing the x coordinate.
   */
  public double getInitialX() {
    return initialX;
  }

  /**
   * Set the initial x value for this entity.
   *
   * @param initialX A double representing the initial x value.
   */
  public void setInitialX(double initialX) {
    this.initialX = initialX;
  }

  /**
   * Get the string representing the effect of this entity.
   *
   * @return The string representation of the effect applied to this entity.
   */
  public String getEffect() {
    return effect;
  }

  /**
   * Set the effect for this entity.
   *
   * @param effect A string representing an effect.
   */
  public void setEffect(String effect) {
    this.effect = effect;
  }

  /**
   * Get the initial y for this entity.
   *
   * @return A double representing the y coordinate.
   */
  public double getInitialY() {
    return initialY;
  }

  /**
   * Set the initial y for this entity.
   *
   * @param initialY A double to represent the y coordinate.
   */
  public void setInitialY(double initialY) {
    this.initialY = initialY;
  }
}
