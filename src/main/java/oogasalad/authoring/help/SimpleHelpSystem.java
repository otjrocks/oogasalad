package oogasalad.authoring.help;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.view.mainView.AuthoringView;

import java.util.Arrays;
import java.util.List;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.ThemeManager;
import oogasalad.engine.view.components.FormattingUtil;

/**
 * A simple help system that displays documentation slides explaining how the authoring environment
 * works.
 *
 * @author Angela Predolac
 */
public class SimpleHelpSystem {

  private final Stage primaryStage;
  private Stage helpStage;
  private int currentSlideIndex = 0;
  private List<HelpSlide> helpSlides;

  /**
   * Constructs a simple help system.
   *
   * @param controller   the authoring controller
   * @param view         the authoring view
   * @param primaryStage the main application window
   */
  public SimpleHelpSystem(AuthoringController controller, AuthoringView view, Stage primaryStage) {
    this.primaryStage = primaryStage;
    initializeHelpSlides();
  }

  /**
   * Displays the help dialog.
   */
  public void showHelpDialog() {
    if (helpStage != null && helpStage.isShowing()) {
      helpStage.requestFocus();
      return;
    }

    helpStage = new Stage();
    helpStage.initOwner(primaryStage);
    helpStage.initModality(Modality.NONE);
    helpStage.setTitle(LanguageManager.getMessage("GAME_AUTHORING_HELP"));

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.getStyleClass().add("main-content");

    // Content area for slides
    StackPane slideContent = new StackPane();
    slideContent.getStyleClass().add("canvas-view");
    slideContent.setPrefSize(800, 500);
    slideContent.setPadding(new Insets(10));
    root.setCenter(slideContent);

    // Navigation buttons
    Button prevButton = FormattingUtil.createSmallButton(LanguageManager.getMessage("PREVIOUS"));
    Button nextButton = FormattingUtil.createSmallButton(LanguageManager.getMessage("NEXT"));
    Button closeButton = FormattingUtil.createSmallButton(LanguageManager.getMessage("CLOSE"));

    prevButton.setOnAction(e -> showPreviousSlide());
    prevButton.setDisable(true);

    nextButton.setOnAction(e -> showNextSlide());

    closeButton.setOnAction(e -> helpStage.close());

    HBox navigationButtons = new HBox(10, prevButton, nextButton, closeButton);
    navigationButtons.setAlignment(Pos.CENTER_RIGHT);
    navigationButtons.setPadding(new Insets(10, 0, 0, 0));
    root.setBottom(navigationButtons);

    // Add slide numbers indicator
    // Show the first slide
    currentSlideIndex = 0;

    Label slideNumberLabel = FormattingUtil.createHeading(
        String.format(LanguageManager.getMessage("SLIDE_NUMBER"), currentSlideIndex + 1,
            helpSlides.size()));
    HBox slideNumberBox = new HBox(slideNumberLabel);
    slideNumberBox.setAlignment(Pos.CENTER_LEFT);
    navigationButtons.getChildren().add(0, slideNumberBox);
    HBox.setHgrow(slideNumberBox, Priority.ALWAYS);

    Scene scene = new Scene(root, 800, 600);
    helpStage.setScene(scene);
    ThemeManager.getInstance().registerScene(scene);

    // Store references for updating
    helpStage.getProperties().put("slideContent", slideContent);
    helpStage.getProperties().put("prevButton", prevButton);
    helpStage.getProperties().put("nextButton", nextButton);
    helpStage.getProperties().put("slideNumberLabel", slideNumberLabel);

    // Show the stage first, then update the slide content
    helpStage.show();

    // Add the first slide to the content pane
    if (!helpSlides.isEmpty()) {
      HelpSlide firstSlide = helpSlides.getFirst();
      slideContent.getChildren().add(firstSlide.createSlideNode());
    }
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
    nextButton.setText(currentSlideIndex == helpSlides.size() - 1 ? LanguageManager.getMessage("CLOSE") : LanguageManager.getMessage("NEXT"));

    // Update slide number

    slideNumberLabel.setText(String.format(LanguageManager.getMessage("SLIDE_NUMBER"), currentSlideIndex + 1,
        helpSlides.size()));
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
            """
                The Game Authoring Environment allows you to create 2D games without writing code. Key features include:

                • Entity-based game design
                • Visual level editor
                • Collision rule system
                • Mode-based entity state management
                • Configurable win and lose conditions""",
            "overview.png"
        ),

