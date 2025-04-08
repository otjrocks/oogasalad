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
import javafx.util.StringConverter;

/**
 * GameSettingsView provides a form-style UI for editing game settings
 * such as game speed, starting lives, initial score, edge policy, width, and height.
 * Includes a button to open a CollisionRuleEditorView popup.
 *
 * @author Angela Predolac
 */
public class GameSettingsView {

    private static final double DEFAULT_SPACING = 10;
    private static final double DEFAULT_PADDING = 15;

    private static final double MIN_GAME_SPEED = 0.5;
    private static final double MAX_GAME_SPEED = 3.0;
    private static final double GAME_SPEED_STEP = 0.1;

    private static final int MIN_LIVES = 1;
    private static final int MAX_LIVES = 10;

    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 1000;

    private static final int MIN_SIZE = 200;
    private static final int MAX_SIZE = 2000;
    private static final int SIZE_STEP = 50;

    private static final String[] EDGE_POLICIES = {"Wrap", "Stop", "Bounce"};

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

        // Get the current game settings from the model
        this.gameSettings = controller.getModel().getDefaultSettings();

        // Create the UI
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
     * Set up the UI components
     */
    private void setupUI() {
        // Setup the main container - horizontal layout to match the image
        rootNode.setSpacing(DEFAULT_SPACING);
        rootNode.setPadding(new Insets(DEFAULT_PADDING));
        rootNode.setAlignment(Pos.CENTER_LEFT);
        rootNode.getStyleClass().add("game-settings-view");

        // Label for the view
        Label titleLabel = new Label("Game Settings");
        titleLabel.getStyleClass().add("settings-title");

        // Create a grid for form fields - more compact to fit at bottom of window
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(5));

        // Game Speed
        formGrid.add(new Label("Game Speed:"), 0, 0);
        gameSpeedSpinner = createDoubleSpinner(MIN_GAME_SPEED, MAX_GAME_SPEED, GAME_SPEED_STEP);
        formGrid.add(gameSpeedSpinner, 1, 0);

        // Starting Lives
        formGrid.add(new Label("Starting Lives:"), 2, 0);
        startingLivesSpinner = createIntegerSpinner(MIN_LIVES, MAX_LIVES, 1);
        formGrid.add(startingLivesSpinner, 3, 0);

        // Initial Score
        formGrid.add(new Label("Initial Score:"), 0, 1);
        initialScoreSpinner = createIntegerSpinner(MIN_SCORE, MAX_SCORE, 50);
        formGrid.add(initialScoreSpinner, 1, 1);

        // Edge Policy
        formGrid.add(new Label("Edge Policy:"), 2, 1);
        edgePolicyComboBox = new ComboBox<>(FXCollections.observableArrayList(EDGE_POLICIES));
        formGrid.add(edgePolicyComboBox, 3, 1);

        // Width and Height
        formGrid.add(new Label("Width:"), 0, 2);
        widthSpinner = createIntegerSpinner(MIN_SIZE, MAX_SIZE, SIZE_STEP);
        formGrid.add(widthSpinner, 1, 2);

        formGrid.add(new Label("Height:"), 2, 2);
        heightSpinner = createIntegerSpinner(MIN_SIZE, MAX_SIZE, SIZE_STEP);
        formGrid.add(heightSpinner, 3, 2);

        // Buttons
        Button saveButton = new Button("Save Settings");
        saveButton.setOnAction(e -> saveSettings());

        Button collisionRulesButton = new Button("Collision Rules");
        collisionRulesButton.setOnAction(e -> showCollisionRulesPopup());

        HBox buttonBox = new HBox(10, saveButton, collisionRulesButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Add components to root
        VBox formAndButtons = new VBox(5, formGrid, buttonBox);
        formAndButtons.setAlignment(Pos.CENTER);

        rootNode.getChildren().addAll(titleLabel, formAndButtons);
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
     * Create a spinner for double values
     */
    private Spinner<Double> createDoubleSpinner(double min, double max, double step) {
        SpinnerValueFactory.DoubleSpinnerValueFactory factory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, min, step);

        Spinner<Double> spinner = new Spinner<>(factory);
        spinner.setEditable(true);
        spinner.setPrefWidth(100);

        // Format to show only one decimal place
        StringConverter<Double> converter = new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                return String.format("%.1f", value);
            }

