package oogasalad.authoring.view;

import java.util.HashMap;
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
public class EntityTypeEditorView extends VBox {

  private final TextField typeField;
  private final ComboBox<String> controlTypeBox;
  private final VBox modeList;
  private final AuthoringController controller;
  private EntityType current;
  private Button addModeButton;

  /**
   * Edit parameters for an entityType
   *
   * @param controller Wires with model
   */
  public EntityTypeEditorView(AuthoringController controller) {
    this.controller = controller;
    this.setSpacing(10);
    this.setPadding(new Insets(10));
    this.getStyleClass().add("entity-editor-view");

    typeField = new TextField();
    controlTypeBox = new ComboBox<>();
    // TODO: Remove hardcoded values
    controlTypeBox.getItems().addAll("Keyboard", "FollowMouse", "TargetEntity", "BFS");
    modeList = new VBox(5);

    addModeButton = new Button("+ Add Mode");
    addModeButton.setOnAction(e -> openAddModeDialog());

    this.getChildren().addAll(
        new Label("Entity Type:"), typeField,
        new Label("Control Strategy:"), controlTypeBox,
        new Label("Modes:"), modeList,
        addModeButton
    );
  }

  /**
   * Set's entity type
   *
   * @param type of type EntityType
   */
  public void setEntityType(EntityType type) {
    this.current = type;
    if (type == null) {
      return;
    }

    typeField.setText(type.type());
    typeField.setOnAction(e -> commitChanges());
    typeField.focusedProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal) {
        commitChanges(); // when field loses focus
      }
    });

    controlTypeBox.setValue(type.controlType());
    controlTypeBox.setOnAction(e -> commitChanges());

    modeList.getChildren().clear();
    for (Map.Entry<String, ModeConfig> entry : type.modes().entrySet()) {
      String modeName = entry.getKey();
      ModeConfig config = entry.getValue();
      Label label = new Label(modeName);
      Button editButton = new Button("Edit");
      editButton.setOnAction(e -> openEditModeDialog(modeName, config));
      modeList.getChildren().addAll(label, editButton);
    }
  }

  private void commitChanges() {
    if (current != null) {
      EntityType newEntity = new EntityType(typeField.getText(), controlTypeBox.getValue(), current.effect(),
          current.modes(), current.blocks(), current.strategyConfig());
      controller.getModel().updateEntityType(current.type(), newEntity);

      controller.updateEntitySelector(); // refresh tile labels if needed
      current = newEntity;
    }
  }

  private void openAddModeDialog() {
    ModeEditorDialog dialog = new ModeEditorDialog();
    dialog.showAndWait().ifPresent(config -> {
      String modeName = config.getModeName();
      if (!modeName.isEmpty() && !current.modes().containsKey(modeName)) {
        current.modes().put(modeName, config);
        setEntityType(current);
      } else {
        showError("Invalid or duplicate mode name.");
      }
    });
  }

  private void openEditModeDialog(String modeName, ModeConfig oldConfig) {
    ModeEditorDialog dialog = new ModeEditorDialog(oldConfig);
    dialog.showAndWait().ifPresent(newConfig -> {
      if (!modeName.equals(newConfig.getModeName())) {
        // Mode name changed → remove old key and insert new one
        current.modes().remove(modeName);
      }
      current.modes().put(newConfig.getModeName(), newConfig);
      setEntityType(current);
      controller.updateEntitySelector();
      controller.updateCanvas();
    });
  }

  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.showAndWait();
  }


}
