package oogasalad.authoring.help;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.view.AuthoringView;

import java.util.Arrays;
import java.util.List;

/**
 * A simple help system that displays documentation slides explaining
 * how the authoring environment works.
 *
 * @author Angela Predolac
 */
public class SimpleHelpSystem {

    private final AuthoringController authoringController;
    private final AuthoringView view;
    private final Stage primaryStage;
    private Stage helpStage;
    private int currentSlideIndex = 0;
    private List<HelpSlide> helpSlides;

    /**
     * Constructs a simple help system.
     *
     * @param controller the authoring controller
     * @param view the authoring view
     * @param primaryStage the main application window
     */
    public SimpleHelpSystem(AuthoringController controller, AuthoringView view, Stage primaryStage) {
        this.authoringController = controller;
        this.view = view;
        this.primaryStage = primaryStage;
        initializeHelpSlides();
    }

    /**
     * Adds a help button to the main view.
     */
    private void addHelpButton() {
        Button helpButton = new Button("?");
        helpButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-background-radius: 50%; -fx-min-width: 30px; " +
                "-fx-min-height: 30px; -fx-max-width: 30px; -fx-max-height: 30px;");
        helpButton.setTooltip(new Tooltip("Show help"));
        helpButton.setOnAction(e -> showHelpDialog());

        // Add the button to the top right corner of the view
        BorderPane rootPane = (BorderPane) view.getNode();
        StackPane.setAlignment(helpButton, Pos.TOP_RIGHT);
        StackPane.setMargin(helpButton, new Insets(10));

