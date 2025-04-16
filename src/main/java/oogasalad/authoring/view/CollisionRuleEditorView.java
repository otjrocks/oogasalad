package oogasalad.authoring.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.model.CollisionRule;
import oogasalad.engine.records.config.model.CollisionEvent;

/**
 * A dialog view that allows the user to define and edit collision rules between pairs of entity
 * types and their modes.
 * <p>
 * Each rule specifies: - An Entity A and Mode A - An Entity B and Mode B - A set of actions for
 * Entity A - A set of actions for Entity B
 * <p>
 * Users can add, delete, and view defined collision rules. This dialog returns a list of
 * CollisionRule objects via showAndWait().
 *
 * @author Angela Predolac, Will He, Ishan Madan
 */
public class CollisionRuleEditorView {

  private final ComboBox<String> entityASelector = new ComboBox<>();
  private final ComboBox<String> modeASelector = new ComboBox<>();
  private final ComboBox<String> entityBSelector = new ComboBox<>();
  private final ComboBox<String> modeBSelector = new ComboBox<>();
  private final ListView<CollisionRule> ruleListView = new ListView<>();
  private final Dialog<List<CollisionRule>> dialog;

  private Map<String, List<String>> entityToModes;
  private final List<CollisionRule> workingRules = new ArrayList<>();
  private final VBox root;
  private final AuthoringController controller;
  private CollisionEventView myRuleViewA;
  private CollisionEventView myRuleViewB;


  /**
   * Constructs a dialog for editing collision rules. Initializes the UI based on entity types and
   * existing rules from the controller's model.
   *
   * @param controller the AuthoringController used to fetch entity types and collision rules
   */
  public CollisionRuleEditorView(AuthoringController controller) {
    this.entityToModes = controller.getModel().getEntityTypeToModes();
    this.controller = controller;
    List<CollisionRule> existingRules = controller.getModel().getCollisionRules();
    if (existingRules != null) {
      workingRules.addAll(existingRules);
    }

    // Create dialog instance
    dialog = new Dialog<>();
    dialog.setTitle(LanguageManager.getMessage("EDIT_COLLISION"));
    dialog.getDialogPane().setPrefWidth(800);

    root = new VBox(15);
    root.setPadding(new Insets(20));

    setupEntityAndModeSelectors();

    GridPane selectionGrid = new GridPane();
    selectionGrid.setHgap(10);
    selectionGrid.setVgap(10);

    selectionGrid.add(new Label(LanguageManager.getMessage("ENTITY_A")), 0, 0);
    selectionGrid.add(entityASelector, 1, 0);
    selectionGrid.add(new Label(LanguageManager.getMessage("MODE_A")), 2, 0);
    selectionGrid.add(modeASelector, 3, 0);

    selectionGrid.add(new Label(LanguageManager.getMessage("ENTITY_B")), 0, 1);
    selectionGrid.add(entityBSelector, 1, 1);
    selectionGrid.add(new Label(LanguageManager.getMessage("MODE_B")), 2, 1);
    selectionGrid.add(modeBSelector, 3, 1);

    HBox addRule = createCollisionRuleHBox();

    HBox buttonBox = getHBox();

    ruleListView.setItems(FXCollections.observableArrayList(workingRules));
    ruleListView.setPrefHeight(300);
    ScrollPane ruleScrollPane = new ScrollPane(ruleListView);
    ruleScrollPane.setFitToWidth(true);
    ruleScrollPane.setPrefHeight(300);

    root.getChildren().addAll(
        selectionGrid,
        addRule,
        buttonBox,
        new Label(LanguageManager.getMessage("DEFINED_RULES")),
        ruleScrollPane
    );

    ScrollPane scrollPane = new ScrollPane(root);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true); // optional
    scrollPane.setPadding(new Insets(10));

