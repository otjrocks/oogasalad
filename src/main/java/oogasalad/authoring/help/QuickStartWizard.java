package oogasalad.authoring.help;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;

import java.util.List;
import java.util.Map;

import javafx.stage.Modality;

/**
 * A quick start wizard that guides users through creating a simple game.
 * @author Angela Predolac
 */
public class QuickStartWizard {

    private final AuthoringController controller;
    private final Stage primaryStage;
    private Stage wizardStage;
    private int currentStep = 0;
    private final List<String> steps = List.of(
            "Welcome to the Quick Start Wizard!",
            "Setting Game Settings",
            "Creating the Player Entity",
            "Creating Enemies",
            "Creating Collectibles",
            "Setting Up Collision Rules",
            "Designing the Level",
            "Saving Your Game"
    );

    /**
     * Constructs a quick start wizard with the given controller and primary stage.
     *
     * @param controller the authoring controller
     * @param primaryStage the main application window
     */
    public QuickStartWizard(AuthoringController controller, Stage primaryStage) {
        this.controller = controller;
        this.primaryStage = primaryStage;
    }

    /**
     * Starts the quick start wizard.
     */
    public void start() {
        createWizardStage();
        showStep(0);
    }

    /**
     * Creates the wizard stage.
     */
    private void createWizardStage() {
        wizardStage = new Stage();
        wizardStage.setTitle("Quick Start Wizard");
        wizardStage.initOwner(primaryStage);
        wizardStage.initModality(Modality.NONE);

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label instructionLabel = new Label();
        instructionLabel.setWrapText(true);
        instructionLabel.setPrefWidth(400);

        Button prevButton = new Button("Previous");
        prevButton.setOnAction(e -> showStep(currentStep - 1));

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> showStep(currentStep + 1));

        Button skipButton = new Button("Skip Wizard");
        skipButton.setOnAction(e -> wizardStage.close());

        HBox buttonBar = new HBox(10, prevButton, nextButton, skipButton);
        buttonBar.setPadding(new Insets(10, 0, 0, 0));

        content.getChildren().addAll(titleLabel, instructionLabel, buttonBar);

        Scene scene = new Scene(content, 500, 300);
        wizardStage.setScene(scene);

        // Store references for later updates
        wizardStage.setUserData(Map.of(
                "titleLabel", titleLabel,
                "instructionLabel", instructionLabel,
                "prevButton", prevButton,
                "nextButton", nextButton
        ));
    }

    /**
     * Shows a specific step in the wizard.
     */
    private void showStep(int step) {
        if (step < 0) {
            step = 0;
        }

        if (step >= steps.size()) {
            wizardStage.close();
            return;
        }

        currentStep = step;

        // Update UI
        Map<String, Object> refs = (Map<String, Object>) wizardStage.getUserData();
        Label titleLabel = (Label) refs.get("titleLabel");
        Label instructionLabel = (Label) refs.get("instructionLabel");
        Button prevButton = (Button) refs.get("prevButton");
        Button nextButton = (Button) refs.get("nextButton");

        titleLabel.setText("Step " + (step + 1) + ": " + steps.get(step));
        instructionLabel.setText(getInstructionForStep(step));

        prevButton.setDisable(step == 0);
        nextButton.setText(step == steps.size() - 1 ? "Finish" : "Next");

        // Execute actions for this step
        executeStepAction(step);

        if (!wizardStage.isShowing()) {
            wizardStage.show();
        }
    }

    /**
     * Gets the instruction text for a specific step.
     */
    private String getInstructionForStep(int step) {
        return switch (step) {
            case 0 -> "Welcome to the Quick Start Wizard! This guide will help you create a simple game in just a few minutes. We'll set up the basic components like the player, enemies, collectibles, and rules. Click 'Next' to begin.";
            case 1 -> "Let's start by setting some basic game information. In the Game Settings panel at the bottom of the screen, enter a title for your game, your name as the author, and a brief description. You can also adjust starting lives and game speed.";
            case 2 -> "Now let's create a player entity. Click the '+ Add Entity Type' button in the Entity Selector. Rename this entity to 'Player'. You can customize its appearance and set it to be controlled by the player.";
            case 3 -> "Let's add some enemies. Click the '+ Add Entity Type' button again to create a new entity. Rename it to 'Enemy'. Set its movement speed and appearance to make it distinct from the player.";
            case 4 -> "Now add a collectible item by creating another entity. Name it 'Collectible' and give it an appropriate appearance. These will be items the player can gather for points.";
            case 5 -> "Time to set up collision rules. In the Game Settings panel, click 'Collision Rules'. Add rules for what happens when the player touches enemies (player loses life) and when the player touches collectibles (player gains points).";
            case 6 -> "Now design your level by dragging entities from the Entity Selector onto the Canvas. Place the player, some enemies, and collectibles to create a simple level layout.";
            case 7 -> "Your game is ready! Click 'File' and then 'Save Game' to save your creation. You can run it in the Engine to test it out. Congratulations on creating your first game!";
            default -> "Step instructions unavailable.";
        };
    }

    /**
     * Executes any actions associated with the current step.
     */
    private void executeStepAction(int step) {
        switch (step) {
            case 1 -> highlightNode("gameSettings");
            case 2 -> highlightNode("addEntityButton");
            case 3 -> highlightNode("entitySelector");
            case 4 -> highlightNode("entitySelector");
            case 5 -> highlightNode("collisionRulesButton");
            case 6 -> highlightNode("canvasView");
            case 7 -> highlightNode("fileMenu");
        }
    }

    /**
     * Highlights a specific node in the authoring UI.
     */
    private void highlightNode(String nodeId) {
        try {
            javafx.scene.Node node = controller.getView().getNode().getScene().lookup("#" + nodeId);
            if (node == null) return;

            // Create a brief glow effect on the target node
            javafx.scene.effect.Glow glow = new javafx.scene.effect.Glow(0.8);
            javafx.scene.effect.Effect originalEffect = node.getEffect();

            node.setEffect(glow);

            // Reset after a delay
            javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(javafx.util.Duration.seconds(3), e -> node.setEffect(originalEffect))
            );
            timeline.play();

        } catch (Exception e) {
            // Silently handle any issues with highlighting
        }
    }
}

