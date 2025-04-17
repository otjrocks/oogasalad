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
import javafx.scene.layout.Priority;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.records.config.model.Settings;
import oogasalad.engine.records.config.model.wincondition.EntityBasedCondition;
import oogasalad.engine.records.config.model.wincondition.SurviveForTimeCondition;
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
  private final VBox rootNode;

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
  private Label winConditionParamLabel;

  private static final String ENTITY_BASED = "EntityBased";
  private static final String SCORE_BASED = "ScoreBased";
  private static final String SURVIVE_FOR_TIME = "SurviveForTime";

  private static final String ENTITY_BASED_CLASS = "EntityBased";
  private static final String SCORE_BASED_CLASS = "ScoreBased";
  private static final String SURVIVE_FOR_TIME_CLASS = "SurviveForTime";
  private static final String PARAMETER_LABEL = "Parameter:";

  /**
   * Constructor initializes the view with the given controller
   */
  public GameSettingsView(AuthoringController controller) {
    this.controller = controller;
    this.gameSettings = controller.getModel().getDefaultSettings();
    this.rootNode = new VBox();
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
    rootNode.setSpacing(0);
    rootNode.setPadding(new Insets(DEFAULT_PADDING));
    VBox.setVgrow(rootNode, Priority.ALWAYS);
    rootNode.getStyleClass().add("game-settings-view");

    // Create "Game Settings" label
    Label titleLabel = new Label(LanguageManager.getMessage("GAME_SETTINGS"));
    titleLabel.getStyleClass().add("settings-title");
    titleLabel.setPrefWidth(120);

    TabPane tabPane = new TabPane();
    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    tabPane.setPrefHeight(250);  // Set a fixed height for the tab pane

    // Create tabs
    Tab metadataTab = new Tab("Game Metadata");
    Tab gameplayTab = new Tab("Gameplay Settings");
    Tab winConditionTab = new Tab("Win Conditions");

    GridPane metadataGrid = new GridPane();
    metadataGrid.setHgap(10);
    metadataGrid.setVgap(10);
    metadataGrid.setPadding(new Insets(10));

// Game Title & Author side-by-side
    Label gameTitleLabel = new Label("Game Title:");
    gameTitle = new TextField(controller.getModel().getGameTitle());
    gameTitle.setPrefWidth(200);

    Label authorLabel = new Label("Author:");
    authorField = new TextField(controller.getModel().getAuthor());
    authorField.setPrefWidth(200);

    metadataGrid.add(gameTitleLabel, 0, 0);
    metadataGrid.add(gameTitle, 1, 0);
    metadataGrid.add(authorLabel, 2, 0);
    metadataGrid.add(authorField, 3, 0);

// Description below, spanning all columns
    Label descLabel = new Label("Description:");
    gameDescriptionArea = new TextArea(controller.getModel().getGameDescription());
    gameDescriptionArea.setPrefRowCount(4);
    gameDescriptionArea.setWrapText(true);
    gameDescriptionArea.setPrefWidth(400);

    metadataGrid.add(descLabel, 0, 1);
    metadataGrid.add(gameDescriptionArea, 0, 2, 4, 1); // Span 4 columns

    // Create contents for gameplay settings tab
    GridPane gameplayGrid = new GridPane();
    gameplayGrid.setHgap(10);
    gameplayGrid.setVgap(10);
    gameplayGrid.setPadding(new Insets(10));

    // Create compact spinners and combo boxes
    gameSpeedSpinner = createDoubleSpinner(0.5, 3.0, 0.1, gameSettings.gameSpeed());
    startingLivesSpinner = createIntegerSpinner(1, 10, 1, gameSettings.startingLives());
    initialScoreSpinner = createIntegerSpinner(0, 1000, 50, gameSettings.initialScore());

    scoreStrategy = new ComboBox<>();
    scoreStrategy.getItems().addAll("Cumulative", "HighScore");
    scoreStrategy.setValue(gameSettings.scoreStrategy() != null ?
            gameSettings.scoreStrategy() : "Cumulative");
    scoreStrategy.setPrefWidth(150);

    gameplayGrid.add(new Label(LanguageManager.getMessage("GAME_SPEED")), 0, 0);
    gameplayGrid.add(gameSpeedSpinner, 1, 0);
    gameplayGrid.add(new Label(LanguageManager.getMessage("STARTING_LIVES")), 0, 1);
    gameplayGrid.add(startingLivesSpinner, 1, 1);
    gameplayGrid.add(new Label(LanguageManager.getMessage("INITIAL_SCORE")), 0, 2);
    gameplayGrid.add(initialScoreSpinner, 1, 2);
    gameplayGrid.add(new Label("Score Strategy:"), 0, 3);
    gameplayGrid.add(scoreStrategy, 1, 3);

    // Create contents for win condition tab
    GridPane winGrid = new GridPane();
    winGrid.setHgap(10);
    winGrid.setVgap(10);
    winGrid.setPadding(new Insets(10));

    winConditionType = new ComboBox<>();
    winConditionType.getItems().addAll(ENTITY_BASED, SCORE_BASED, SURVIVE_FOR_TIME);
    winConditionType.setPrefWidth(150);
    winConditionType.setPrefWidth(150);

    winConditionParam = new TextField(getWinConditionParam());
    winConditionParam.setPrefWidth(150);

    winConditionParamLabel = new Label(PARAMETER_LABEL);

    winGrid.add(new Label("Win Condition Type:"), 0, 0);
    winGrid.add(winConditionType, 1, 0);
    winGrid.add(winConditionParamLabel, 0, 1);
    winGrid.add(winConditionParam, 1, 1);

    // Add descriptions for win conditions
    VBox descriptions = new VBox(5);
    descriptions.setPadding(new Insets(10, 0, 0, 0));
    descriptions.getChildren().addAll(
            new Label(ENTITY_BASED + ": Win by consuming all entities of type"),
            new Label(SCORE_BASED + ": Win by reaching target score"),
            new Label(SURVIVE_FOR_TIME + ": Win by surviving for X seconds")
    );
    descriptions.setStyle("-fx-font-style: italic; -fx-font-size: 12px;");

    VBox winContent = new VBox(10);
    winContent.getChildren().addAll(winGrid, descriptions);

    // Set content for tabs
    metadataTab.setContent(metadataGrid);
    gameplayTab.setContent(gameplayGrid);
    winConditionTab.setContent(winContent);

    // Add tabs to tabPane
    tabPane.getTabs().addAll(metadataTab, gameplayTab, winConditionTab);

    // Create buttons
    Button saveButton = new Button(LanguageManager.getMessage("SAVE_SETTINGS"));
    saveButton.setOnAction(e -> saveSettings());

    Button collisionRulesButton = new Button(LanguageManager.getMessage("COLLISION_RULES"));
    collisionRulesButton.setOnAction(e -> showCollisionRulesPopup());

    HBox buttonBox = new HBox(15, saveButton, collisionRulesButton);
    buttonBox.setAlignment(Pos.CENTER_RIGHT);
    buttonBox.setPadding(new Insets(5, 0, 0, 0));

    // Add all to main layout
    VBox mainLayout = new VBox(10);
    mainLayout.setPadding(new Insets(5));
    mainLayout.getChildren().addAll(titleLabel, tabPane, buttonBox);

    rootNode.getChildren().add(mainLayout);

    // Add listeners
    winConditionType.setOnAction(e -> updateWinConditionParamLabel());
    updateWinConditionParamLabel();

    VBox.setVgrow(mainLayout, Priority.ALWAYS);

  }

  private void updateWinConditionParamLabel() {
    String type = winConditionType.getValue();

    if (type == null) {
      winConditionParamLabel.setText(PARAMETER_LABEL);
      return;
    }

    switch (type) {
      case ENTITY_BASED:
        winConditionParamLabel.setText("Entity Type:");
        break;
      case SCORE_BASED:
        winConditionParamLabel.setText("Target Score:");
        break;
      case SURVIVE_FOR_TIME:
        winConditionParamLabel.setText("Seconds:");
        break;
      default:
        winConditionParamLabel.setText(PARAMETER_LABEL);
    }
  }

  /**
   * Determines the win condition type based on the current settings.
   *
   * @return the string representation of the current win condition type
   */
  private String getWinConditionType() {
    if (gameSettings.winCondition() == null) {
      return SURVIVE_FOR_TIME;
    }

    String className = gameSettings.winCondition().getClass().getSimpleName();
    if (className.contains(ENTITY_BASED_CLASS)) {
      return ENTITY_BASED;
    } else if (className.contains(SCORE_BASED_CLASS)) {
      return SCORE_BASED;
    } else if (className.contains(SURVIVE_FOR_TIME_CLASS)) {
      return SURVIVE_FOR_TIME;
    }

    return SURVIVE_FOR_TIME; // Default
  }

  /**
   * Retrieves the parameter value for the current win condition.
   *
   * @return the string representation of the win condition parameter
   */
  private String getWinConditionParam() {
    if (gameSettings.winCondition() == null) {
      return "5"; // Default if no win condition
    }

    String className = gameSettings.winCondition().getClass().getSimpleName();

    if (className.contains(ENTITY_BASED_CLASS)) {
      return "dot";
    }
    if (className.contains(SCORE_BASED_CLASS)) {
      return "1000";
    }
    if (className.contains(SURVIVE_FOR_TIME_CLASS)) {
      return "5";
    }

    return "5"; // Fallback default
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

    // Update metadata fields
    gameTitle.setText(controller.getModel().getGameTitle());
    authorField.setText(controller.getModel().getAuthor());
    gameDescriptionArea.setText(controller.getModel().getGameDescription());

    // Update score strategy
    if (gameSettings.scoreStrategy() != null) {
      scoreStrategy.setValue(gameSettings.scoreStrategy());
    }

    // Update win condition fields
    winConditionType.setValue(getWinConditionType());
    winConditionParam.setText(getWinConditionParam());
    updateWinConditionParamLabel();
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

    // Update metadata
    String titleText = gameTitle.getText();
    String authorText = authorField.getText();
    String descText = gameDescriptionArea.getText();

    if (titleText != null && !titleText.isBlank()) {
      controller.getModel().setGameTitle(titleText);
    }

    if (authorText != null && !authorText.isBlank()) {
      controller.getModel().setAuthor(authorText);
    }

    if (descText != null && !descText.isBlank()) {
      controller.getModel().setGameDescription(descText);
    }

    // Create updated settings with game values and win condition
    Settings updatedSettings = new Settings(
            gameSpeedSpinner.getValue(),
            startingLivesSpinner.getValue(),
            initialScoreSpinner.getValue(),
            scoreStrategy.getValue(),
            createWinCondition()
    );

    // Update the model
    controller.getModel().setDefaultSettings(updatedSettings);

    // Show confirmation to user
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(LanguageManager.getMessage("SAVED"));
    alert.setHeaderText(null);
    alert.setContentText(LanguageManager.getMessage("GAME_SETTINGS_SAVED"));
    alert.showAndWait();
  }

  /**
   * Creates the appropriate win condition based on UI selection
   */
  private WinCondition createWinCondition() {
    String type = winConditionType.getValue();
    String param = winConditionParam.getText();

    try {
      switch (type) {
        case ENTITY_BASED:
          return new EntityBasedCondition(param);

        case SURVIVE_FOR_TIME:
          int seconds = Integer.parseInt(param);
          return new SurviveForTimeCondition(seconds);

        default:
          return new SurviveForTimeCondition(5);
      }
    } catch (Exception e) {
      // Default to SurviveForTime if any errors
      return new SurviveForTimeCondition(5);
    }
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

      return switch (type) {
          case ENTITY_BASED -> createEntityBasedWinCondition(param);
          case SCORE_BASED -> createScoreBasedWinCondition(param);
          case SURVIVE_FOR_TIME -> createSurviveForTimeCondition(param);
          default -> createSurviveForTimeCondition("5"); // Default
      };
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