package oogasalad.authoring.view;

import java.util.HashSet;
import java.util.Objects;
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
import oogasalad.authoring.view.mainView.AlertUtil;
import oogasalad.engine.config.CollisionRule;
import oogasalad.engine.records.config.model.SettingsRecord;
import oogasalad.engine.records.config.model.losecondition.LivesBasedConditionRecord;
import oogasalad.engine.records.config.model.losecondition.LoseConditionInterface;
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
import oogasalad.engine.utility.ThemeManager;
import oogasalad.engine.view.components.FormattingUtil;


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
   * Constructor initializes the view with the given controller.
   *
   * @param controller The controller of this view.
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
      Class<?> winConditionInterface = Class.forName(
          WIN_CONDITION_PACKAGE + ".WinConditionInterface");
      winConditionClasses.putAll(
          findConditionClasses(WIN_CONDITION_PACKAGE, winConditionInterface));

      Class<?> loseConditionInterface = Class.forName(
          LOSE_CONDITION_PACKAGE + ".LoseConditionInterface");
      loseConditionClasses.putAll(
          findConditionClasses(LOSE_CONDITION_PACKAGE, loseConditionInterface));

      if (winConditionClasses.isEmpty()) {
        LoggingManager.LOGGER.warn(
            "No win condition classes found via reflection. Using hardcoded defaults.");
        Class<?> surviveForTimeClass = Class.forName(
            WIN_CONDITION_PACKAGE + ".SurviveForTimeConditionRecord");
        Class<?> entityBasedClass = Class.forName(
            WIN_CONDITION_PACKAGE + ".EntityBasedConditionRecord");

        winConditionClasses.put(WIN_CONDITION_SURVIVE_FOR_TIME, surviveForTimeClass);
        winConditionClasses.put(WIN_CONDITION_ENTITY_BASED, entityBasedClass);
      }

      if (loseConditionClasses.isEmpty()) {
        LoggingManager.LOGGER.warn(
            "No lose condition classes found via reflection. Using hardcoded defaults.");
        Class<?> livesBasedClass = Class.forName(
            LOSE_CONDITION_PACKAGE + ".LivesBasedConditionRecord");
        loseConditionClasses.put(LOSE_CONDITION_LIVES_BASED, livesBasedClass);
      }

      LoggingManager.LOGGER.info("Loaded {} win condition classes and {} lose condition classes",
          winConditionClasses.size(), loseConditionClasses.size());

    } catch (ClassNotFoundException e) {
      LoggingManager.LOGGER.error("Error loading condition interfaces", e);

      try {
        Class<?> surviveForTimeClass = Class.forName(
            WIN_CONDITION_PACKAGE + ".SurviveForTimeConditionRecord");
        Class<?> entityBasedClass = Class.forName(
            WIN_CONDITION_PACKAGE + ".EntityBasedConditionRecord");
        Class<?> livesBasedClass = Class.forName(
            LOSE_CONDITION_PACKAGE + ".LivesBasedConditionRecord");

        winConditionClasses.put(WIN_CONDITION_SURVIVE_FOR_TIME, surviveForTimeClass);
        winConditionClasses.put(WIN_CONDITION_ENTITY_BASED, entityBasedClass);
        loseConditionClasses.put(LOSE_CONDITION_LIVES_BASED, livesBasedClass);
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
    gameTitleField = FormattingUtil.createTextField(controller.getModel().getGameTitle());
    gameTitleField.setPrefWidth(150);

    authorField = FormattingUtil.createTextField(controller.getModel().getAuthor());
    authorField.setPrefWidth(150);

    descriptionField = FormattingUtil.createTextField(controller.getModel().getGameDescription());
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
    settingsGrid.add(FormattingUtil.createLabel(LanguageManager.getMessage("GAME_TITLE")), 0, 0);
    settingsGrid.add(gameTitleField, 1, 0);
    settingsGrid.add(FormattingUtil.createLabel(LanguageManager.getMessage("AUTHOR")), 2, 0);
    settingsGrid.add(authorField, 3, 0);

    settingsGrid.add(FormattingUtil.createLabel(LanguageManager.getMessage("DESCRIPTION")), 0, 1);
    settingsGrid.add(descriptionField, 1, 1, 3, 1); // Span across multiple columns

    // Add game settings fields
    settingsGrid.add(FormattingUtil.createLabel(LanguageManager.getMessage("GAME_SPEED")), 0, 2);
    settingsGrid.add(gameSpeedSpinner, 1, 2);
    settingsGrid.add(FormattingUtil.createLabel(LanguageManager.getMessage("STARTING_LIVES")), 2, 2);
    settingsGrid.add(startingLivesSpinner, 3, 2);

    // Add more game settings
    settingsGrid.add(FormattingUtil.createLabel(LanguageManager.getMessage("INITIAL_SCORE")), 0, 3);
    settingsGrid.add(initialScoreSpinner, 1, 3);
    settingsGrid.add(FormattingUtil.createLabel(LanguageManager.getMessage("SCORE_STRATEGY")), 2, 3);

    // Add win condition fields
    settingsGrid.add(FormattingUtil.createLabel(LanguageManager.getMessage("WIN_CONDITION_TYPE")), 0, 4);
    settingsGrid.add(winConditionTypeComboBox, 1, 4);
    settingsGrid.add(winConditionValueLabel, 2, 4);
    settingsGrid.add(winConditionValueField, 3, 4);

    settingsGrid.add(FormattingUtil.createLabel(LanguageManager.getMessage("LOSE_CONDITION_TYPE")), 0, 5);
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
    return gameSettings.winCondition().getConditionType();
  }

  private String getWinConditionValue() {
    return gameSettings.winCondition()
        .getConditionValue()
        .orElse("5"); // fallback to default time
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

    CollisionRuleEditorView collisionEditor = new CollisionRuleEditorView(controller);

    Dialog<List<CollisionRule>> dialog = collisionEditor.getDialog();
    ThemeManager.getInstance().registerScene(dialog.getDialogPane().getScene());

    // Show and update model if confirmed
    collisionEditor.showAndWait()
        .ifPresent(updatedRules -> controller.getModel().setCollisionRules(updatedRules));
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
        newLoseCondition,

        //TODO
        new HashSet<>()
    );
    // Update the model with the current settings
    controller.getModel().setDefaultSettings(updatedSettings);

    // Show confirmation to user
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(LanguageManager.getMessage("SAVED"));
    alert.setHeaderText(null);
    alert.setContentText(LanguageManager.getMessage("GAME_SETTINGS_SAVED"));
    FormattingUtil.applyStandardDialogStyle(alert);
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
    winConditionValueField = FormattingUtil.createTextField(getWinConditionValue());
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
    loseConditionValueField = FormattingUtil.createTextField(getLoseConditionValue());
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
      } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
               InvocationTargetException e) {
        LoggingManager.LOGGER.error("Error creating lose condition instance", e);
      }
    }

    // Default to lives-based condition
    return new LivesBasedConditionRecord();
  }

  private WinConditionInterface createWinConditionFromUI() {
    String typeName = winConditionTypeComboBox.getValue();
    String value = winConditionValueField.getText();

    try {
      // Get the selected class from our map
      Class<?> conditionClass = winConditionClasses.get(typeName);
      if (conditionClass == null) {
        LoggingManager.LOGGER.warn("Win condition class not found for type: {}", typeName);
        return createDefaultWinCondition();
      }

      // Create the condition instance using the appropriate helper method
      return createWinConditionInstance(conditionClass, typeName, value);
    } catch (Exception e) {
      LoggingManager.LOGGER.error("Error creating win condition", e);
      return createDefaultWinCondition();
    }
  }

  /**
   * Helper method to create a win condition instance with the correct parameters
   *
   * @param conditionClass The class to instantiate
   * @param typeName       The type name (for parameter type determination)
   * @param value          The parameter value from the UI
   * @return A WinConditionInterface instance
   */
  private WinConditionInterface createWinConditionInstance(Class<?> conditionClass, String typeName,
      String value)
      throws ReflectiveOperationException {

    // Get parameter type and parsed value based on the condition type
    Object[] constructorArgs = getConstructorArgsForType(typeName, value);
    Class<?>[] paramTypes = getParameterTypesForType(typeName);

    // Get the constructor and create the instance
    Constructor<?> constructor = conditionClass.getDeclaredConstructor(paramTypes);
    return (WinConditionInterface) constructor.newInstance(constructorArgs);
  }

  /**
   * Gets the constructor parameter types for a given win condition type
   *
   * @param typeName The win condition type name
   * @return An array of parameter types
   */
  private Class<?>[] getParameterTypesForType(String typeName) {
    return switch (typeName) {
      case WIN_CONDITION_SURVIVE_FOR_TIME -> new Class<?>[]{int.class};
      case WIN_CONDITION_ENTITY_BASED -> new Class<?>[]{String.class};
      default -> new Class<?>[0];
    };
  }

  /**
   * Gets the constructor arguments for a given win condition type
   *
   * @param typeName The win condition type name
   * @param value    The value from the UI field
   * @return An array of constructor arguments
   */
  private Object[] getConstructorArgsForType(String typeName, String value) {
    return switch (typeName) {
      case WIN_CONDITION_SURVIVE_FOR_TIME -> new Object[]{parseIntWithDefault(value, 5)};
      case WIN_CONDITION_ENTITY_BASED -> new Object[]{value.isEmpty() ? "dot" : value};
      default -> new Object[0];
    };
  }

  /**
   * Parse an integer with a default value if parsing fails
   *
   * @param value        The string to parse
   * @param defaultValue The default value to use if parsing fails
   * @return The parsed integer or the default value
   */
  private int parseIntWithDefault(String value, int defaultValue) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * Creates a default win condition when other methods fail
   *
   * @return A default win condition
   */
  private WinConditionInterface createDefaultWinCondition() {
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
   * Utility method to scan a package for condition classes using reflection This scans a directory
   * for .class files, loads them, and checks if they implement the specified interface type
   *
   * @param packagePath   The full package path (e.g.,
   *                      "oogasalad.engine.records.config.model.wincondition")
   * @param interfaceType The interface class that the discovered classes should implement
   * @return A map of simplified class names to their Class objects
   */
  private Map<String, Class<?>> findConditionClasses(String packagePath, Class<?> interfaceType) {
    Map<String, Class<?>> classMap = new HashMap<>();
    try {
      // Get the directory path from the package name
      String directoryPath = getDirectoryPathForPackage(packagePath);

      // Get all class files in the directory
      List<String> classNames = getClassNamesInDirectory(directoryPath);

      // Process each class file
      processClassNames(packagePath, interfaceType, classNames, classMap);

      // Log warning if no classes found
      if (classMap.isEmpty()) {
        LoggingManager.LOGGER.warn("No condition classes found in package: {}", packagePath);
      }
    } catch (Exception e) {
      LoggingManager.LOGGER.error("Error scanning directory for condition classes", e);
    }
    return classMap;
  }

  /**
   * Gets the directory path corresponding to a package path
   *
   * @param packagePath The package path to convert
   * @return The corresponding directory path
   */
  private String getDirectoryPathForPackage(String packagePath) {
    // Convert package path to directory path for file system access
    String classpathRoot = System.getProperty("user.dir") + "/target/classes/";
    // Convert package dots to directory separators
    return classpathRoot + packagePath.replace('.', '/');
  }

  /**
   * Gets all class files in a directory
   *
   * @param directoryPath The directory to scan
   * @return A list of class names without the .class extension
   */
  private List<String> getClassNamesInDirectory(String directoryPath) {
    return FileUtility.getFileNamesInDirectory(directoryPath, ".class");
  }

  /**
   * Processes a list of class names, loading each class and checking if it's valid
   *
   * @param packagePath   The base package path
   * @param interfaceType The interface that classes should implement
   * @param classNames    The names of classes to process
   * @param classMap      The map to populate with valid classes
   */
  private void processClassNames(
      String packagePath,
      Class<?> interfaceType,
      List<String> classNames,
      Map<String, Class<?>> classMap) {

    for (String className : classNames) {
      try {
        Class<?> clazz = loadClass(packagePath, className);

        if (isValidConditionClass(clazz, interfaceType)) {
          String simpleName = getSimplifiedClassName(clazz);
          classMap.put(simpleName, clazz);
          LoggingManager.LOGGER.info("Found condition class: {}", simpleName);
        }
      } catch (ClassNotFoundException e) {
        LoggingManager.LOGGER.error("Error loading class: {}", className, e);
      } catch (Exception e) {
        LoggingManager.LOGGER.error("Unexpected error processing class: {}", className, e);
      }
    }
  }

  /**
   * Loads a class by name
   *
   * @param packagePath The package path
   * @param className   The class name to load
   * @return The loaded class
   * @throws ClassNotFoundException If the class cannot be found
   */
  private Class<?> loadClass(String packagePath, String className) throws ClassNotFoundException {
    return Class.forName(packagePath + "." + className);
  }

  /**
   * Checks if a class is a valid condition class
   *
   * @param clazz         The class to check
   * @param interfaceType The interface the class should implement
   * @return true if the class is valid, false otherwise
   */
  private boolean isValidConditionClass(Class<?> clazz, Class<?> interfaceType) {
    return interfaceType.isAssignableFrom(clazz) &&
        !clazz.isInterface() &&
        !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers());
  }

  /**
   * Gets a simplified class name for display
   *
   * @param clazz The class to get the name from
   * @return The simplified class name
   */
  private String getSimplifiedClassName(Class<?> clazz) {
    String simpleName = clazz.getSimpleName();
    if (simpleName.endsWith(RECORD_SUFFIX)) {
      simpleName = simpleName.substring(0, simpleName.length() - RECORD_SUFFIX.length());
    }
    return simpleName;
  }
}