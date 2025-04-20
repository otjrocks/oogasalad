package oogasalad.authoring.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.records.config.model.SettingsRecord;
import oogasalad.engine.records.config.model.wincondition.EntityBasedConditionRecord;
import oogasalad.engine.records.config.model.wincondition.SurviveForTimeConditionRecord;
import oogasalad.engine.records.config.model.wincondition.WinConditionInterface;
import oogasalad.engine.utility.LanguageManager;


/**
 * GameSettingsView provides a compact form for editing game settings Designed to fit at the bottom
 * of the screen as shown in the wireframe
 *
 * @author Angela Predolac, Ishan Madan
 */
public class GameSettingsView {

  private static final double DEFAULT_PADDING = 5;

  private final AuthoringController controller;
  private SettingsRecord gameSettings;

  // Root node containing the view
  private final HBox rootNode;

  // UI Components
  private TextField gameTitleField;
  private TextField authorField;
  private TextField descriptionField;
  private Spinner<Double> gameSpeedSpinner;
  private Spinner<Integer> startingLivesSpinner;
  private Spinner<Integer> initialScoreSpinner;
  private ComboBox<String> scoreStrategyComboBox;
  private ComboBox<String> winConditionTypeComboBox;
  private TextField winConditionValueField;
  private Label winConditionValueLabel;

  /**
   * Constructor initializes the view with the given controller
   */
  public GameSettingsView(AuthoringController controller) {
    this.controller = controller;

    this.gameSettings = controller.getModel().getDefaultSettings();

    this.rootNode = new HBox();
    setupUI();
    bindToModel();
  }

  /**
   * Returns the JavaFX node that represents this view
   */
  public Node getNode() {
    return rootNode;
  }

  /**
   * Set up the UI components in a compact layout making sure buttons are visible
   */
  private void setupUI() {
    // Setup the root container
    rootNode.setSpacing(15);
    rootNode.setPadding(new Insets(DEFAULT_PADDING));
    rootNode.setAlignment(Pos.CENTER_LEFT);
    rootNode.getStyleClass().add("game-settings-view");

    // Create "Game Settings" label
    Label titleLabel = new Label(LanguageManager.getMessage("GAME_SETTINGS"));
    titleLabel.getStyleClass().add("settings-title");
    titleLabel.setPrefWidth(120);

    // Create compact grid layout for settings
    GridPane settingsGrid = new GridPane();
    settingsGrid.setHgap(8);
    settingsGrid.setVgap(5);
    settingsGrid.setPadding(new Insets(0));

    // Create text fields for game metadata
    gameTitleField = new TextField(controller.getModel().getGameTitle());
    gameTitleField.setPrefWidth(150);

    authorField = new TextField(controller.getModel().getAuthor());
    authorField.setPrefWidth(150);

    descriptionField = new TextField(controller.getModel().getGameDescription());
    descriptionField.setPrefWidth(250);

    // Create compact spinners and combo boxes
    gameSpeedSpinner = createDoubleSpinner(0.5, 3.0, 0.1, gameSettings.gameSpeed());
    startingLivesSpinner = createIntegerSpinner(1, 10, 1, gameSettings.startingLives());
    initialScoreSpinner = createIntegerSpinner(0, 1000, 50, gameSettings.initialScore());

    // Create score strategy dropdown
    scoreStrategyComboBox = new ComboBox<>(FXCollections.observableArrayList(
            "Cumulative", "HighestLevel", "TimeBasedMultiplier"));
    scoreStrategyComboBox.setValue(gameSettings.scoreStrategy());
    scoreStrategyComboBox.setPrefWidth(150);

    // Create win condition type dropdown
    winConditionTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(
            "SurviveForTime", "EntityBased"));
    winConditionTypeComboBox.setValue(getWinConditionType());
    winConditionTypeComboBox.setPrefWidth(150);

    // Create win condition value field
    winConditionValueLabel = new Label(LanguageManager.getMessage("WIN_CONDITION_VALUE"));
    winConditionValueField = new TextField(getWinConditionValue());
    winConditionValueField.setPrefWidth(80);

    // Add change listener to update the label based on selected win condition type
    winConditionTypeComboBox.setOnAction(e -> updateWinConditionValueLabel());

    // Add metadata fields
    settingsGrid.add(new Label(LanguageManager.getMessage("GAME_TITLE")), 0, 0);
    settingsGrid.add(gameTitleField, 1, 0);
    settingsGrid.add(new Label(LanguageManager.getMessage("AUTHOR")), 2, 0);
    settingsGrid.add(authorField, 3, 0);

    settingsGrid.add(new Label(LanguageManager.getMessage("DESCRIPTION")), 0, 1);
    settingsGrid.add(descriptionField, 1, 1, 3, 1); // Span across multiple columns

