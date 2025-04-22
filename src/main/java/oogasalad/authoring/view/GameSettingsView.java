package oogasalad.authoring.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.records.config.model.SettingsRecord;
import oogasalad.engine.records.config.model.losecondition.LivesBasedConditionRecord;
import oogasalad.engine.records.config.model.losecondition.LoseConditionInterface;
import oogasalad.engine.records.config.model.wincondition.EntityBasedConditionRecord;
import oogasalad.engine.records.config.model.wincondition.SurviveForTimeConditionRecord;
import oogasalad.engine.records.config.model.wincondition.WinConditionInterface;
import oogasalad.engine.utility.FileUtility;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.LoggingManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * GameSettingsView provides a compact form for editing game settings Designed to fit at the bottom
 * of the screen as shown in the wireframe
 *
 * @author Angela Predolac, Ishan Madan
 */
public class GameSettingsView {

  // Package path constants
  private static final String WIN_CONDITION_PACKAGE = "oogasalad.engine.records.config.model.wincondition";
  private static final String LOSE_CONDITION_PACKAGE = "oogasalad.engine.records.config.model.losecondition";

  // Record suffix
  private static final String RECORD_SUFFIX = "Record";

  // Map to store win condition class information
  private final Map<String, Class<?>> winConditionClasses = new HashMap<>();
  private final Map<String, Class<?>> loseConditionClasses = new HashMap<>();


  // Constants for win condition types
  private static final String WIN_CONDITION_SURVIVE_FOR_TIME = "SurviveForTime";
  private static final String WIN_CONDITION_ENTITY_BASED = "EntityBased";

  // Constants for lose condition types
  private static final String LOSE_CONDITION_LIVES_BASED = "LivesBased";

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
  private ComboBox<String> winConditionTypeComboBox;
  private TextField winConditionValueField;
  private Label winConditionValueLabel;
  private ComboBox<String> loseConditionTypeComboBox;
  private TextField loseConditionValueField;
  private Label loseConditionValueLabel;

  /**
   * Constructor initializes the view with the given controller
   */
  public GameSettingsView(AuthoringController controller) {
    this.controller = controller;
    this.gameSettings = controller.getModel().getDefaultSettings();
    this.rootNode = new HBox();

    loadConditionClasses();
    setupUI();
    bindToModel();
  }

  /**
   * Load available win and lose condition classes using reflection
   */
  private void loadConditionClasses() {
    try {
      Class<?> winConditionInterface = Class.forName(WIN_CONDITION_PACKAGE + ".WinConditionInterface");
      winConditionClasses.putAll(findConditionClasses(WIN_CONDITION_PACKAGE, winConditionInterface));

      Class<?> loseConditionInterface = Class.forName(LOSE_CONDITION_PACKAGE + ".LoseConditionInterface");
      loseConditionClasses.putAll(findConditionClasses(LOSE_CONDITION_PACKAGE, loseConditionInterface));

      if (winConditionClasses.isEmpty()) {
        LoggingManager.LOGGER.warn("No win condition classes found via reflection. Using hardcoded defaults.");
        Class<?> surviveForTimeClass = Class.forName(WIN_CONDITION_PACKAGE + ".SurviveForTimeConditionRecord");
        Class<?> entityBasedClass = Class.forName(WIN_CONDITION_PACKAGE + ".EntityBasedConditionRecord");

        winConditionClasses.put("SurviveForTime", surviveForTimeClass);
        winConditionClasses.put("EntityBased", entityBasedClass);
      }

      if (loseConditionClasses.isEmpty()) {
        LoggingManager.LOGGER.warn("No lose condition classes found via reflection. Using hardcoded defaults.");
        Class<?> livesBasedClass = Class.forName(LOSE_CONDITION_PACKAGE + ".LivesBasedConditionRecord");
        loseConditionClasses.put("LivesBased", livesBasedClass);
      }

      LoggingManager.LOGGER.info("Loaded {} win condition classes and {} lose condition classes",
              winConditionClasses.size(), loseConditionClasses.size());

    } catch (ClassNotFoundException e) {
      LoggingManager.LOGGER.error("Error loading condition interfaces", e);

      try {
        Class<?> surviveForTimeClass = Class.forName(WIN_CONDITION_PACKAGE + ".SurviveForTimeConditionRecord");
        Class<?> entityBasedClass = Class.forName(WIN_CONDITION_PACKAGE + ".EntityBasedConditionRecord");
        Class<?> livesBasedClass = Class.forName(LOSE_CONDITION_PACKAGE + ".LivesBasedConditionRecord");

        winConditionClasses.put("SurviveForTime", surviveForTimeClass);
        winConditionClasses.put("EntityBased", entityBasedClass);
        loseConditionClasses.put("LivesBased", livesBasedClass);
      } catch (ClassNotFoundException ex) {
        LoggingManager.LOGGER.error("Critical error: Could not load default condition classes", ex);
      }
    }
  }

  /**
   * Set a specific preferred height for the view
   *
   * @param height the preferred height in pixels
   */
  public void setPreferredHeight(double height) {
    rootNode.setPrefHeight(height);
  }

