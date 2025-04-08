package oogasalad.authoring.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.model.GameSettings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * GameSettingsView provides a form-style UI for editing game settings
 * such as game speed, starting lives, initial score, edge policy, width, and height.
 * Also includes a button to open the CollisionRuleEditorView as a popup.
 * Uses composition rather than inheritance for JavaFX components.
 *
 * @author Angela Predolac
 */
public class GameSettingsView {

    private static final String TITLE = "Game Settings";
    private static final double DEFAULT_SPACING = 10;
    private static final double DEFAULT_PADDING = 15;

    private static final double MIN_GAME_SPEED = 0.5;
    private static final double MAX_GAME_SPEED = 3.0;
    private static final double GAME_SPEED_STEP = 0.1;

    private static final int MIN_LIVES = 1;
    private static final int MAX_LIVES = 10;

    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 1000;
    private static final int SCORE_STEP = 50;

    private static final int MIN_SIZE = 200;
    private static final int MAX_SIZE = 2000;
    private static final int SIZE_STEP = 50;

    private static final String[] EDGE_POLICIES = {"Wrap", "Stop", "Bounce"};

    private final AuthoringController controller;
    private GameSettings gameSettings;

    // Root node containing the view
    private final VBox rootNode;

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
     * Set up the UI components
     */
    private void setupUI() {
        // Setup the main container
        rootNode.setSpacing(DEFAULT_SPACING);
        rootNode.setPadding(new Insets(DEFAULT_PADDING));
        rootNode.getStyleClass().add("game-settings-view");

        // Create a title label
        Label titleLabel = new Label(TITLE);
        titleLabel.getStyleClass().add("settings-title");

        // Create a grid for form fields
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));
        formGrid.getStyleClass().add("grid-pane");

        // Game Speed
        Label gameSpeedLabel = new Label("Game Speed:");
        gameSpeedSpinner = createDoubleSpinner(MIN_GAME_SPEED, MAX_GAME_SPEED, GAME_SPEED_STEP);
        formGrid.add(gameSpeedLabel, 0, 0);
        formGrid.add(gameSpeedSpinner, 1, 0);

        // Starting Lives
        Label startingLivesLabel = new Label("Starting Lives:");
        startingLivesSpinner = createIntegerSpinner(MIN_LIVES, MAX_LIVES, 1);
        formGrid.add(startingLivesLabel, 0, 1);
        formGrid.add(startingLivesSpinner, 1, 1);

        // Initial Score
        Label initialScoreLabel = new Label("Initial Score:");
        initialScoreSpinner = createIntegerSpinner(MIN_SCORE, MAX_SCORE, SCORE_STEP);
        formGrid.add(initialScoreLabel, 0, 2);
        formGrid.add(initialScoreSpinner, 1, 2);

        // Edge Policy
        Label edgePolicyLabel = new Label("Edge Policy:");
        edgePolicyComboBox = new ComboBox<>(FXCollections.observableArrayList(EDGE_POLICIES));
        edgePolicyComboBox.setPrefWidth(150);
        formGrid.add(edgePolicyLabel, 0, 3);
        formGrid.add(edgePolicyComboBox, 1, 3);

        // Width
        Label widthLabel = new Label("Game Width:");
        widthSpinner = createIntegerSpinner(MIN_SIZE, MAX_SIZE, SIZE_STEP);
        formGrid.add(widthLabel, 0, 4);
        formGrid.add(widthSpinner, 1, 4);

        // Height
        Label heightLabel = new Label("Game Height:");
        heightSpinner = createIntegerSpinner(MIN_SIZE, MAX_SIZE, SIZE_STEP);
        formGrid.add(heightLabel, 0, 5);
        formGrid.add(heightSpinner, 1, 5);

        // Button container for save and collision rules
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);

        // Add save button
        Button saveButton = new Button("Save Settings");
        saveButton.setOnAction(e -> saveSettings());

        // Add collision rules button
        Button collisionRulesButton = new Button("Collision Rules");
        collisionRulesButton.setOnAction(e -> showCollisionRulesPopup());
        collisionRulesButton.getStyleClass().add("collision-button");

        buttonContainer.getChildren().addAll(saveButton, collisionRulesButton);

        // Add all components to the view
        rootNode.getChildren().addAll(titleLabel, formGrid, buttonContainer);
    }

    /**
     * Display the CollisionRuleEditorView in a popup window
     */
    private void showCollisionRulesPopup() {
        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.setTitle("Collision Rules Editor");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        // Check if the CollisionRuleEditorView already exists in the view
        // If not, assume we need to create it
        Node collisionEditorNode;
        if (controller.getView().getCollisionEditorView() != null) {
            collisionEditorNode = controller.getView().getCollisionEditorView().getNode();
        } else {
            CollisionRuleEditorView collisionEditor = new CollisionRuleEditorView(controller);
            collisionEditorNode = collisionEditor.getNode();
        }

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
        spinner.setPrefWidth(150);

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
        spinner.setPrefWidth(150);

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
