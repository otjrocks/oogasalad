package oogasalad.authoring.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.authoring.view.util.StrategyLoader;
import oogasalad.engine.records.config.model.SpawnEventRecord;
import oogasalad.engine.records.model.ConditionRecord;
import oogasalad.engine.records.model.EntityTypeRecord;

import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.view.components.FormattingUtil;
import oogasalad.player.model.strategies.spawnevent.SpawnEventStrategyInterface;

/**
 * A dialog window for editing spawn events within a level. Allows the user to configure entities
 * that spawn under specific conditions, along with optional despawn conditions.
 */
public class SpawnEventDialog extends Stage {

  private static final Map<String, List<String>> CONDITION_PARAMETERS = Map.of(
      "TimeElapsed", List.of("amount"),
      "ScoreBased", List.of("amount"),
      "Always", List.of() // If you later have an AlwaysSpawn condition needing no parameters
  );

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

    // <-- your package
    // <-- the interface they implement
    Map<String, Class<?>> spawnConditions = StrategyLoader.loadStrategies(
        "oogasalad.player.model.strategies.spawnevent", // <-- your package
        SpawnEventStrategyInterface.class // <-- the interface they implement
    );


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

    spawnConditionTypeDropdown.getItems().addAll(spawnConditions.keySet());

    spawnConditionTypeDropdown.setOnAction(
        e -> renderConditionUI(spawnConditionTypeDropdown, spawnConditionParamsBox));
    renderConditionUI(spawnConditionTypeDropdown, spawnConditionParamsBox);

    hasDespawnCondition.setOnAction(e -> toggleDespawnControls());
    despawnConditionTypeDropdown.getItems().addAll(spawnConditions.keySet());

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

    table.getStyleClass().add("spawn-event-table");
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
      renderConditionUI(despawnConditionTypeDropdown, despawnConditionParamsBox);
    }
  }

  private void renderConditionUI(ComboBox<String> typeDropdown, VBox paramBox) {
    paramBox.getChildren().clear();

    String selectedType = typeDropdown.getValue();
    if (selectedType == null || !CONDITION_PARAMETERS.containsKey(selectedType)) {
      return;
    }

    List<String> params = CONDITION_PARAMETERS.get(selectedType);

    for (String param : params) {
      Label label = new Label(param);
      TextField field = FormattingUtil.createTextField();
      field.setId(param); // important: set ID = param name
      paramBox.getChildren().addAll(label, field);
    }
  }


  private Map<String, Object> extractParameters(VBox paramBox) {
    Map<String, Object> params = new HashMap<>();

    for (Node node : paramBox.getChildren()) {
      if (node instanceof TextField field && field.getId() != null) {
        String raw = field.getText();
        try {
          params.put(field.getId(), Integer.parseInt(raw)); // Default to parsing as double
        } catch (NumberFormatException e) {
          params.put(field.getId(), raw); // fallback: save as string if parse fails
        }
      }
    }

    return params;
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
