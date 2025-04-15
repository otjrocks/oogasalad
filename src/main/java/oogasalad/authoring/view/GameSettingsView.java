package oogasalad.authoring.view;

import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.records.config.model.Settings;
import oogasalad.engine.records.config.model.wincondition.WinCondition;


/**
 * GameSettingsView provides a compact form for editing game settings Designed to fit at the bottom
 * of the screen as shown in the wireframe
 *
 * @author Angela Predolac, Ishan Madan
 */
public class GameSettingsView {

  private static final double DEFAULT_PADDING = 5;

  private final AuthoringController controller;
  private Settings gameSettings;

  // Root node containing the view
  private final HBox rootNode;

  // UI Components
  private Spinner<Double> gameSpeedSpinner;
  private Spinner<Integer> startingLivesSpinner;
  private Spinner<Integer> initialScoreSpinner;
  private TextField gameTitle;
  private TextField authorField;
  private TextArea gameDescriptionArea;
  private ComboBox<String> scoreStrategy;
  private ComboBox<String> winConditionType;
  private TextField winConditionParam;

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

    // Create compact spinners and combo boxes
    gameSpeedSpinner = createDoubleSpinner(0.5, 3.0, 0.1, gameSettings.gameSpeed());
    startingLivesSpinner = createIntegerSpinner(1, 10, 1, gameSettings.startingLives());
    initialScoreSpinner = createIntegerSpinner(0, 1000, 50, gameSettings.initialScore());

    // Create fields for metadata
    gameTitle = new TextField(controller.getModel().getGameTitle());
    gameTitle.setPrefWidth(200);
    authorField = new TextField(controller.getModel().getAuthor());
    authorField.setPrefWidth(200);
    gameDescriptionArea = new TextArea(controller.getModel().getGameDescription());
    gameDescriptionArea.setPrefRowCount(2);
    gameDescriptionArea.setPrefWidth(300);
    gameDescriptionArea.setWrapText(true);

    // Create win condition fields
    scoreStrategy = new ComboBox<>();
    scoreStrategy.getItems().addAll("Cumulative", "HighScore");
    scoreStrategy.setValue(gameSettings.scoreStrategy() != null ?
            gameSettings.scoreStrategy() : "Cumulative");

    winConditionType = new ComboBox<>();
    winConditionType.getItems().addAll("EntityBased", "ScoreBased", "SurviveForTime");

    // Set the default value based on current win condition
    String currentWinCondition = getWinConditionType();
    winConditionType.setValue(currentWinCondition);

    winConditionParam = new TextField(getWinConditionParam());
    winConditionParam.setPrefWidth(100);

    // Add settings to the grid - first row (game specs)
    settingsGrid.add(new Label("Game Title:"), 0, 0);
    settingsGrid.add(gameTitle, 1, 0);
    settingsGrid.add(new Label("Author:"), 2, 0);
    settingsGrid.add(authorField, 3, 0);

    // Second row - game description
    settingsGrid.add(new Label("Description:"), 0, 1);
    settingsGrid.add(gameDescriptionArea, 1, 1, 3, 1);

    // Third row - game speed and lives
    settingsGrid.add(new Label(LanguageManager.getMessage("GAME_SPEED")), 0, 2);
    settingsGrid.add(gameSpeedSpinner, 1, 2);
    settingsGrid.add(new Label(LanguageManager.getMessage("STARTING_LIVES")), 2, 2);
    settingsGrid.add(startingLivesSpinner, 3, 2);

    // Fourth row - score and strategy
    settingsGrid.add(new Label(LanguageManager.getMessage("INITIAL_SCORE")), 0, 3);
    settingsGrid.add(initialScoreSpinner, 1, 3);
    settingsGrid.add(new Label("Score Strategy:"), 2, 3);
    settingsGrid.add(scoreStrategy, 3, 3);

    // Fifth row - win condition
    settingsGrid.add(new Label("Win Condition:"), 0, 4);
    settingsGrid.add(winConditionType, 1, 4);
    settingsGrid.add(new Label("Parameter:"), 2, 4);
    settingsGrid.add(winConditionParam, 3, 4);

    // Create action buttons
    Button saveButton = new Button(LanguageManager.getMessage("SAVE_SETTINGS"));
    saveButton.setOnAction(e -> saveSettings());

    Button collisionRulesButton = new Button(LanguageManager.getMessage("COLLISION_RULES"));
    collisionRulesButton.setOnAction(e -> showCollisionRulesPopup());

    // Create button container with more space between buttons
    HBox buttonBox = new HBox(15, saveButton, collisionRulesButton);
    buttonBox.setAlignment(Pos.CENTER_LEFT);

    // Create a vertical layout to contain all elements
    VBox mainContainer = new VBox(10, titleLabel, settingsGrid, buttonBox);
    mainContainer.setPadding(new Insets(5));

    rootNode.getChildren().add(mainContainer);

