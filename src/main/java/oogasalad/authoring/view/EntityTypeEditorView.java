package oogasalad.authoring.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.records.config.ModeConfigRecord;
import oogasalad.engine.records.model.EntityTypeRecord;
import oogasalad.engine.utility.LanguageManager;

/**
 * View for editing a selected EntityType.
 *
 * @author Will He, Ishan Madan, Angela Predolac, Owen Jennings
 */
public class EntityTypeEditorView {

  private final VBox root;
  private final TextField typeField;
  private final VBox modeList;
  private final AuthoringController controller;
  private EntityTypeRecord current;
  private final VBox blocksList;


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

    modeList = new VBox(5);

    Button addModeButton = new Button(LanguageManager.getMessage("ADD_MODE"));
    addModeButton.setOnAction(e -> openAddModeDialog());

    Button deleteButton = new Button(LanguageManager.getMessage("DELETE_ENTITY_TYPE"));
    deleteButton.getStyleClass().add("delete-button");
    deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
    deleteButton.setOnAction(e -> deleteEntityType());

    blocksList = new VBox(5);
    blocksList.setPadding(new Insets(5));

    ScrollPane blocksScrollPane = new ScrollPane(blocksList);
    blocksScrollPane.setFitToWidth(true);
    blocksScrollPane.setMinHeight(100);

    VBox blocksSection = new VBox(5,
        new Label(LanguageManager.getMessage("BLOCKS_OTHER_ENTITIES")),
        blocksScrollPane
    );

    root.getChildren().addAll(
        new Label(LanguageManager.getMessage("ENTITY_TYPE")), typeField,
        blocksSection,
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
  public void setEntityType(EntityTypeRecord type) {
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

    populateBlocksList(this.current);

    // Refresh mode list UI
    modeList.getChildren().clear();
    for (Map.Entry<String, ModeConfigRecord> entry : type.modes().entrySet()) {
      String modeName = entry.getKey();
      ModeConfigRecord config = entry.getValue();
      Label label = new Label(modeName);
      Button editButton = new Button(LanguageManager.getMessage("EDIT"));
      editButton.setOnAction(e -> openEditModeDialog(modeName, config));
      modeList.getChildren().addAll(label, editButton);
    }
  }

  private void populateBlocksList(EntityTypeRecord type) {
    blocksList.getChildren().clear();

    String currentName = type.type();
    Map<String, EntityTypeRecord> allTypes = controller.getModel().getEntityTypeMap();

    for (String otherType : allTypes.keySet()) {
      if (otherType.equals(currentName)) {
        continue;
      }

      CheckBox box = new CheckBox(otherType);
      box.setSelected(type.blocks() != null && type.blocks().contains(otherType));
      box.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> commitChanges());

      blocksList.getChildren().add(box);
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
    if (current == null) {
      return;
    }

    List<String> selectedBlocks = blocksList.getChildren().stream()
        .filter(node -> node instanceof CheckBox)
        .map(node -> (CheckBox) node)
        .filter(CheckBox::isSelected)
        .map(CheckBox::getText)
        .toList();

    EntityTypeRecord newEntity = new EntityTypeRecord(
        typeField.getText(),
        current.modes(),
        selectedBlocks
    );

    controller.getModel().updateEntityType(current.type(), newEntity);
    controller.updateEntitySelector(); // update visuals if needed
    current = newEntity;
  }

  private void openAddModeDialog() {
    ModeEditorDialog dialog = new ModeEditorDialog();
    dialog.showAndWait().ifPresent(config -> {
      String modeName = config.name();

      if (modeName.isEmpty() || current.modes().containsKey(modeName)) {
        showError(LanguageManager.getMessage("INVALID_OR_DUPLICATE_MODE"));
        return;
      }

      // Create a new map with the added mode
      Map<String, ModeConfigRecord> newModes = new HashMap<>(current.modes());
      newModes.put(modeName, config);

      // Replace current with new EntityType (workaround for record immutability)
      current = new EntityTypeRecord(
          current.type(),
          newModes,
          current.blocks()
      );

      setEntityType(current); // this pushes it wherever it needs to go
      controller.updateEntitySelector();
      controller.updateCanvas();
      commitChanges();
    });
  }


  private void openEditModeDialog(String oldName, ModeConfigRecord oldConfig) {
    ModeEditorDialog dialog = new ModeEditorDialog(oldConfig);
    dialog.showAndWait().ifPresent(newConfig -> {
      Map<String, ModeConfigRecord> newModes = new HashMap<>(current.modes());

      boolean renamed = !oldName.equals(newConfig.name());
      if (renamed) {
        newModes.remove(oldName);
      }
      newModes.put(newConfig.name(), newConfig);

      // 1. Create updated EntityType
      EntityTypeRecord updated = new EntityTypeRecord(
          current.type(),
          newModes,
          current.blocks()
      );

      // 2. Replace in model
      controller.getModel().addEntityType(updated);
      controller.getModel().getCurrentLevel()
          .refreshEntityTypes(controller.getModel().getEntityTypeMap());

      // 3. Update placements to reflect renamed mode
      if (renamed) {
        controller.getModel()
            .getCurrentLevel()
            .updateModeName(updated.type(), oldName, newConfig.name());
      }

      // 4. Update UI
      current = updated; // update local cache
      setEntityType(updated);
      controller.updateEntitySelector();
      controller.updateCanvas();
      commitChanges();
    });
  }


  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.showAndWait();
  }

  private void deleteEntityType() {
    if (current == null) {
      return;
    }

    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle(LanguageManager.getMessage("CONFIRM_DELETE"));
    confirm.setHeaderText(LanguageManager.getMessage("DELETE_ENTITY_TYPE"));
    confirm.setContentText(LanguageManager.getMessage("CONFIRM_DELETE_ENTITY_TYPE_MESSAGE"));

    if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
      controller.deleteEntityType(current.type());
    }
  }
}
