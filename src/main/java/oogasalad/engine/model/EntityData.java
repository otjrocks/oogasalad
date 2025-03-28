package oogasalad.engine.model;

import java.util.Map;

public class EntityData {
  private String type;
  private String imagePath;
  private String controlType;
  private String effect;
  private double initialX;
  private double initialY;
  private Map<String, String> tags;

  public EntityData() {}

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public String getControlType() {
    return controlType;
  }

  public void setControlType(String controlType) {
    this.controlType = controlType;
  }

  public double getInitialX() {
    return initialX;
  }

  public void setInitialX(double initialX) {
    this.initialX = initialX;
  }

  public String getEffect() {
    return effect;
  }

  public void setEffect(String effect) {
    this.effect = effect;
  }

  public double getInitialY() {
    return initialY;
  }

  public void setInitialY(double initialY) {
    this.initialY = initialY;
  }

  public Map<String, String> getTags() {
    return tags;
  }

  public void setTags(Map<String, String> tags) {
    this.tags = tags;
  }
}