    // Add listeners for win condition type changes
    winConditionType.setOnAction(e -> updateWinConditionParamLabel());
    updateWinConditionParamLabel();
  }

  private void updateWinConditionParamLabel() {
    String type = winConditionType.getValue();
    Label paramLabel = (Label) ((GridPane) ((VBox) rootNode.getChildren().get(0)).getChildren().get(1)).getChildren().get(10);

    switch (type) {
      case "EntityBased":
        paramLabel.setText("Entity Type:");
        break;
      case "ScoreBased":
        paramLabel.setText("Target Score:");
        break;
      case "SurviveForTime":
        paramLabel.setText("Seconds:");
        break;
      default:
        paramLabel.setText("Parameter:");
    }
  }

  private String getWinConditionType() {
    if (gameSettings.winCondition() == null) {
      return "SurviveForTime";
    }

    String className = gameSettings.winCondition().getClass().getSimpleName();
    if (className.contains("EntityBased")) {
      return "EntityBased";
    } else if (className.contains("ScoreBased")) {
      return "ScoreBased";
    } else if (className.contains("SurviveForTime")) {
      return "SurviveForTime";
    }

    return "SurviveForTime"; // Default
  }

  private String getWinConditionParam() {
    if (gameSettings.winCondition() == null) {
      return "5"; // Default time in seconds
    }

    String className = gameSettings.winCondition().getClass().getSimpleName();
    if (className.contains("EntityBased")) {
      // Attempt to get the entity type
      try {
        // This would need proper reflection based on actual structure
        return "dot"; // Default if we can't extract
      } catch (Exception e) {
        return "dot";
      }
    } else if (className.contains("ScoreBased")) {
      // Attempt to get target score
      try {
        // This would need proper reflection based on actual structure
        return "1000"; // Default if we can't extract
      } catch (Exception e) {
        return "1000";
      }} else if (className.contains("SurviveForTime")) {
      // Attempt to get time
      try {
        // This would need proper reflection based on actual structure
        return "5"; // Default if we can't extract
      } catch (Exception e) {
        return "5";
      }
    }

    return "5"; // Default
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

    // Update UI elements with model values
    gameSpeedSpinner.getValueFactory().setValue(gameSettings.gameSpeed());
    startingLivesSpinner.getValueFactory().setValue(gameSettings.startingLives());
    initialScoreSpinner.getValueFactory().setValue(gameSettings.initialScore());
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
    // Update the model with the current settings
    controller.getModel().setGameTitle(gameTitle.getText());
    controller.getModel().setAuthor(authorField.getText());
    controller.getModel().setGameDescription(gameDescriptionArea.getText());
    Settings updatedSettings = createUpdatedSettings();
    controller.getModel().setDefaultSettings(updatedSettings);

    // Show confirmation to user
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(LanguageManager.getMessage("SAVED"));
    alert.setHeaderText(null);
    alert.setContentText(LanguageManager.getMessage("GAME_SETTINGS_SAVED"));
    alert.showAndWait();
  }

  private Settings createUpdatedSettings() {
    // Build the win condition based on selected type
    Object winCondition = buildWinCondition();

    // Create and return the new Settings object
    return new Settings(
            gameSpeedSpinner.getValue(),
            startingLivesSpinner.getValue(),
            initialScoreSpinner.getValue(),
            scoreStrategy.getValue(),
            (WinCondition) winCondition
    );
  }

  private Object buildWinCondition() {
    String type = winConditionType.getValue();
    String param = winConditionParam.getText();

    switch (type) {
      case "EntityBased":
        return createEntityBasedWinCondition(param);
      case "ScoreBased":
        return createScoreBasedWinCondition(param);
      case "SurviveForTime":
        return createSurviveForTimeCondition(param);
      default:
        return createSurviveForTimeCondition("5"); // Default
    }
  }

  private Object createEntityBasedWinCondition(String entityType) {
    try {
      // Create an EntityBasedWinCondition with the specified entity type
      Class<?> clazz = Class.forName("oogasalad.engine.records.config.model.wincondition.EntityBasedWinCondition");
      return clazz.getConstructor(String.class).newInstance(entityType);
    } catch (Exception e) {
      // Fallback to SurviveForTimeCondition if entity-based fails
      return createSurviveForTimeCondition("5");
    }
  }

  private Object createScoreBasedWinCondition(String scoreStr) {
    try {
      // Parse the target score
      int targetScore = Integer.parseInt(scoreStr);

      // Create a ScoreBasedWinCondition with the specified target score
      Class<?> clazz = Class.forName("oogasalad.engine.records.config.model.wincondition.ScoreBasedWinCondition");
      return clazz.getConstructor(int.class).newInstance(targetScore);
    } catch (Exception e) {
      // Fallback to SurviveForTimeCondition if score-based fails
      return createSurviveForTimeCondition("5");
    }
  }

  private Object createSurviveForTimeCondition(String secondsStr) {
    try {
      // Parse the time in seconds
      int seconds = Integer.parseInt(secondsStr);

      // Create a SurviveForTimeCondition with the specified time
      return new oogasalad.engine.records.config.model.wincondition.SurviveForTimeCondition(seconds);
    } catch (Exception e) {
      // Default to 5 seconds if parsing fails
      return new oogasalad.engine.records.config.model.wincondition.SurviveForTimeCondition(5);
    }
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