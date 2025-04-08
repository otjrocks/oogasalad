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
    addModeButton.setOnAction(e -> showAddModeDialog());

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

    modeList.getChildren().clear();
    for (Map.Entry<String, ModeConfig> entry : type.modes().entrySet()) {
      String modeName = entry.getKey();
      ModeConfig config = entry.getValue();
      Label label = new Label(
          modeName + ": " + config.getImagePath() + ", speed=" + config.getMovementSpeed());
      modeList.getChildren().add(label);
    }
  }

  private void commitChanges() {
    if (current != null) {
      current = new EntityType(typeField.getText(), current.controlType(), current.effect(),
          current.modes(), current.blocks(), current.strategyConfig());
      controller.updateEntitySelector(); // refresh tile labels if needed
    }
  }

  private void showAddModeDialog() {
    AddModeDialog dialog = new AddModeDialog();
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

  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.showAndWait();
  }


}
