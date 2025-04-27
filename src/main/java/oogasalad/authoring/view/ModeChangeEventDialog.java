package oogasalad.authoring.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.authoring.view.util.StrategyLoader;
import oogasalad.engine.records.config.model.ModeChangeInfo;
import oogasalad.engine.records.model.ConditionRecord;
import oogasalad.engine.records.model.EntityTypeRecord;
import oogasalad.engine.records.model.ModeChangeEventRecord;

import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.view.components.FormattingUtil;
import oogasalad.player.model.strategies.modechangeevent.ModeChangeEventStrategyInterface;

/**
 * A dialog window that allows the user to configure mode change events for entities. These events
 * specify transitions between behavior modes based on certain conditions (e.g., time).
 */
public class ModeChangeEventDialog extends Stage {


  private static final Map<String, Set<String>> STRATEGY_PARAMETERS = Map.of(
      "TimeElapsed", Set.of("amount")
      // Add more here if you create new strategies
  );


  private final LevelDraft level;
  private final Map<String, EntityTypeRecord> entityTypes;

  private final ComboBox<String> entityTypeDropdown = new ComboBox<>();
  private final ComboBox<String> currentModeDropdown = new ComboBox<>();
  private final ComboBox<String> nextModeDropdown = new ComboBox<>();
  private final ComboBox<String> conditionTypeDropdown = new ComboBox<>();
  private final VBox conditionParamsBox = new VBox(5);
  private final TableView<ModeChangeEventRecord> table = new TableView<>();


  /**
   * Constructs a dialog window for editing mode change events.
   *
   * @param entityTypeMap a map of available entity types
   * @param level         the LevelDraft object to which mode change events are tied
   */
  public ModeChangeEventDialog(Map<String, EntityTypeRecord> entityTypeMap, LevelDraft level) {
    this.entityTypes = entityTypeMap;
    this.level = level;

    setTitle(LanguageManager.getMessage("EDIT_MODE_CHANGE_EVENTS"));
    initModality(Modality.APPLICATION_MODAL);
    setResizable(false);

    VBox root = new VBox(10);
    root.setPadding(new Insets(10));

    HBox formRow1 = new HBox(10, new Label(LanguageManager.getMessage("ENTITY_TYPE")),
        entityTypeDropdown);
    HBox formRow2 = new HBox(10, new Label(LanguageManager.getMessage("CURRENT_MODE")),
        currentModeDropdown);
    HBox formRow3 = new HBox(10, new Label(LanguageManager.getMessage("NEXT_MODE")),
        nextModeDropdown);
    HBox formRow4 = new HBox(10, new Label(LanguageManager.getMessage("CONDITION_TYPE")),
        conditionTypeDropdown);

    populateEntityTypeDropdown();

    // assuming this interface exists
    Map<String, Class<?>> modeChangeStrategies = StrategyLoader.loadStrategies(
        "oogasalad.player.model.strategies.modechangeevent",
        ModeChangeEventStrategyInterface.class // assuming this interface exists
    );

    conditionTypeDropdown.getItems().addAll(modeChangeStrategies.keySet());

    conditionTypeDropdown.setOnAction(e -> renderConditionParams());

    renderConditionParams(); // initial

    Button addButton = new Button(LanguageManager.getMessage("ADD_EVENT"));
    addButton.setOnAction(e -> addModeChangeEvent());

    Button deleteButton = new Button(LanguageManager.getMessage("DELETE_SELECTED"));
    deleteButton.setOnAction(e -> {
      ModeChangeEventRecord selected = table.getSelectionModel().getSelectedItem();
      if (selected != null) {
        level.getModeChangeEvents().remove(selected);
        refreshTable();
      }
    });

    table.getStyleClass().add("mode-change-event-table");
    table.getItems().addAll(level.getModeChangeEvents());
    table.getColumns().addAll(
        makeColumn(LanguageManager.getMessage("ENTITY"), r -> r.entityType().type()),
        makeColumn(LanguageManager.getMessage("FROM"), r -> r.modeChangeInfo().originalMode()),
        makeColumn(LanguageManager.getMessage("TO"), r -> r.modeChangeInfo().transitionMode()),
        makeColumn(LanguageManager.getMessage("CONDITION"),
            r -> r.changeCondition().type() + ": " + r.changeCondition().parameters())
    );

    root.getChildren().addAll(
        formRow1, formRow2, formRow3, formRow4,
        conditionParamsBox,
        new HBox(10, addButton, deleteButton),
        new Label(LanguageManager.getMessage("CURRENT_EVENTS")),
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

    String selectedStrategy = conditionTypeDropdown.getValue();
    if (selectedStrategy == null || !STRATEGY_PARAMETERS.containsKey(selectedStrategy)) {
      return;
    }

    Set<String> params = STRATEGY_PARAMETERS.get(selectedStrategy);

    for (String param : params) {
      Label label = new Label(param);
      TextField field = FormattingUtil.createTextField();
      field.setId(param);
      conditionParamsBox.getChildren().addAll(label, field);
    }
  }


  private void addModeChangeEvent() {
    String entityTypeKey = entityTypeDropdown.getValue();
    String currentMode = currentModeDropdown.getValue();
    String nextMode = nextModeDropdown.getValue();
    String conditionType = conditionTypeDropdown.getValue();

    if (entityTypeKey == null || currentMode == null || nextMode == null || conditionType == null) {
      showAlert(LanguageManager.getMessage("MUST_FILL_OUT_FIELDS"));
      return;
    }

    Map<String, Object> params = new HashMap<>();
    Set<String> expectedParams = STRATEGY_PARAMETERS.getOrDefault(conditionType, Set.of());

    for (String paramName : expectedParams) {
      TextField field = (TextField) conditionParamsBox.lookup("#" + paramName);
      if (field == null) {
        continue;
      }

      String raw = field.getText();
      params.put(paramName,
          raw); // always store as string (consistent with how your strategies parse)
    }

    ConditionRecord condition = new ConditionRecord(conditionType, params);
    EntityTypeRecord entityType = entityTypes.get(entityTypeKey);

    ModeChangeEventRecord event = new ModeChangeEventRecord(
        new EntityTypeRecord(entityType.type(), Collections.emptyMap(), Collections.emptyList()),
        new ModeChangeInfo(currentMode, nextMode, 999999999, 1),
        condition
    );

    level.getModeChangeEvents().add(event);
    refreshTable();
  }


  private void refreshTable() {
    table.getItems().setAll(level.getModeChangeEvents());
  }

  private TableColumn<ModeChangeEventRecord, String> makeColumn(String title,
      java.util.function.Function<ModeChangeEventRecord, String> mapper) {
    TableColumn<ModeChangeEventRecord, String> col = new TableColumn<>(title);
    col.setCellValueFactory(
        data -> new javafx.beans.property.SimpleStringProperty(mapper.apply(data.getValue())));
    return col;
  }

  private void showAlert(String msg) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(LanguageManager.getMessage("WARNING"));
    alert.setHeaderText(null);
    alert.setContentText(msg);

    FormattingUtil.applyStandardDialogStyle(alert);

    alert.showAndWait();
  }
}