        // Main Interface slide
        new HelpSlide(
            "Main Interface",
            """
                The authoring environment consists of these main areas:

                1. Canvas (center) - Where you place game entities
                2. Entity Selector (right) - Contains available entity types
                3. Entity Editor (right) - Configure entity properties
                4. Level Selector (left) - Manage game levels
                5. Level Settings (left) - Configure level properties
                6. Game Settings (bottom) - Configure global game settings""",
            "interface.png"
        ),

        // Canvas View slide
        new HelpSlide(
            "Canvas View",
            """
                The Canvas is where you design your game levels by placing entities:

                • Drag entities from the Entity Selector onto the canvas
                • Click on placed entities to select and edit them
                • Right-click on the canvas for additional options
                • Delete key removes selected entities
                • The grid helps you align entities precisely""",
            "canvas.png"
        ),

        // Entity System slide
        new HelpSlide(
            "Entity System",
            """
                Entities are the building blocks of your game:

                • Create new entity types using the '+ Add Entity Type' button
                • Entity types can have multiple modes (states) with different appearances and behaviors
                • Configure movement speed, controls, and other properties
                • Place instances of entity types on the canvas to build your level""",
            "entities.png"
        ),

        // Entity Editor slide
        new HelpSlide(
            "Entity Editor",
            """
                The Entity Editor appears when you click on an entity type:

                • Change the entity name
                • Add new modes (different states like 'Default', 'Powered', etc.)
                • Set movement speed
                • Choose sprite images for each mode
                • Configure control settings (player-controlled or not)""",
            "entity_editor.png"
        ),

        // Level System slide
        new HelpSlide(
            "Level System",
            """
                The Level System lets you create and manage multiple game levels:

                • Add new levels with the 'Add Level' button
                • Switch between levels using the dropdown
                • Configure level dimensions (width and height)
                • Set up mode change events triggered during gameplay
                • Configure entity spawn events based on conditions""",
            "levels.png"
        ),

        // Collision Rules slide
        new HelpSlide(
            "Collision Rules",
            """
                Collision Rules define what happens when entities interact:

                • Access from the Game Settings panel ('Collision Rules' button)
                • Define rules between pairs of entity types
                • Set actions for each entity involved (destroy, add points, change mode, etc.)
                • Mode-specific rules allow for different behaviors in different states""",
            "collisions.png"
        ),

        // Game Settings slide
        new HelpSlide(
            "Game Settings",
            """
                Game Settings control global game properties:

                • Set game title, author, and description
                • Configure overall game speed
                • Set starting lives and initial score
                • Choose score strategy (how points accumulate)
                • Define win and lose conditions""",
            "settings.png"
        ),

        // Creating Your First Game slide
        new HelpSlide(
            "Creating Your First Game",
            """
                Basic steps to create a simple game:

                1. Create entity types (player, enemies, collectibles, obstacles)
                2. Configure their appearance and properties
                3. Set up collision rules between them
                4. Place entities on the canvas to design levels
                5. Configure win/lose conditions
                6. Save your game and test it in the Engine""",
            "first_game.png"
        ),

        // Tips and Tricks slide
        new HelpSlide(
            "Tips and Tricks",
            """
                Helpful tips for efficient game authoring:

                • Use meaningful names for entity types and levels
                • Start with a simple game to learn the basics
                • Test your game frequently during development
                • Create reusable entity types to save time
                • Use right-click context menus for quick actions
                • Save your work often!""",
            null
        ),

        // Conclusion slide
        new HelpSlide(
            "Ready to Create!",
            """
                You now know the basics of the Game Authoring Environment!

                Remember that game development is an iterative process. Start simple, test often, and gradually add complexity as you become more comfortable with the tools.

                Happy game creating!""",
            null
        )
    );
    HelpSlideContent helpSlideContent = new HelpSlideContent();
    helpSlides = helpSlideContent.getHelpSlides();
  }
}
