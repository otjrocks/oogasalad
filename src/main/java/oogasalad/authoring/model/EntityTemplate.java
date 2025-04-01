package oogasalad.authoring.model;

/**
 * A template to store information about an Entity.
 *
 * @author Angela
 */
public class EntityTemplate {

  private String type;
  private String imagePath;
  private String controlType;
  private String effectStrategy;

  /**
   * Create an Entity template.
   *
   * @param type           The type of the Entity.
   * @param imagePath      The image path of the Entity.
   * @param controlType    The control type of the Entity.
   * @param effectStrategy The effect strategy for this Entity.
   */
  public EntityTemplate(String type, String imagePath, String controlType, String effectStrategy) {
    this.type = type;
    this.imagePath = imagePath;
    this.controlType = controlType;
    this.effectStrategy = effectStrategy;
  }

  /**
   * Get the type.
   *
   * @return The type for this entity.
   */
  public String getType() {
    return type;
  }

  /**
   * Set the type for this Entity.
   *
   * @param type The type you wish to set.
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Get the image directory path.
   *
   * @return The path of this image.
   */
  public String getImagePath() {
    return imagePath;
  }

  /**
   * Set the image directory path.
   *
   * @param imagePath The image's directory path.
   */
  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  /**
   * Get the control type of this entity as a String.
   *
   * @return The string representing the control type.
   */
  public String getControlType() {
    return controlType;
  }

  /**
   * Set the control type string value.
   *
   * @param controlType The control type string.
   */
  public void setControlType(String controlType) {
    this.controlType = controlType;
  }

  /**
   * Get the effect strategy for this entity template.
   *
   * @return The string representing the effect strategy.
   */
  public String getEffectStrategy() {
    return effectStrategy;
  }

  /**
   * Set the string representing this entity's effect.
   *
   * @param effectStrategy The string you wish to set for the effect strategy.
   */
  public void setEffectStrategy(String effectStrategy) {
    this.effectStrategy = effectStrategy;
  }

  @Override
  public String toString() {
    return "EntityTemplate{" +
        "type='" + type + '\'' +
        ", imagePath='" + imagePath + '\'' +
        ", controlType='" + controlType + '\'' +
        ", effectStrategy='" + effectStrategy + '\'' +
        '}';
  }
}
