package oogasalad.authoring.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.model.EntityType;

import java.util.Map;
import oogasalad.engine.config.ModeConfig;
import oogasalad.engine.model.controlConfig.ControlConfig;

/**
 * View for editing a selected EntityType.
 *
 * @author Will He, Ishan Madan, Angela Predolac
 */
public class EntityTypeEditorView {

  private final VBox root;
  private final TextField typeField;
  private final ComboBox<String> controlTypeBox;
  private final VBox modeList;
  private final AuthoringController controller;
  private EntityType current;
  private Button deleteButton;

  /**
   * Edit parameters for an entityType
   *
   * @param controller Wires with model
   */
  public EntityTypeEditorView(AuthoringController controller) {
    this.controller = controller;

    root = new VBox();
    root.setSpacing(10);
    root.setPadding(new Insets(10));
    root.getStyleClass().add("entity-editor-view");

    typeField = new TextField();
    controlTypeBox = new ComboBox<>();
    // TODO: Remove hardcoded values
    controlTypeBox.getItems().addAll("Keyboard", "FollowMouse", "TargetEntity", "BFS");
    modeList = new VBox(5);

    Button addModeButton = new Button(LanguageManager.getMessage("ADD_MODE"));
    addModeButton.setOnAction(e -> openAddModeDialog());

    deleteButton = new Button("Delete Entity Type");
    deleteButton.getStyleClass().add("delete-button");
    deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
    deleteButton.setOnAction(e -> deleteEntityType());

    root.getChildren().addAll(
        new Label(LanguageManager.getMessage("ENTITY_TYPE")), typeField,
        new Label(LanguageManager.getMessage("CONTROL_STRATEGY")), controlTypeBox,
        new Label(LanguageManager.getMessage("MODES")), modeList,
        addModeButton,
            new Separator(), deleteButton
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

    // TODO: New config
//    controlTypeBox.setValue();
    controlTypeBox.setOnAction(e -> commitChanges());

    modeList.getChildren().clear();
    for (Map.Entry<String, ModeConfig> entry : type.modes().entrySet()) {
      String modeName = entry.getKey();
      ModeConfig config = entry.getValue();
      Label label = new Label(modeName);
      Button editButton = new Button(LanguageManager.getMessage("EDIT"));
      editButton.setOnAction(e -> openEditModeDialog(modeName, config));
      modeList.getChildren().addAll(label, editButton);
    }
  }

  /**
   * Returns the root JavaFX node of this view
   *
   * @return the root node
   */
  public Parent getRoot() {
    return root;
  }

  private void commitChanges() {
    if (current == null) return;

    ControlConfig controlConfig = buildControlConfigFromUI(); // dynamically builds appropriate config

    EntityType newEntity = new EntityType(
        typeField.getText(),
        controlConfig,
        current.modes(),
        current.blocks()
    );

    controller.getModel().updateEntityType(current.type(), newEntity);
    controller.updateEntitySelector(); // update visuals if needed
    current = newEntity;
  }

  private ControlConfig buildControlConfigFromUI() {
    // TODO: Implement

    return null;
  }


  private void openAddModeDialog() {
    ModeEditorDialog dialog = new ModeEditorDialog();
    dialog.showAndWait().ifPresent(config -> {
      String modeName = config.name();
      if (!modeName.isEmpty() && !current.modes().containsKey(modeName)) {
        current.modes().put(modeName, config);
        setEntityType(current);
      } else {
        showError(LanguageManager.getMessage("INVALID_OR_DUPLICATE_MODE"));
      }
    });
  }

  private void openEditModeDialog(String modeName, ModeConfig oldConfig) {
    ModeEditorDialog dialog = new ModeEditorDialog(oldConfig);
    dialog.showAndWait().ifPresent(newConfig -> {
      if (!modeName.equals(newConfig.name())) {
        // Mode name changed → remove old key and insert new one
        current.modes().remove(modeName);
      }
      current.modes().put(newConfig.name(), newConfig);
      setEntityType(current);
      controller.updateEntitySelector();
      controller.updateCanvas();
    });
  }

  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.showAndWait();
  }

  private void deleteEntityType() {
    if (current == null) return;

    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle(LanguageManager.getMessage("CONFIRM_DELETE"));
    confirm.setHeaderText(LanguageManager.getMessage("DELETE_ENTITY_TYPE"));
    confirm.setContentText(LanguageManager.getMessage("CONFIRM_DELETE_ENTITY_TYPE_MESSAGE"));

    if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
      controller.deleteEntityType(current.type());
    }
  }

}