            @Override
            public Double fromString(String string) {
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    return min;
                }
            }
        };

        factory.setConverter(converter);

        return spinner;
    }

    /**
     * Create a spinner for integer values
     */
    private Spinner<Integer> createIntegerSpinner(int min, int max, int step) {
        SpinnerValueFactory.IntegerSpinnerValueFactory factory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, min, step);

        Spinner<Integer> spinner = new Spinner<>(factory);
        spinner.setEditable(true);
        spinner.setPrefWidth(100);

        return spinner;
    }

    /**
     * Update the view based on the current model
     */
    public void updateFromModel() {
        // Get the latest settings from the model
        this.gameSettings = controller.getModel().getDefaultSettings();

        // Update UI elements with model values
        gameSpeedSpinner.getValueFactory().setValue(gameSettings.getGameSpeed());
        startingLivesSpinner.getValueFactory().setValue(gameSettings.getStartingLives());
        initialScoreSpinner.getValueFactory().setValue(gameSettings.getInitialScore());
        edgePolicyComboBox.setValue(gameSettings.getEdgePolicy());
        widthSpinner.getValueFactory().setValue(gameSettings.getWidth());
        heightSpinner.getValueFactory().setValue(gameSettings.getHeight());
    }

    /**
     * Set up listeners to update the model when UI elements change
     */
    private void bindToModel() {
        // Listen for UI changes and update the model accordingly
        gameSpeedSpinner.valueProperty().addListener((obs, oldVal, newVal) ->
                gameSettings.setGameSpeed(newVal));

        startingLivesSpinner.valueProperty().addListener((obs, oldVal, newVal) ->
                gameSettings.setStartingLives(newVal));

        initialScoreSpinner.valueProperty().addListener((obs, oldVal, newVal) ->
                gameSettings.setInitialScore(newVal));

        edgePolicyComboBox.valueProperty().addListener((obs, oldVal, newVal) ->
                gameSettings.setEdgePolicy(newVal));

        widthSpinner.valueProperty().addListener((obs, oldVal, newVal) ->
                gameSettings.setWidth(newVal));

        heightSpinner.valueProperty().addListener((obs, oldVal, newVal) ->
                gameSettings.setHeight(newVal));

        // Initialize values from the model
        updateFromModel();
    }

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
        if (gameSpeedSpinner.isEditable()) {
            String text = gameSpeedSpinner.getEditor().getText();
            try {
                double value = Double.parseDouble(text);
                gameSpeedSpinner.getValueFactory().setValue(value);
            } catch (NumberFormatException e) {
                // Reset to current value
                gameSpeedSpinner.getEditor().setText(gameSpeedSpinner.getValue().toString());
            }
        }

        if (startingLivesSpinner.isEditable()) {
            String text = startingLivesSpinner.getEditor().getText();
            try {
                int value = Integer.parseInt(text);
                startingLivesSpinner.getValueFactory().setValue(value);
            } catch (NumberFormatException e) {
                startingLivesSpinner.getEditor().setText(startingLivesSpinner.getValue().toString());
            }
        }

        if (initialScoreSpinner.isEditable()) {
            String text = initialScoreSpinner.getEditor().getText();
            try {
                int value = Integer.parseInt(text);
                initialScoreSpinner.getValueFactory().setValue(value);
            } catch (NumberFormatException e) {
                initialScoreSpinner.getEditor().setText(initialScoreSpinner.getValue().toString());
            }
        }

        if (widthSpinner.isEditable()) {
            String text = widthSpinner.getEditor().getText();
            try {
                int value = Integer.parseInt(text);
                widthSpinner.getValueFactory().setValue(value);
            } catch (NumberFormatException e) {
                widthSpinner.getEditor().setText(widthSpinner.getValue().toString());
            }
        }

        if (heightSpinner.isEditable()) {
            String text = heightSpinner.getEditor().getText();
            try {
                int value = Integer.parseInt(text);
                heightSpinner.getValueFactory().setValue(value);
            } catch (NumberFormatException e) {
                heightSpinner.getEditor().setText(heightSpinner.getValue().toString());
            }
        }
    }
}