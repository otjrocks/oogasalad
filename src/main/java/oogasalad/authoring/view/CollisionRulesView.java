package oogasalad.authoring.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import oogasalad.engine.model.CollisionRule;

public class CollisionRulesView extends Dialog<List<CollisionRule>> {

  private final ComboBox<String> entityASelector = new ComboBox<>();
  private final ComboBox<String> modeASelector = new ComboBox<>();
  private final ComboBox<String> entityBSelector = new ComboBox<>();
  private final ComboBox<String> modeBSelector = new ComboBox<>();
  private final ListView<String> actionASelector = new ListView<>();
  private final ListView<String> actionBSelector = new ListView<>();
  private final ListView<CollisionRule> ruleListView = new ListView<>();

  private final Map<String, List<String>> entityToModes;
  private final List<CollisionRule> workingRules = new ArrayList<>();

  public CollisionRulesView(Map<String, List<String>> entityToModes, List<CollisionRule> existingRules) {
    this.entityToModes = entityToModes;
    if (existingRules != null) workingRules.addAll(existingRules);

    setTitle("Edit Collision Rules");
    getDialogPane().setPrefWidth(800);

    VBox root = new VBox(15);
    root.setPadding(new Insets(20));

    setupEntityAndModeSelectors();
    setupActionSelectors();

    GridPane selectionGrid = new GridPane();
    selectionGrid.setHgap(10);
    selectionGrid.setVgap(10);

    selectionGrid.add(new Label("Entity A:"), 0, 0);
    selectionGrid.add(entityASelector, 1, 0);
    selectionGrid.add(new Label("Mode A:"), 2, 0);
    selectionGrid.add(modeASelector, 3, 0);

    selectionGrid.add(new Label("Entity B:"), 0, 1);
    selectionGrid.add(entityBSelector, 1, 1);
    selectionGrid.add(new Label("Mode B:"), 2, 1);
    selectionGrid.add(modeBSelector, 3, 1);

    HBox actionBox = new HBox(30,
        new VBox(new Label("Actions for Entity A:"), actionASelector),
        new VBox(new Label("Actions for Entity B:"), actionBSelector)
    );

    Button addRuleButton = new Button("Add Rule");
    addRuleButton.setOnAction(e -> handleAddRule());

    Button deleteRuleButton = new Button("Delete Selected Rule");
    deleteRuleButton.setOnAction(e -> {
      CollisionRule selected = ruleListView.getSelectionModel().getSelectedItem();
      if (selected != null) {
        workingRules.remove(selected);
        ruleListView.getItems().remove(selected);
      }
    });

    HBox buttonBox = new HBox(10, addRuleButton, deleteRuleButton);

    ruleListView.setItems(FXCollections.observableArrayList(workingRules));
    ruleListView.setPrefHeight(200);

    root.getChildren().addAll(
        selectionGrid,
        actionBox,
        buttonBox,
        new Label("Defined Rules:"),
        ruleListView
    );

    getDialogPane().setContent(root);
    getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    setResultConverter(button -> {
      if (button == ButtonType.OK) {
        return new ArrayList<>(ruleListView.getItems());
      }
      return null;
    });
  }

  private void setupEntityAndModeSelectors() {
    List<String> entities = new ArrayList<>(entityToModes.keySet());

    entityASelector.setItems(FXCollections.observableArrayList(entities));
    entityBSelector.setItems(FXCollections.observableArrayList(entities));

    entityASelector.setOnAction(e -> updateModes(modeASelector, entityASelector.getValue()));
    entityBSelector.setOnAction(e -> updateModes(modeBSelector, entityBSelector.getValue()));

    if (!entities.isEmpty()) {
      entityASelector.getSelectionModel().selectFirst();
      entityBSelector.getSelectionModel().selectFirst();
    }
  }

  private void updateModes(ComboBox<String> modeBox, String entityName) {
    List<String> modes = new ArrayList<>();
    modes.add("Any");
    if (entityToModes.containsKey(entityName)) {
      modes.addAll(entityToModes.get(entityName));
    }
    modeBox.setItems(FXCollections.observableArrayList(modes));
    modeBox.getSelectionModel().select("Any");
  }

  private void setupActionSelectors() {
    List<String> actions = List.of("REMOVE", "DIE", "STOP", "WIN", "SCORE", "RESPAWN", "IGNORE");

    actionASelector.setItems(FXCollections.observableArrayList(actions));
    actionASelector.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    actionBSelector.setItems(FXCollections.observableArrayList(actions));
    actionBSelector.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  private void handleAddRule() {
    String a = entityASelector.getValue();
    String aMode = modeASelector.getValue();
    String b = entityBSelector.getValue();
    String bMode = modeBSelector.getValue();

    List<String> aActions = new ArrayList<>(actionASelector.getSelectionModel().getSelectedItems());
    List<String> bActions = new ArrayList<>(actionBSelector.getSelectionModel().getSelectedItems());

    if (a == null || aMode == null || b == null || bMode == null || aActions.isEmpty() || bActions.isEmpty()) {
      showError("Please fill out both entities, both modes, and select at least one action for each.");
      return;
    }

    CollisionRule rule = new CollisionRule();
    rule.setEventsA(aActions);
    rule.setEventsB(bActions);
    rule.setModeA(aMode);
    rule.setModeB(bMode);
    rule.setEntityTypeA(a);
    rule.setEntityTypeB(b);
    workingRules.add(rule);
    ruleListView.getItems().add(rule);
  }

  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.initOwner(getDialogPane().getScene().getWindow());
    alert.showAndWait();
  }
}