  /**
   * Set a specific minimum height for the view
   *
   * @param height the minimum height in pixels
   */
  public void setMinimumHeight(double height) {
    rootNode.setMinHeight(height);
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
    // Set up the root container
    rootNode.setSpacing(15);
    rootNode.setPadding(new Insets(15)); // Increase padding
    rootNode.setAlignment(Pos.CENTER_LEFT);
    rootNode.getStyleClass().add("game-settings-view");

    // Create scrollable container for all content
    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(150); // Smaller preferred height to fit the container
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    VBox contentBox = new VBox(10); // Main content container with spacing
    contentBox.setPadding(new Insets(5));

    // Create "Game Settings" label
    Label titleLabel = new Label(LanguageManager.getMessage("GAME_SETTINGS"));
    titleLabel.getStyleClass().add("settings-title");
    titleLabel.setPrefWidth(120);

    // Create compact grid layout for settings
    GridPane settingsGrid = new GridPane();
    settingsGrid.setHgap(10);
    settingsGrid.setVgap(8); // Increase vertical gap
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

    // Create win condition UI components
    createWinConditionComponents();

    // Create lose condition UI components
    createLoseConditionComponents();

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

    // Add win condition fields
    settingsGrid.add(new Label(LanguageManager.getMessage("WIN_CONDITION_TYPE")), 0, 4);
    settingsGrid.add(winConditionTypeComboBox, 1, 4);
    settingsGrid.add(winConditionValueLabel, 2, 4);
    settingsGrid.add(winConditionValueField, 3, 4);

    settingsGrid.add(new Label(LanguageManager.getMessage("LOSE_CONDITION_TYPE")), 0, 5);
    settingsGrid.add(loseConditionTypeComboBox, 1, 5);
    settingsGrid.add(loseConditionValueLabel, 2, 5);
    settingsGrid.add(loseConditionValueField, 3, 5);

    // Add buttons
    HBox buttonBox = getHBox();

    // Add everything to the content box
    contentBox.getChildren().addAll(titleLabel, settingsGrid, buttonBox);

    // Set content to scroll pane
    scrollPane.setContent(contentBox);

    // Add scroll pane to root
    rootNode.getChildren().add(scrollPane);
  }

  private void updateWinConditionValueLabel() {
    String selectedType = winConditionTypeComboBox.getValue();
    if (WIN_CONDITION_SURVIVE_FOR_TIME.equals(selectedType)) {
      winConditionValueLabel.setText(LanguageManager.getMessage("SURVIVE_TIME_SECONDS"));
    } else if (WIN_CONDITION_ENTITY_BASED.equals(selectedType)) {
      winConditionValueLabel.setText(LanguageManager.getMessage("ENTITY_TYPE"));
    }
  }

  private String getWinConditionType() {
    WinConditionInterface condition = gameSettings.winCondition();
    if (condition instanceof SurviveForTimeConditionRecord) {
      return WIN_CONDITION_SURVIVE_FOR_TIME;
    } else if (condition instanceof EntityBasedConditionRecord) {
      return WIN_CONDITION_ENTITY_BASED;
    }
    return WIN_CONDITION_SURVIVE_FOR_TIME; // Default
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
    collisionEditor.showAndWait().ifPresent(updatedRules -> controller.getModel().setCollisionRules(updatedRules));
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
    // Create new lose condition based on current UI values
    LoseConditionInterface newLoseCondition = createLoseConditionFromUI();
    // Create new settings record with updated values
    SettingsRecord updatedSettings = new SettingsRecord(
        gameSpeedSpinner.getValue(),
        startingLivesSpinner.getValue(),
        initialScoreSpinner.getValue(),
        newWinCondition,
        newLoseCondition
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

  /**
   * Creates UI components for win condition selection
   */
  private void createWinConditionComponents() {
    // Create win condition type dropdown
    winConditionTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(
            WIN_CONDITION_SURVIVE_FOR_TIME, WIN_CONDITION_ENTITY_BASED));
    winConditionTypeComboBox.setValue(getWinConditionType());
    winConditionTypeComboBox.setPrefWidth(150);

    // Create win condition value field
    winConditionValueLabel = new Label(LanguageManager.getMessage("WIN_CONDITION_VALUE"));
    winConditionValueField = new TextField(getWinConditionValue());
    winConditionValueField.setPrefWidth(80);

    // Add change listener to update the label based on selected win condition type
    winConditionTypeComboBox.setOnAction(e -> updateWinConditionValueLabel());
  }

  /**
   * Creates UI components for lose condition selection
   */
  private void createLoseConditionComponents() {
    // Create lose condition type dropdown
    loseConditionTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(
            LOSE_CONDITION_LIVES_BASED));
    loseConditionTypeComboBox.setValue(getLoseConditionType());
    loseConditionTypeComboBox.setPrefWidth(150);

    // Create lose condition value field
    loseConditionValueLabel = new Label(LanguageManager.getMessage("LOSE_CONDITION_VALUE"));
    loseConditionValueField = new TextField(getLoseConditionValue());
    loseConditionValueField.setPrefWidth(80);

    // Add change listener to update the label based on selected lose condition type
    loseConditionTypeComboBox.setOnAction(e -> updateLoseConditionValueLabel());
  }

