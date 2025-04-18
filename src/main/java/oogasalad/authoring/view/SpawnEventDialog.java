package oogasalad.authoring.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.records.config.model.SpawnEventRecord;
import oogasalad.engine.records.model.ConditionRecord;
import oogasalad.engine.records.model.EntityTypeRecord;

import java.util.*;
import java.util.function.Consumer;

public class SpawnEventDialog extends Stage {

  private final LevelDraft level;
  private final Map<String, EntityTypeRecord> entityTypes;

  private final ComboBox<String> entityTypeDropdown = new ComboBox<>();
  private final ComboBox<String> modeDropdown = new ComboBox<>();
  private final TextField xField = new TextField();
  private final TextField yField = new TextField();

  private final ComboBox<String> spawnConditionTypeDropdown = new ComboBox<>();
  private final VBox spawnConditionParamsBox = new VBox(5);

  private final CheckBox hasDespawnCondition = new CheckBox("Enable Despawn Condition");
  private final ComboBox<String> despawnConditionTypeDropdown = new ComboBox<>();
  private final VBox despawnConditionParamsBox = new VBox(5);

  private final TableView<SpawnEventRecord> table = new TableView<>();

  public SpawnEventDialog(Map<String, EntityTypeRecord> entityTypes, LevelDraft level) {
    this.entityTypes = entityTypes;
    this.level = level;

    setTitle("Edit Spawn Events");
    initModality(Modality.APPLICATION_MODAL);
    setResizable(false);

    VBox root = new VBox(10);
    root.setPadding(new Insets(10));

    HBox row1 = new HBox(10, new Label("Entity Type:"), entityTypeDropdown);
    HBox row2 = new HBox(10, new Label("Mode:"), modeDropdown);
    HBox row3 = new HBox(10, new Label("X:"), xField, new Label("Y:"), yField);
    HBox row4 = new HBox(10, new Label("Spawn Condition Type:"), spawnConditionTypeDropdown);
    HBox row5 = new HBox(10, hasDespawnCondition);
    HBox row6 = new HBox(10, new Label("Despawn Condition Type:"), despawnConditionTypeDropdown);

    entityTypeDropdown.getItems().addAll(entityTypes.keySet());
    entityTypeDropdown.setOnAction(e -> updateModes());

    spawnConditionTypeDropdown.getItems().addAll("TimeElapsed", "ScoreBased");
    spawnConditionTypeDropdown.setValue("TimeElapsed");
    spawnConditionTypeDropdown.setOnAction(e -> renderConditionUI(spawnConditionTypeDropdown, spawnConditionParamsBox));
    renderConditionUI(spawnConditionTypeDropdown, spawnConditionParamsBox);

    hasDespawnCondition.setOnAction(e -> toggleDespawnControls());
    despawnConditionTypeDropdown.getItems().addAll("TimeElapsed", "ScoreBased");
    despawnConditionTypeDropdown.setOnAction(e -> renderConditionUI(despawnConditionTypeDropdown, despawnConditionParamsBox));
    toggleDespawnControls();

    Button addButton = new Button("Add Spawn Event");
    addButton.setOnAction(e -> addSpawnEvent());

    Button deleteButton = new Button("Delete Selected");
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
        makeColumn("Entity", r -> r.entityType().type()),
        makeColumn("Mode", SpawnEventRecord::mode),
        makeColumn("Spawn Cond", r -> r.spawnCondition().type()),
        makeColumn("Despawn Cond", r -> Optional.ofNullable(r.despawnCondition()).map(ConditionRecord::type).orElse(""))
    );

    root.getChildren().addAll(
        row1, row2, row3,
        row4, spawnConditionParamsBox,
        row5, row6, despawnConditionParamsBox,
        new HBox(10, addButton, deleteButton),
        new Label("Current Spawn Events:"), table
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
    despawnConditionTypeDropdown.setDisable(!hasDespawnCondition.isSelected());
    despawnConditionParamsBox.setDisable(!hasDespawnCondition.isSelected());
    if (hasDespawnCondition.isSelected()) {
      despawnConditionTypeDropdown.setValue("TimeElapsed");
      renderConditionUI(despawnConditionTypeDropdown, despawnConditionParamsBox);
    }
  }

  private void renderConditionUI(ComboBox<String> typeDropdown, VBox paramBox) {
    paramBox.getChildren().clear();
    String type = typeDropdown.getValue();
    if (type.equals("TimeElapsed") || type.equals("ScoreBased")) {
      Label label = new Label("Amount:");
      TextField field = new TextField();
      field.setUserData("amount");
      paramBox.getChildren().addAll(label, field);
    }
  }

  private Map<String, Object> extractParameters(VBox paramBox) {
    Map<String, Object> map = new HashMap<>();
    for (Node node : paramBox.getChildren()) {
      if (node instanceof TextField field && "amount".equals(field.getUserData())) {
        map.put("amount", Double.parseDouble(field.getText()));
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
      SpawnEventRecord event = new SpawnEventRecord(entity, spawnCondition, x, y, mode, despawnCondition);
      level.getSpawnEvents().add(event);
      refreshTable();
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Invalid input: " + e.getMessage()).showAndWait();
    }
  }

  private void refreshTable() {
    table.getItems().setAll(level.getSpawnEvents());
  }

  private TableColumn<SpawnEventRecord, String> makeColumn(String title, java.util.function.Function<SpawnEventRecord, String> mapper) {
    TableColumn<SpawnEventRecord, String> col = new TableColumn<>(title);
    col.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(mapper.apply(data.getValue())));
    return col;
  }
}
