package oogasalad.authoring.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.records.model.ConditionRecord;
import oogasalad.engine.records.model.EntityTypeRecord;
import oogasalad.engine.records.model.ModeChangeEventRecord;

import java.util.*;

public class ModeChangeEventDialog extends Stage {

  private final LevelDraft level;
  private final Map<String, EntityTypeRecord> entityTypes;

  private final ComboBox<String> entityTypeDropdown = new ComboBox<>();
  private final ComboBox<String> currentModeDropdown = new ComboBox<>();
  private final ComboBox<String> nextModeDropdown = new ComboBox<>();
  private final ComboBox<String> conditionTypeDropdown = new ComboBox<>();
  private final VBox conditionParamsBox = new VBox(5);
  private final TableView<ModeChangeEventRecord> table = new TableView<>();

  public ModeChangeEventDialog(Map<String, EntityTypeRecord> entityTypeMap, LevelDraft level) {
    this.entityTypes = entityTypeMap;
    this.level = level;

    setTitle("Edit Mode Change Events");
    initModality(Modality.APPLICATION_MODAL);
    setResizable(false);

    VBox root = new VBox(10);
    root.setPadding(new Insets(10));

    HBox formRow1 = new HBox(10, new Label("Entity Type:"), entityTypeDropdown);
    HBox formRow2 = new HBox(10, new Label("Current Mode:"), currentModeDropdown);
    HBox formRow3 = new HBox(10, new Label("Next Mode:"), nextModeDropdown);
    HBox formRow4 = new HBox(10, new Label("Condition Type:"), conditionTypeDropdown);

    populateEntityTypeDropdown();

    conditionTypeDropdown.getItems().add("time");
    conditionTypeDropdown.setValue("time");

    conditionTypeDropdown.setOnAction(e -> renderConditionParams());

    renderConditionParams(); // initial

    Button addButton = new Button("Add Event");
    addButton.setOnAction(e -> addModeChangeEvent());

    Button deleteButton = new Button("Delete Selected");
    deleteButton.setOnAction(e -> {
      ModeChangeEventRecord selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        level.getModeChangeEvents().remove(selected);
        refreshTable();
      }
    });

    table.setPrefHeight(200);
    table.getItems().addAll(level.getModeChangeEvents());
    table.getColumns().addAll(
        makeColumn("Entity", r -> r.entityType().type()),
        makeColumn("From", ModeChangeEventRecord::currentMode),
        makeColumn("To", ModeChangeEventRecord::nextMode),
        makeColumn("Condition", r -> r.changeCondition().type() + ": " + r.changeCondition().parameters())
    );

    root.getChildren().addAll(
        formRow1, formRow2, formRow3, formRow4,
        conditionParamsBox,
        new HBox(10, addButton, deleteButton),
        new Label("Current Events:"),
        table
    );

    Scene scene = new Scene(root);
    setScene(scene);
  }

  private void populateEntityTypeDropdown() {
    entityTypeDropdown.getItems().addAll(entityTypes.keySet());
    entityTypeDropdown.setOnAction(e -> {
      String selected = entityTypeDropdown.getValue();
      if (selected != null && entityTypes.containsKey(selected)) {
        Set<String> modes = entityTypes.get(selected).modes().keySet();
        currentModeDropdown.getItems().setAll(modes);
        nextModeDropdown.getItems().setAll(modes);
      }
    });
  }

  private void renderConditionParams() {
    conditionParamsBox.getChildren().clear();


    // TODO: This shouldn't be an if statement
    String type = conditionTypeDropdown.getValue();
    if ("time".equals(type)) {
      Label label = new Label("Amount (seconds):");
      TextField amountField = new TextField();
      amountField.setId("amountField");
      conditionParamsBox.getChildren().addAll(label, amountField);
    }

    // Easily extensible for more types later
  }

  private void addModeChangeEvent() {
    String entityTypeKey = entityTypeDropdown.getValue();
    String currentMode = currentModeDropdown.getValue();
    String nextMode = nextModeDropdown.getValue();
    String conditionType = conditionTypeDropdown.getValue();

    if (entityTypeKey == null || currentMode == null || nextMode == null) {
      showAlert("Please fill out all fields before adding.");
      return;
    }

    Map<String, Object> params = new HashMap<>();
    if ("time".equals(conditionType)) {
      TextField amountField = (TextField) conditionParamsBox.lookup("#amountField");
      try {
        int amount = Integer.parseInt(amountField.getText());
        params.put("amount", amount);
      } catch (NumberFormatException e) {
        showAlert("Amount must be a number.");
        return;
      }
    }

    ConditionRecord condition = new ConditionRecord(conditionType, params);
    EntityTypeRecord entityType = entityTypes.get(entityTypeKey);

    ModeChangeEventRecord event = new ModeChangeEventRecord(
        new EntityTypeRecord(
            entityType.type(),
            null,           // controlConfig (not needed for mode events)
            Collections.emptyMap(),     // modes
            Collections.emptyList(),    // blocks
            0.0                         // speed
        ),
        currentMode,
        nextMode,
        condition
    );

    level.getModeChangeEvents().add(event);
    refreshTable();
  }

  private void refreshTable() {
    table.getItems().setAll(level.getModeChangeEvents());
  }

  private TableColumn<ModeChangeEventRecord, String> makeColumn(String title, java.util.function.Function<ModeChangeEventRecord, String> mapper) {
    TableColumn<ModeChangeEventRecord, String> col = new TableColumn<>(title);
    col.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(mapper.apply(data.getValue())));
    return col;
  }

  private void showAlert(String msg) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Warning");
    alert.setHeaderText(null);
    alert.setContentText(msg);
    alert.showAndWait();
  }
}