        // Create a stack pane to overlay the help button on the existing UI
        StackPane overlayPane = new StackPane();
        overlayPane.getChildren().addAll(rootPane.getCenter(), helpButton);
        rootPane.setCenter(overlayPane);
    }

    private void showHelpDialog() {
        if (helpStage != null && helpStage.isShowing()) {
            helpStage.requestFocus();
            return;
        }

        helpStage = new Stage();
        helpStage.initOwner(primaryStage);
        helpStage.initModality(Modality.NONE);
        helpStage.setTitle("Game Authoring Help");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Content area for slides
        StackPane slideContent = new StackPane();
        slideContent.setPrefSize(800, 500);
        slideContent.setPadding(new Insets(10));
        root.setCenter(slideContent);

        // Navigation buttons
        Button prevButton = new Button("Previous");
        prevButton.setOnAction(e -> showPreviousSlide());
        prevButton.setDisable(true);

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> showNextSlide());

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> helpStage.close());

        HBox navigationButtons = new HBox(10, prevButton, nextButton, closeButton);
        navigationButtons.setAlignment(Pos.CENTER_RIGHT);
        navigationButtons.setPadding(new Insets(10, 0, 0, 0));
        root.setBottom(navigationButtons);

        // Add slide numbers indicator
        Label slideNumberLabel = new Label("Slide 1 of " + helpSlides.size());
        HBox slideNumberBox = new HBox(slideNumberLabel);
        slideNumberBox.setAlignment(Pos.CENTER_LEFT);
        navigationButtons.getChildren().add(0, slideNumberBox);
        HBox.setHgrow(slideNumberBox, Priority.ALWAYS);

        Scene scene = new Scene(root, 800, 600);
        helpStage.setScene(scene);

        // Store references for updating
        helpStage.getProperties().put("slideContent", slideContent);
        helpStage.getProperties().put("prevButton", prevButton);
        helpStage.getProperties().put("nextButton", nextButton);
        helpStage.getProperties().put("slideNumberLabel", slideNumberLabel);

        // Show the first slide
        currentSlideIndex = 0;
        showCurrentSlide();

        helpStage.show();
    }

    /**
     * Shows the current slide in the help dialog.
     */
    private void showCurrentSlide() {
        if (helpStage == null || !helpStage.isShowing() || helpSlides.isEmpty()) {
            return;
        }

        StackPane slideContent = (StackPane) helpStage.getProperties().get("slideContent");
        Button prevButton = (Button) helpStage.getProperties().get("prevButton");
        Button nextButton = (Button) helpStage.getProperties().get("nextButton");
        Label slideNumberLabel = (Label) helpStage.getProperties().get("slideNumberLabel");

        // Clear previous content
        slideContent.getChildren().clear();

        // Add the current slide
        HelpSlide currentSlide = helpSlides.get(currentSlideIndex);
        slideContent.getChildren().add(currentSlide.createSlideNode());

        // Update navigation buttons
        prevButton.setDisable(currentSlideIndex == 0);
        nextButton.setDisable(currentSlideIndex == helpSlides.size() - 1);
        nextButton.setText(currentSlideIndex == helpSlides.size() - 1 ? "Finish" : "Next");

        // Update slide number
        slideNumberLabel.setText("Slide " + (currentSlideIndex + 1) + " of " + helpSlides.size());
    }

    private void showPreviousSlide() {
        if (currentSlideIndex > 0) {
            currentSlideIndex--;
            showCurrentSlide();
        }
    }
    private void showNextSlide() {
        if (currentSlideIndex < helpSlides.size() - 1) {
            currentSlideIndex++;
            showCurrentSlide();
        }
    }

    private void initializeHelpSlides() {
        helpSlides = Arrays.asList(
                // Welcome slide
                new HelpSlide(
                        "Welcome to the Game Authoring Environment",
                        "This help guide will walk you through the main features of the authoring environment and how to create your own games.",
                        null
                ),

                // Overview slide
                new HelpSlide(
                        "Overview",
                        "The Game Authoring Environment allows you to create 2D games without writing code. Key features include:\n\n" +
                                "• Entity-based game design\n" +
                                "• Visual level editor\n" +
                                "• Collision rule system\n" +
                                "• Mode-based entity state management\n" +
                                "• Configurable win and lose conditions",
                        "overview.png"
                ),

                // Main Interface slide
                new HelpSlide(
                        "Main Interface",
                        "The authoring environment consists of these main areas:\n\n" +
                                "1. Canvas (center) - Where you place game entities\n" +
                                "2. Entity Selector (right) - Contains available entity types\n" +
                                "3. Entity Editor (right) - Configure entity properties\n" +
                                "4. Level Selector (left) - Manage game levels\n" +
                                "5. Level Settings (left) - Configure level properties\n" +
                                "6. Game Settings (bottom) - Configure global game settings",
                        "interface.png"
                ),

                // Canvas View slide
                new HelpSlide(
                        "Canvas View",
                        "The Canvas is where you design your game levels by placing entities:\n\n" +
                                "• Drag entities from the Entity Selector onto the canvas\n" +
                                "• Click on placed entities to select and edit them\n" +
                                "• Right-click on the canvas for additional options\n" +
                                "• Delete key removes selected entities\n" +
                                "• The grid helps you align entities precisely",
                        "canvas.png"
                ),

                // Entity System slide
                new HelpSlide(
                        "Entity System",
                        "Entities are the building blocks of your game:\n\n" +
                                "• Create new entity types using the '+ Add Entity Type' button\n" +
                                "• Entity types can have multiple modes (states) with different appearances and behaviors\n" +
                                "• Configure movement speed, controls, and other properties\n" +
                                "• Place instances of entity types on the canvas to build your level",
                        "entities.png"
                ),

                // Entity Editor slide
                new HelpSlide(
                        "Entity Editor",
                        "The Entity Editor appears when you click on an entity type:\n\n" +
                                "• Change the entity name\n" +
                                "• Add new modes (different states like 'Default', 'Powered', etc.)\n" +
                                "• Set movement speed\n" +
                                "• Choose sprite images for each mode\n" +
                                "• Configure control settings (player-controlled or not)",
                        "entity_editor.png"
                ),

                // Level System slide
                new HelpSlide(
                        "Level System",
                        "The Level System lets you create and manage multiple game levels:\n\n" +
                                "• Add new levels with the 'Add Level' button\n" +
                                "• Switch between levels using the dropdown\n" +
                                "• Configure level dimensions (width and height)\n" +
                                "• Set up mode change events triggered during gameplay\n" +
                                "• Configure entity spawn events based on conditions",
                        "levels.png"
                ),

                // Collision Rules slide
                new HelpSlide(
                        "Collision Rules",
                        "Collision Rules define what happens when entities interact:\n\n" +
                                "• Access from the Game Settings panel ('Collision Rules' button)\n" +
                                "• Define rules between pairs of entity types\n" +
                                "• Set actions for each entity involved (destroy, add points, change mode, etc.)\n" +
                                "• Mode-specific rules allow for different behaviors in different states",
                        "collisions.png"
                ),

                // Game Settings slide
                new HelpSlide(
                        "Game Settings",
                        "Game Settings control global game properties:\n\n" +
                                "• Set game title, author, and description\n" +
                                "• Configure overall game speed\n" +
                                "• Set starting lives and initial score\n" +
                                "• Choose score strategy (how points accumulate)\n" +
                                "• Define win and lose conditions",
                        "settings.png"
                ),

                // Creating Your First Game slide
                new HelpSlide(
                        "Creating Your First Game",
                        "Basic steps to create a simple game:\n\n" +
                                "1. Create entity types (player, enemies, collectibles, obstacles)\n" +
                                "2. Configure their appearance and properties\n" +
                                "3. Set up collision rules between them\n" +
                                "4. Place entities on the canvas to design levels\n" +
                                "5. Configure win/lose conditions\n" +
                                "6. Save your game and test it in the Engine",
                        "first_game.png"
                ),

                // Tips and Tricks slide
                new HelpSlide(
                        "Tips and Tricks",
                        "Helpful tips for efficient game authoring:\n\n" +
                                "• Use meaningful names for entity types and levels\n" +
                                "• Start with a simple game to learn the basics\n" +
                                "• Test your game frequently during development\n" +
                                "• Create reusable entity types to save time\n" +
                                "• Use right-click context menus for quick actions\n" +
                                "• Save your work often!",
                        null
                ),

                // Conclusion slide
                new HelpSlide(
                        "Ready to Create!",
                        "You now know the basics of the Game Authoring Environment!\n\n" +
                                "Remember that game development is an iterative process. Start simple, test often, and gradually add complexity as you become more comfortable with the tools.\n\n" +
                                "Happy game creating!",
                        null
                )
        );
    }
}