    // Add game settings fields
    settingsGrid.add(new Label(LanguageManager.getMessage("GAME_SPEED")), 0, 2);
    settingsGrid.add(gameSpeedSpinner, 1, 2);
    settingsGrid.add(new Label(LanguageManager.getMessage("STARTING_LIVES")), 2, 2);
    settingsGrid.add(startingLivesSpinner, 3, 2);

    // Add more game settings
    settingsGrid.add(new Label(LanguageManager.getMessage("INITIAL_SCORE")), 0, 3);
    settingsGrid.add(initialScoreSpinner, 1, 3);
    settingsGrid.add(new Label(LanguageManager.getMessage("SCORE_STRATEGY")), 2, 3);
    settingsGrid.add(scoreStrategyComboBox, 3, 3);

    // Add win condition fields
    settingsGrid.add(new Label(LanguageManager.getMessage("WIN_CONDITION_TYPE")), 0, 4);
    settingsGrid.add(winConditionTypeComboBox, 1, 4);
    settingsGrid.add(winConditionValueLabel, 2, 4);
    settingsGrid.add(winConditionValueField, 3, 4);

    // Add bottom buttons
    HBox buttonBox = getHBox();

    rootNode.getChildren().addAll(titleLabel, settingsGrid, buttonBox);

    // Initial update of the win condition value label
    updateWinConditionValueLabel();
  }

  private void updateWinConditionValueLabel() {
    String selectedType = winConditionTypeComboBox.getValue();
    if ("SurviveForTime".equals(selectedType)) {
      winConditionValueLabel.setText(LanguageManager.getMessage("SURVIVE_TIME_SECONDS"));
    } else if ("EntityBased".equals(selectedType)) {
      winConditionValueLabel.setText(LanguageManager.getMessage("ENTITY_TYPE"));
    }
  }

  private String getWinConditionType() {
    WinConditionInterface condition = gameSettings.winCondition();
    if (condition instanceof SurviveForTimeConditionRecord) {
      return "SurviveForTime";
    } else if (condition instanceof EntityBasedConditionRecord) {
      return "EntityBased";
    }
    return "SurviveForTime"; // Default
  }

  private String getWinConditionValue() {
    WinConditionInterface condition = gameSettings.winCondition();
    if (condition instanceof SurviveForTimeConditionRecord(int amount)) {
      return String.valueOf(amount);
    } else if (condition instanceof EntityBasedConditionRecord(String entityType)) {
      return entityType;
    }
    return "5"; // Default time
  }

  private HBox getHBox() {
    Button saveButton = new Button(LanguageManager.getMessage("SAVE_SETTINGS"));
    saveButton.setOnAction(e -> saveSettings());

    Button collisionRulesButton = new Button(LanguageManager.getMessage("COLLISION_RULES"));
    collisionRulesButton.setOnAction(e -> showCollisionRulesPopup());

    // Create button container with more space between buttons
    HBox buttonBox = new HBox(15, saveButton, collisionRulesButton);
    buttonBox.setAlignment(Pos.CENTER_LEFT);
    return buttonBox;
  }

  /**
   * Create a compact spinner for double values
   */
  private Spinner<Double> createDoubleSpinner(double min, double max, double step, double initial) {
    SpinnerValueFactory.DoubleSpinnerValueFactory factory =
        new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initial, step);

    Spinner<Double> spinner = new Spinner<>(factory);
    spinner.setEditable(true);
    spinner.setPrefWidth(80);
    spinner.getStyleClass().add("compact-spinner");

    return spinner;
  }

  /**
   * Create a compact spinner for integer values
   */
  private Spinner<Integer> createIntegerSpinner(int min, int max, int step, int initial) {
    SpinnerValueFactory.IntegerSpinnerValueFactory factory =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial, step);

    Spinner<Integer> spinner = new Spinner<>(factory);
    spinner.setEditable(true);
    spinner.setPrefWidth(80);
    spinner.getStyleClass().add("compact-spinner");

    return spinner;
  }

  /**
   * Display the CollisionRuleEditorView in a popup window
   */
  private void showCollisionRulesPopup() {
    // Create a new stage for the popup
    Stage popupStage = new Stage();
    popupStage.setTitle(LanguageManager.getMessage("COLLISION_RULES_EDITOR"));
    popupStage.initModality(Modality.APPLICATION_MODAL);

    // Create a new CollisionRuleEditorView
    CollisionRuleEditorView collisionEditor = new CollisionRuleEditorView(controller);
    collisionEditor.showAndWait().ifPresent(updatedRules -> {
      controller.getModel().setCollisionRules(updatedRules);
    });
  }

  /**
   * Update the view based on the current model
   */
  public void updateFromModel() {
    // Get the latest settings from the model
    this.gameSettings = controller.getModel().getDefaultSettings();

    // Update metadata fields with model values
    gameTitleField.setText(controller.getModel().getGameTitle());
    authorField.setText(controller.getModel().getAuthor());
    descriptionField.setText(controller.getModel().getGameDescription());

    // Update UI elements with model values
    gameSpeedSpinner.getValueFactory().setValue(gameSettings.gameSpeed());
    startingLivesSpinner.getValueFactory().setValue(gameSettings.startingLives());
    initialScoreSpinner.getValueFactory().setValue(gameSettings.initialScore());
    scoreStrategyComboBox.setValue(gameSettings.scoreStrategy());

    // Update win condition fields
    winConditionTypeComboBox.setValue(getWinConditionType());
    winConditionValueField.setText(getWinConditionValue());
    updateWinConditionValueLabel();
  }

  /**
   * Set up listeners to update the model when UI elements change
   */
  private void bindToModel() {
    updateFromModel();
  }

  /**
   * Save the current settings to the controller
   */
  private void saveSettings() {
    // Commit any edited values in spinners
    commitSpinnerValues();
    // Save metadata fields
    controller.getModel().setGameTitle(gameTitleField.getText());
    controller.getModel().setAuthor(authorField.getText());
    controller.getModel().setGameDescription(descriptionField.getText());
    // Create new win condition based on current UI values
    WinConditionInterface newWinCondition = createWinConditionFromUI();
    // Create new settings record with updated values
    SettingsRecord updatedSettings = new SettingsRecord(
            gameSpeedSpinner.getValue(),
            startingLivesSpinner.getValue(),
            initialScoreSpinner.getValue(),
            scoreStrategyComboBox.getValue(),
            newWinCondition,
            gameSettings.loseCondition() // Keep the existing lose condition
    );
    // Update the model with the current settings
    controller.getModel().setDefaultSettings(updatedSettings);

    // Show confirmation to user
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(LanguageManager.getMessage("SAVED"));
    alert.setHeaderText(null);
    alert.setContentText(LanguageManager.getMessage("GAME_SETTINGS_SAVED"));
    alert.showAndWait();
  }

  private WinConditionInterface createWinConditionFromUI() {
    String type = winConditionTypeComboBox.getValue();
    String value = winConditionValueField.getText();

    if ("SurviveForTime".equals(type)) {
      try {
        int seconds = Integer.parseInt(value);
        return new SurviveForTimeConditionRecord(seconds);
      } catch (NumberFormatException e) {
        // Default to 5 seconds if invalid input
        return new SurviveForTimeConditionRecord(5);
      }
    } else if ("EntityBased".equals(type)) {
      return new EntityBasedConditionRecord(value.isEmpty() ? "dot" : value);
    }

    // Default to survive for 5 seconds
    return new SurviveForTimeConditionRecord(5);
  }

  /**
   * Helper method to commit any edited values in spinners
   */
  private void commitSpinnerValues() {
    // Commit values from all spinners using helper methods
    commitDoubleSpinnerValue(gameSpeedSpinner, 0.5);
    commitIntegerSpinnerValue(startingLivesSpinner, 1);
    commitIntegerSpinnerValue(initialScoreSpinner, 0);
  }

  /**
   * Helper method to safely commit a double spinner value
   */
  private void commitDoubleSpinnerValue(Spinner<Double> spinner, double defaultValue) {
    if (!spinner.isEditable()) {
      return;
    }

    try {
      String text = spinner.getEditor().getText();
      double value = Double.parseDouble(text);
      SpinnerValueFactory<Double> factory = spinner.getValueFactory();

      double min = ((SpinnerValueFactory.DoubleSpinnerValueFactory) factory).getMin();
      double max = ((SpinnerValueFactory.DoubleSpinnerValueFactory) factory).getMax();
      value = Math.max(min, Math.min(max, value));

      factory.setValue(value);
    } catch (NumberFormatException e) {
      spinner.getValueFactory().setValue(defaultValue);
    }
  }

  /**
   * Helper method to safely commit an integer spinner value
   */
  private void commitIntegerSpinnerValue(Spinner<Integer> spinner, int defaultValue) {
    if (!spinner.isEditable()) {
      return;
    }

    try {
      String text = spinner.getEditor().getText();
      int value = Integer.parseInt(text);
      SpinnerValueFactory<Integer> factory = spinner.getValueFactory();

      int min = ((SpinnerValueFactory.IntegerSpinnerValueFactory) factory).getMin();
      int max = ((SpinnerValueFactory.IntegerSpinnerValueFactory) factory).getMax();
      value = Math.max(min, Math.min(max, value));

      factory.setValue(value);
    } catch (NumberFormatException e) {
      spinner.getValueFactory().setValue(defaultValue);
    }
  }
}