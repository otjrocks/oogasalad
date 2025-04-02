package oogasalad.authoring.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.config.ModeConfig;

import java.util.Map;

/**
 * View for editing a selected EntityType.
 *
 * @author Will He
 */
public class EntityEditorView extends VBox {

  private final TextField typeField;
  private final ComboBox<String> controlTypeBox;
  private final VBox modeList;
  private final AuthoringController controller;
  private EntityType current;

  /**
   * Edit parameters for an entityType
   * @param controller Wires with model
   */
  public EntityEditorView(AuthoringController controller) {
    this.controller = controller;
    this.setSpacing(10);
    this.setPadding(new Insets(10));
    this.getStyleClass().add("entity-editor-view");

    typeField = new TextField();
    controlTypeBox = new ComboBox<>();
    controlTypeBox.getItems().addAll("Keyboard", "FollowMouse", "TargetEntity", "BFS");
    modeList = new VBox(5);

    this.getChildren().addAll(
        new Label("Entity Type:"), typeField,
        new Label("Control Strategy:"), controlTypeBox,
        new Label("Modes:"), modeList
    );
  }

  /**
   * Set's entity type
   * @param type of type EntityType
   */
  public void setEntityType(EntityType type) {
    this.current = type;
    if (type == null) return;

    typeField.setText(type.getType());
    typeField.setOnAction(e -> commitChanges());
    typeField.focusedProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal) commitChanges(); // when field loses focus
    });

    controlTypeBox.setValue(type.getControlType());

    modeList.getChildren().clear();
    for (Map.Entry<String, ModeConfig> entry : type.getModes().entrySet()) {
      String modeName = entry.getKey();
      ModeConfig config = entry.getValue();
      Label label = new Label(modeName + ": " + config.getImagePath() + ", speed=" + config.getMovementSpeed());
      modeList.getChildren().add(label);
    }
  }

  /**
   * Getter for entityType
   * @return entity Type
   */
  public EntityType getEditedEntityType() {
    current.setType(typeField.getText());
    current.setControlType(controlTypeBox.getValue());
    return current;
  }

  private void commitChanges() {
    if (current != null) {
      current.setType(typeField.getText());
      controller.updateEntitySelector(); // refresh tile labels if needed
    }
  }

}
