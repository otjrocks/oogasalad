package oogasalad.authoring.view;

import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.model.GameSettings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * GameSettingsView provides a compact form for editing game settings
 * Designed to fit at the bottom of the screen as shown in the wireframe
 *
 * @author Angela Predolac
 */
public class GameSettingsView {

    private static final double DEFAULT_PADDING = 5;
    private static final double DEFAULT_SPACING = 5;

    private AuthoringController controller;
    private GameSettings gameSettings;

    // Root node containing the view
    private HBox rootNode;

    // UI Components
    private Spinner<Double> gameSpeedSpinner;
    private Spinner<Integer> startingLivesSpinner;
    private Spinner<Integer> initialScoreSpinner;
    private ComboBox<String> edgePolicyComboBox;
    private Spinner<Integer> widthSpinner;
    private Spinner<Integer> heightSpinner;

    /**
     * Constructor initializes the view with the given controller
     */
    public GameSettingsView(AuthoringController controller) {
        this.controller = controller;

        this.gameSettings = controller.getModel().getDefaultSettings();

        this.rootNode = new HBox();
        setupUI();
//        bindToModel();
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
        Label titleLabel = new Label("Game Settings");
        titleLabel.getStyleClass().add("settings-title");
        titleLabel.setPrefWidth(120);

        // Create compact grid layout for settings
        GridPane settingsGrid = new GridPane();
        settingsGrid.setHgap(8);
        settingsGrid.setVgap(5);
        settingsGrid.setPadding(new Insets(0));

        // Create compact spinners and combo boxes
        gameSpeedSpinner = createDoubleSpinner(0.5, 3.0, 0.1, 0.5);
        startingLivesSpinner = createIntegerSpinner(1, 10, 1, 1);
        initialScoreSpinner = createIntegerSpinner(0, 1000, 50, 0);
        edgePolicyComboBox = new ComboBox<>(FXCollections.observableArrayList("Wrap", "Stop", "Bounce"));
        edgePolicyComboBox.setPrefWidth(120);

        // Add first row of settings
        settingsGrid.add(new Label("Game Speed:"), 0, 0);
        settingsGrid.add(gameSpeedSpinner, 1, 0);
        settingsGrid.add(new Label("Starting Lives:"), 2, 0);
        settingsGrid.add(startingLivesSpinner, 3, 0);

        // Add second row of settings
        settingsGrid.add(new Label("Initial Score:"), 0, 1);
        settingsGrid.add(initialScoreSpinner, 1, 1);
        settingsGrid.add(new Label("Edge Policy:"), 2, 1);
        settingsGrid.add(edgePolicyComboBox, 3, 1);

        Button saveButton = new Button("Save Settings");
        saveButton.setOnAction(e -> saveSettings());
        saveButton.setPrefWidth(130);
        saveButton.setMinWidth(130);
        saveButton.setStyle("-fx-padding: 5px 12px; -fx-font-size: 12px;");

        Button collisionRulesButton = new Button("Collision Rules");
        collisionRulesButton.setOnAction(e -> showCollisionRulesPopup());
        collisionRulesButton.setPrefWidth(130);
        collisionRulesButton.setMinWidth(130);
        collisionRulesButton.setStyle("-fx-padding: 5px 12px; -fx-font-size: 12px;");

        // Create button container with more space between buttons
        HBox buttonBox = new HBox(15, saveButton, collisionRulesButton);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        rootNode.getChildren().addAll(titleLabel, settingsGrid, buttonBox);
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
        popupStage.setTitle("Collision Rules Editor");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        // Create a new CollisionRuleEditorView
        CollisionRuleEditorView collisionEditor = new CollisionRuleEditorView(controller);
        Node collisionEditorNode = collisionEditor.getNode();

        // Create a scene with the collision editor
        Scene scene = new Scene(new VBox(collisionEditorNode), 600, 400);

        // Set the scene and show the popup
        popupStage.setScene(scene);
        popupStage.showAndWait();
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
        edgePolicyComboBox.setValue(gameSettings.edgePolicy());
    }

//    /**
//     * Set up listeners to update the model when UI elements change
//     */
//    private void bindToModel() {
//        // Listen for UI changes and update the model accordingly
//        gameSpeedSpinner.valueProperty().addListener((obs, oldVal, newVal) ->
//                gameSettings.setGameSpeed(newVal));
//
//        startingLivesSpinner.valueProperty().addListener((obs, oldVal, newVal) ->
//                gameSettings.setStartingLives(newVal));
//
//        initialScoreSpinner.valueProperty().addListener((obs, oldVal, newVal) ->
//                gameSettings.setInitialScore(newVal));
//
//        edgePolicyComboBox.valueProperty().addListener((obs, oldVal, newVal) ->
//                gameSettings.setEdgePolicy(newVal));
//
//        // Initialize values from the model
//        updateFromModel();
//    }

    /**
     * Save the current settings to the controller
     */
    private void saveSettings() {
        // Commit any edited values in spinners
        commitSpinnerValues();

        // Update the model with the current settings
        controller.getModel().setDefaultSettings(gameSettings);

        // Show confirmation to user
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings Saved");
        alert.setHeaderText(null);
        alert.setContentText("Game settings have been saved successfully.");
        alert.showAndWait();
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
        if (!spinner.isEditable()) return;

        try {
            String text = spinner.getEditor().getText();
            double value = Double.parseDouble(text);
            spinner.getValueFactory().setValue(value);
        } catch (NumberFormatException e) {
            spinner.getValueFactory().setValue(defaultValue);
        }
    }

    /**
     * Helper method to safely commit an integer spinner value
     */
    private void commitIntegerSpinnerValue(Spinner<Integer> spinner, int defaultValue) {
        if (!spinner.isEditable()) return;

        try {
            String text = spinner.getEditor().getText();
            int value = Integer.parseInt(text);
            spinner.getValueFactory().setValue(value);
        } catch (NumberFormatException e) {
            spinner.getValueFactory().setValue(defaultValue);
        }
    }
}