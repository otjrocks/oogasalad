package oogasalad.authoring.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.authoring.view.mainView.AlertUtil;
import oogasalad.engine.records.config.model.SpawnEventRecord;
import oogasalad.engine.records.model.ConditionRecord;
import oogasalad.engine.records.model.EntityTypeRecord;

import java.util.*;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.ThemeManager;
import oogasalad.engine.view.components.FormattingUtil;

/**
 * A dialog window for editing spawn events within a level. Allows the user to configure entities
 * that spawn under specific conditions, along with optional despawn conditions.
 */
public class SpawnEventDialog extends Stage {

  private static final String CONDITION_TIME_ELAPSED = "TimeElapsed";
  private static final String CONDITION_SCORE_BASED = "ScoreBased";
  private static final String PARAM_AMOUNT = "amount";

  private final LevelDraft level;
  private final Map<String, EntityTypeRecord> entityTypes;

  private final ComboBox<String> entityTypeDropdown = new ComboBox<>();
  private final ComboBox<String> modeDropdown = new ComboBox<>();
  private final TextField xField = FormattingUtil.createTextField();
  private final TextField yField = FormattingUtil.createTextField();

  private final ComboBox<String> spawnConditionTypeDropdown = new ComboBox<>();
  private final VBox spawnConditionParamsBox = new VBox(5);

  private final CheckBox hasDespawnCondition = new CheckBox(
      LanguageManager.getMessage("ENABLE_DESPAWN_CONDITION"));
  private final ComboBox<String> despawnConditionTypeDropdown = new ComboBox<>();
  private final VBox despawnConditionParamsBox = new VBox(5);

  private final TableView<SpawnEventRecord> table = new TableView<>();

  /**
   * Constructs a dialog for adding and editing spawn events tied to a level.
   *
   * @param entityTypes a map of all entity types available
   * @param level       the current level draft that holds the spawn events
   */
  public SpawnEventDialog(Map<String, EntityTypeRecord> entityTypes, LevelDraft level) {
    this.entityTypes = entityTypes;
    this.level = level;

    setTitle(LanguageManager.getMessage("EDIT_SPAWN_EVENTS"));
    initModality(Modality.APPLICATION_MODAL);
    setResizable(false);

    VBox root = new VBox(10);
    root.setPadding(new Insets(10));

    HBox row1 = new HBox(10, new Label(LanguageManager.getMessage("ENTITY_TYPE")),
        entityTypeDropdown);
    HBox row2 = new HBox(10, new Label(LanguageManager.getMessage("MODE")), modeDropdown);
    HBox row3 = new HBox(10, new Label(LanguageManager.getMessage("X")), xField,
        new Label(LanguageManager.getMessage("Y")), yField);
    HBox row4 = new HBox(10, new Label(LanguageManager.getMessage("SPAWN_CONDITION_TYPE")),
        spawnConditionTypeDropdown);
    HBox row5 = new HBox(10, hasDespawnCondition);
    HBox row6 = new HBox(10, new Label(LanguageManager.getMessage("DESPAWN_CONDITION_TYPE")),
        despawnConditionTypeDropdown);

    entityTypeDropdown.getItems().addAll(entityTypes.keySet());
    entityTypeDropdown.setOnAction(e -> updateModes());

    spawnConditionTypeDropdown.getItems().addAll(CONDITION_TIME_ELAPSED, CONDITION_SCORE_BASED);
    spawnConditionTypeDropdown.setValue(CONDITION_TIME_ELAPSED);
    spawnConditionTypeDropdown.setOnAction(
        e -> renderConditionUI(spawnConditionTypeDropdown, spawnConditionParamsBox));
    renderConditionUI(spawnConditionTypeDropdown, spawnConditionParamsBox);

    hasDespawnCondition.setOnAction(e -> toggleDespawnControls());
    despawnConditionTypeDropdown.getItems().addAll(CONDITION_TIME_ELAPSED, CONDITION_SCORE_BASED);
    despawnConditionTypeDropdown.setOnAction(
        e -> renderConditionUI(despawnConditionTypeDropdown, despawnConditionParamsBox));
    toggleDespawnControls();

    Button addButton = new Button(LanguageManager.getMessage("ADD_SPAWN_EVENT"));
    addButton.setOnAction(e -> addSpawnEvent());

    Button deleteButton = new Button(LanguageManager.getMessage("DELETE_SELECTED"));
    deleteButton.setOnAction(e -> {
      SpawnEventRecord selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        level.getSpawnEvents().remove(selected);
        refreshTable();
      }
    });

