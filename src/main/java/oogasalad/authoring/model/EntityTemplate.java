package oogasalad.authoring.model;

public class EntityTemplate {
    private String type;
    private String imagePath;
    private String controlType;
    private String effectStrategy;

    public EntityTemplate(String type, String imagePath, String controlType, String effectStrategy) {
        this.type = type;
        this.imagePath = imagePath;
        this.controlType = controlType;
        this.effectStrategy = effectStrategy;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getControlType() { return controlType; }
    public void setControlType(String controlType) { this.controlType = controlType; }

    public String getEffectStrategy() { return effectStrategy; }
    public void setEffectStrategy(String effectStrategy) { this.effectStrategy = effectStrategy; }

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