    dialog.getDialogPane().setContent(scrollPane);
    dialog.getDialogPane().setPrefSize(800, 600);

    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    dialog.setResultConverter(button -> {
      if (button == ButtonType.OK) {
        return new ArrayList<>(ruleListView.getItems());
      }
      return null;
    });
  }

  private HBox createCollisionRuleHBox() {
    HBox addRule = new HBox(15);
    myRuleViewA = new CollisionEventView(
        String.format(LanguageManager.getMessage("RULE_VIEW_LABEL"), "A"));
    myRuleViewB = new CollisionEventView(
        String.format(LanguageManager.getMessage("RULE_VIEW_LABEL"), "B"));
    addRule.getChildren().addAll(myRuleViewA.getRoot(), myRuleViewB.getRoot());
    return addRule;
  }

  /**
   * Shows the dialog and waits for user input.
   *
   * @return Optional containing the list of collision rules if OK was pressed, empty Optional
   * otherwise
   */
  public Optional<List<CollisionRule>> showAndWait() {
    return dialog.showAndWait();
  }

  /**
   * Returns the underlying dialog instance
   *
   * @return the dialog
   */
  public Dialog<List<CollisionRule>> getDialog() {
    return dialog;
  }

  private HBox getHBox() {
    Button addRuleButtonA = new Button(LanguageManager.getMessage("ADD_ACTION") + " A");
    addRuleButtonA.setOnAction(e -> handleAddRuleA());

    Button addRuleButtonB = new Button(LanguageManager.getMessage("ADD_ACTION") + " B");
    addRuleButtonB.setOnAction(e -> handleAddRuleB());

    Button deleteRuleButton = new Button(LanguageManager.getMessage("DELETE_RULE"));
    deleteRuleButton.setOnAction(e -> {
      CollisionRule selected = ruleListView.getSelectionModel().getSelectedItem();
      if (selected != null) {
        workingRules.remove(selected);
        ruleListView.getItems().remove(selected);
      }
    });

    return new HBox(10, addRuleButtonA, addRuleButtonB, deleteRuleButton);
  }

  /**
   * Initializes the entity and mode selectors with data from the model. Sets listeners to
   * dynamically populate mode dropdowns based on selected entity.
   */
  private void setupEntityAndModeSelectors() {
    List<String> entities = new ArrayList<>(entityToModes.keySet());

    entityASelector.setItems(FXCollections.observableArrayList(entities));
    entityBSelector.setItems(FXCollections.observableArrayList(entities));

    entityASelector.setOnAction(e -> updateModes(modeASelector, entityASelector.getValue()));
    entityBSelector.setOnAction(e -> updateModes(modeBSelector, entityBSelector.getValue()));

    if (!entities.isEmpty()) {
      entityASelector.getSelectionModel().selectFirst();
      entityBSelector.getSelectionModel().selectFirst();
      updateModes(modeASelector, entityASelector.getValue());
      updateModes(modeBSelector, entityBSelector.getValue());
    }
  }

  /**
   * Updates the mode selector with the list of modes for the selected entity. Includes a default
   * "Any" option for wildcard matching.
   *
   * @param modeBox    the ComboBox to populate
   * @param entityName the selected entity name
   */
  private void updateModes(ComboBox<String> modeBox, String entityName) {
    entityToModes = controller.getModel().getEntityTypeToModes();
    List<String> modes = new ArrayList<>();
    if (entityToModes.containsKey(entityName)) {
      modes.addAll(entityToModes.get(entityName));
    }
    modeBox.setItems(FXCollections.observableArrayList(modes));
    modeBox.getSelectionModel().select("Default");
  }

  private void handleAddRuleA() {
    String a = entityASelector.getValue();
    String aMode = modeASelector.getValue();
    String b = entityBSelector.getValue();
    String bMode = modeBSelector.getValue();

    if (validateInput(a, aMode, b, bMode)) {
      return;
    }

    updateRules(a, b, aMode, bMode, true);
  }

  private boolean validateInput(String a, String aMode, String b, String bMode) {
    if (a == null || aMode == null || b == null || bMode == null) {
      showError(
          LanguageManager.getMessage("RULE_ERROR"));
      return true;
    }
    return false;
  }

  private void handleAddRuleB() {
    String a = entityASelector.getValue();
    String aMode = modeASelector.getValue();
    String b = entityBSelector.getValue();
    String bMode = modeBSelector.getValue();

    validateInput(a, aMode, b, bMode);
    updateRules(a, b, aMode, bMode, false);
  }

  private void updateRules(String a, String b, String aMode, String bMode, boolean isA) {
    boolean ruleAlreadyExists = checkForExistingRuleAndUpdate(a, b, aMode, bMode, isA);
    if (!ruleAlreadyExists) {
      CollisionRule rule = new CollisionRule();
      updateCollisionRule(a, b, aMode, bMode, rule, isA);
      workingRules.add(rule);
      ruleListView.getItems().add(rule);
    }
  }

  private boolean checkForExistingRuleAndUpdate(String a, String b, String aMode, String bMode,
      boolean isA) {
    boolean ruleAlreadyExists = false;
    for (CollisionRule rule : workingRules) {
      if (rule.getEntityA().equals(a) && rule.getEntityB().equals(b)
          && rule.getModeA().equals(aMode) && rule.getModeB().equals(bMode)) {
        updateCollisionRule(a, b, aMode, bMode, rule, isA);
        ruleAlreadyExists = true;
        // move update rule to bottom of list
        ruleListView.getItems().remove(rule);
        ruleListView.getItems().add(rule);
      }
    }
    return ruleAlreadyExists;
  }

  private void updateCollisionRule(String a, String b, String aMode, String bMode,
      CollisionRule rule, boolean isA) {
    rule.setEntityA(a);
    rule.setEntityB(b);
    CollisionEvent eventA;
    CollisionEvent eventB;
    try {
      eventA = myRuleViewA.getCollisionEvent();
      eventB = myRuleViewB.getCollisionEvent();
    } catch (IllegalArgumentException e) {
      showError(e.getMessage());
      throw e;
    }
    List<CollisionEvent> eventsA = rule.getEventsA();
    if (eventsA == null) {
      eventsA = new ArrayList<>();
      rule.setEventsA(eventsA);
    }
    List<CollisionEvent> eventsB = rule.getEventsB();
    if (eventsB == null) {
      eventsB = new ArrayList<>();
      rule.setEventsB(eventsB);
    }
    if (isA) {
      eventsA.add(eventA);
      rule.setEventsA(eventsA);
    } else {
      eventsB.add(eventB);
      rule.setEventsB(eventsB);
    }
    rule.setModeA(aMode);
    rule.setModeB(bMode);
  }

  /**
   * Displays an error alert dialog with the given message.
   *
   * @param msg the error message to display
   */
  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.initOwner(dialog.getDialogPane().getScene().getWindow());
    alert.showAndWait();
  }

  /**
   * Returns the root layout node of the dialog (for testing/debugging).
   *
   * @return the root VBox node
   */
  public Node getNode() {
    return root;
  }

}