    table.setPrefHeight(200);
    table.getItems().addAll(level.getSpawnEvents());
    table.getColumns().addAll(
        makeColumn(LanguageManager.getMessage("ENTITY"), r -> r.entityType().type()),
        makeColumn(LanguageManager.getMessage("MODE_LABEL"), SpawnEventRecord::mode),
        makeColumn(LanguageManager.getMessage("SPAWN_CONDITION"), r -> r.spawnCondition().type()),
        makeColumn(LanguageManager.getMessage("DESPAWN_CONDITION"),
            r -> Optional.ofNullable(r.despawnCondition()).map(ConditionRecord::type).orElse(""))
    );

    root.getChildren().addAll(
        row1, row2, row3,
        row4, spawnConditionParamsBox,
        row5, row6, despawnConditionParamsBox,
        new HBox(10, addButton, deleteButton),
        new Label(LanguageManager.getMessage("CURRENT_SPAWN_EVENTS")), table
    );

    setScene(new Scene(root));
  }

  private void updateModes() {
    String entityKey = entityTypeDropdown.getValue();
    if (entityKey != null) {
      modeDropdown.getItems().setAll(entityTypes.get(entityKey).modes().keySet());
    }
  }

  private void toggleDespawnControls() {
    boolean enabled = hasDespawnCondition.isSelected();
    despawnConditionTypeDropdown.setDisable(!enabled);
    despawnConditionParamsBox.setDisable(!enabled);
    if (enabled) {
      despawnConditionTypeDropdown.setValue(CONDITION_TIME_ELAPSED);
      renderConditionUI(despawnConditionTypeDropdown, despawnConditionParamsBox);
    }
  }

  // TODO: render by reflection, rather than hard code
  private void renderConditionUI(ComboBox<String> typeDropdown, VBox paramBox) {
    paramBox.getChildren().clear();
    String type = typeDropdown.getValue();
    if (CONDITION_TIME_ELAPSED.equals(type) || CONDITION_SCORE_BASED.equals(type)) {
      Label label = new Label(LanguageManager.getMessage("AMOUNT"));
      TextField field = FormattingUtil.createTextField();
      field.setUserData(PARAM_AMOUNT);
      paramBox.getChildren().addAll(label, field);
    }
  }

  private Map<String, Object> extractParameters(VBox paramBox) {
    Map<String, Object> map = new HashMap<>();
    for (Node node : paramBox.getChildren()) {
      if (node instanceof TextField field && PARAM_AMOUNT.equals(field.getUserData())) {
        map.put(PARAM_AMOUNT, Double.parseDouble(field.getText()));
      }
    }
    return map;
  }

  private void addSpawnEvent() {
    try {
      String entityKey = entityTypeDropdown.getValue();
      String mode = modeDropdown.getValue();
      double x = Double.parseDouble(xField.getText());
      double y = Double.parseDouble(yField.getText());
      String spawnType = spawnConditionTypeDropdown.getValue();
      Map<String, Object> spawnParams = extractParameters(spawnConditionParamsBox);

      ConditionRecord spawnCondition = new ConditionRecord(spawnType, spawnParams);
      ConditionRecord despawnCondition = null;
      if (hasDespawnCondition.isSelected()) {
        String despawnType = despawnConditionTypeDropdown.getValue();
        Map<String, Object> despawnParams = extractParameters(despawnConditionParamsBox);
        despawnCondition = new ConditionRecord(despawnType, despawnParams);
      }

      EntityTypeRecord entity = entityTypes.get(entityKey);
      SpawnEventRecord event = new SpawnEventRecord(entity, spawnCondition, x, y, mode,
          despawnCondition);
      level.getSpawnEvents().add(event);
      refreshTable();
    } catch (Exception e) {
      Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input: " + e.getMessage());
      FormattingUtil.applyStandardDialogStyle(alert);
      alert.showAndWait();
    }
  }

  private void refreshTable() {
    table.getItems().setAll(level.getSpawnEvents());
  }

  private TableColumn<SpawnEventRecord, String> makeColumn(String title,
      java.util.function.Function<SpawnEventRecord, String> mapper) {
    TableColumn<SpawnEventRecord, String> col = new TableColumn<>(title);
    col.setCellValueFactory(
        data -> new javafx.beans.property.SimpleStringProperty(mapper.apply(data.getValue())));
    return col;
  }
}
