package oogasalad.authoring.help;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.help.templates.EmptyTemplate;
import oogasalad.authoring.help.templates.GameTemplate;

import javafx.scene.control.Label;
import java.awt.TextField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Dialog that allows users to create a custom template or select from predefined templates.
 * @author Angela Predolac
 */
public class GameTemplateWizard {

    private final AuthoringController controller;
    private final Stage primaryStage;
    private final Dialog<GameTemplate> dialog;
    private final List<GameTemplate> availableTemplates;

    /**
     * Constructs a wizard for selecting or creating a game template.
     *
     * @param controller the authoring controller
     * @param primaryStage the main application window
     */
    public GameTemplateWizard(AuthoringController controller, Stage primaryStage) {
        this.controller = controller;
        this.primaryStage = primaryStage;
        this.dialog = new Dialog<>();
        this.availableTemplates = new ArrayList<>();

        initializeTemplates();
        setupDialog();
    }

    /**
     * Shows the wizard dialog and waits for user input.
     *
     * @return Optional containing the selected template, or empty if cancelled
     */
    public Optional<GameTemplate> showAndWait() {
        return dialog.showAndWait();
    }

    /**
     * Initializes the list of available templates.
     */
    private void initializeTemplates() {
        availableTemplates.add(new EmptyTemplate());
        availableTemplates.add(GameTemplateFactory.createTemplate("pacman"));
        availableTemplates.add(GameTemplateFactory.createTemplate("platform"));
        availableTemplates.add(GameTemplateFactory.createTemplate("shooter"));
        availableTemplates.add(GameTemplateFactory.createTemplate("puzzle"));
    }

    /**
     * Sets up the dialog UI.
     */
    private void setupDialog() {
        dialog.setTitle("Game Template Wizard");
        dialog.setHeaderText("Choose a template for your game");
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Create the UI
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        Label instructionLabel = new Label("Select a pre-defined template or create a custom one:");

        // Template list
        ListView<GameTemplate> templateList = new ListView<>(FXCollections.observableArrayList(availableTemplates));
        templateList.setPrefHeight(200);
        templateList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(GameTemplate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getDescription());
                }
            }
        });

        // Custom template configuration
        TitledPane customPane = new TitledPane("Custom Template", createCustomTemplatePane());
        customPane.setExpanded(false);

        content.getChildren().addAll(instructionLabel, templateList, customPane);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Set the result converter
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return templateList.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        // Select the first template by default
        templateList.getSelectionModel().select(0);
    }

    /**
     * Creates the pane for configuring a custom template.
     */
    private GridPane createCustomTemplatePane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        // Game title
        Label titleLabel = new Label("Game Title:");
        TextField titleField = new TextField("My Custom Game");
        grid.add(titleLabel, 0, 0);
        //grid.add(titleField, 1, 0);

        // Author
        Label authorLabel = new Label("Author:");
        TextField authorField = new TextField("");
        grid.add(authorLabel, 0, 1);
        //grid.add(authorField, 1, 1);

        // Description
        Label descLabel = new Label("Description:");
        TextField descField = new TextField("A custom game template");
        grid.add(descLabel, 0, 2);
        //grid.add(descField, 1, 2);

        // Level dimensions
        Label dimensionsLabel = new Label("Level Size:");
        HBox dimensionsBox = new HBox(5);
        Spinner<Integer> widthSpinner = new Spinner<>(5, 50, 20);
        widthSpinner.setEditable(true);
        Spinner<Integer> heightSpinner = new Spinner<>(5, 50, 15);
        heightSpinner.setEditable(true);
        dimensionsBox.getChildren().addAll(widthSpinner, new Label("x"), heightSpinner);
        grid.add(dimensionsLabel, 0, 3);
        grid.add(dimensionsBox, 1, 3);

        // Entities to include
        Label entitiesLabel = new Label("Include Entities:");
        VBox entitiesBox = new VBox(5);
        CheckBox playerCheck = new CheckBox("Player");
        playerCheck.setSelected(true);
        CheckBox enemyCheck = new CheckBox("Enemies");
        enemyCheck.setSelected(true);
        CheckBox collectibleCheck = new CheckBox("Collectibles");
        collectibleCheck.setSelected(true);
        CheckBox obstacleCheck = new CheckBox("Obstacles");
        obstacleCheck.setSelected(true);
        entitiesBox.getChildren().addAll(playerCheck, enemyCheck, collectibleCheck, obstacleCheck);
        grid.add(entitiesLabel, 0, 4);
        grid.add(entitiesBox, 1, 4);

        return grid;
    }
}