  private String getLoseConditionType() {
    LoseConditionInterface condition = gameSettings.loseCondition();

    // Use reflection to determine the condition type
    String className = condition.getClass().getSimpleName();
    // Remove "Record" suffix if present
    if (className.endsWith(RECORD_SUFFIX)) {
      className = className.substring(0, className.length() - RECORD_SUFFIX.length());
    }

    return className;
  }

  private String getLoseConditionValue() {
    return String.valueOf(gameSettings.startingLives());
  }

  private void updateLoseConditionValueLabel() {
    String selectedType = loseConditionTypeComboBox.getValue();
    if (LOSE_CONDITION_LIVES_BASED.equals(selectedType)) {
      loseConditionValueLabel.setText(LanguageManager.getMessage("LIVES_REMAINING"));
    }
  }

  private LoseConditionInterface createLoseConditionFromUI() {
    String typeName = loseConditionTypeComboBox.getValue();
    Class<?> conditionClass = loseConditionClasses.get(typeName);

    if (conditionClass != null) {
      try {
        // For LivesBasedConditionRecord, create a new instance using no-arg constructor
        return (LoseConditionInterface) conditionClass.getDeclaredConstructor().newInstance();
      } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
        LoggingManager.LOGGER.error("Error creating lose condition instance", e);
      }
    }

    // Default to lives-based condition
    return new LivesBasedConditionRecord();
  }

  private WinConditionInterface createWinConditionFromUI() {
    String typeName = winConditionTypeComboBox.getValue();
    String value = winConditionValueField.getText();
    Class<?> conditionClass = winConditionClasses.get(typeName);

    if (conditionClass != null) {
      try {
        if ("SurviveForTime".equals(typeName)) {
          // For SurviveForTimeConditionRecord, we need an int parameter
          try {
            int seconds = Integer.parseInt(value);
            Constructor<?> constructor = conditionClass.getDeclaredConstructor(int.class);
            return (WinConditionInterface) constructor.newInstance(seconds);
          } catch (NumberFormatException e) {
            // Default to 5 seconds if invalid input
            Constructor<?> constructor = conditionClass.getDeclaredConstructor(int.class);
            return (WinConditionInterface) constructor.newInstance(5);
          }
        } else if ("EntityBased".equals(typeName)) {
          // For EntityBasedConditionRecord, we need a String parameter
          Constructor<?> constructor = conditionClass.getDeclaredConstructor(String.class);
          return (WinConditionInterface) constructor.newInstance(value.isEmpty() ? "dot" : value);
        }
      } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
        LoggingManager.LOGGER.error("Error creating win condition instance", e);
      }
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

  /**
   * Utility method to scan a package for condition classes using reflection
   * This scans a directory for .class files, loads them, and checks if they implement
   * the specified interface type
   *
   * @param packagePath The full package path (e.g., "oogasalad.engine.records.config.model.wincondition")
   * @param interfaceType The interface class that the discovered classes should implement
   * @return A map of simplified class names to their Class objects
   */
  private Map<String, Class<?>> findConditionClasses(String packagePath, Class<?> interfaceType) {
    Map<String, Class<?>> classMap = new HashMap<>();

    try {
      // Convert package path to directory path for file system access
      // First determine the classpath root directory
      String classpathRoot = System.getProperty("user.dir") + "/target/classes/";
      // Then convert package dots to directory separators
      String directoryPath = classpathRoot + packagePath.replace('.', '/');

      // Use FileUtility to get all class files in the directory
      List<String> classNames = FileUtility.getFileNamesInDirectory(directoryPath, ".class");

      // Process each class file
      for (String className : classNames) {
        try {
          // Load the class using reflection
          Class<?> clazz = Class.forName(packagePath + "." + className);

          // Check if the class implements the required interface
          // and is not the interface itself or an abstract class
          if (interfaceType.isAssignableFrom(clazz) &&
                  !clazz.isInterface() &&
                  !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {

            // Extract the simple name for display purposes
            String simpleName = clazz.getSimpleName();
            if (simpleName.endsWith(RECORD_SUFFIX)) {
              simpleName = simpleName.substring(0, simpleName.length() - RECORD_SUFFIX.length());
            }

            // Add to our map
            classMap.put(simpleName, clazz);
            LoggingManager.LOGGER.info("Found condition class: {}", simpleName);
          }
        } catch (ClassNotFoundException e) {
          LoggingManager.LOGGER.error("Error loading class: " + className, e);
        } catch (Exception e) {
          LoggingManager.LOGGER.error("Unexpected error processing class: " + className, e);
        }
      }
    } catch (Exception e) {
      LoggingManager.LOGGER.error("Error scanning directory for condition classes", e);
    }

    // If no classes found, log a warning
    if (classMap.isEmpty()) {
      LoggingManager.LOGGER.warn("No condition classes found in package: {}", packagePath);
    }

    return classMap;
  }
}