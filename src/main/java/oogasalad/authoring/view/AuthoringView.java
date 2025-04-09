package oogasalad.authoring.view;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;

/**
 * Top-level view for the Authoring Environment.
 * Combines and arranges all major UI components including the canvas, entity selector,
 * and entity editor. This class initializes its subviews and injects the controller as needed.
 *
 * Intended to be used as the main root node for the authoring scene.
 *
 * @author Will He
 */
public class AuthoringView extends BorderPane {

  private EntitySelectorView selectorView;
  private CanvasView canvasView;
  private EntityEditorView entityEditorView;
  private AuthoringController controller;
  private LevelSelectorView levelSelectorView;
  private GameSettingsView gameSettingsView;
  private CollisionRuleEditorView collisionEditorView;

  /**
   * Constructs the full authoring environment interface without a controller.
   * The controller should be set separately via {@link #setController(AuthoringController)}.
   */
  public AuthoringView() {
    this.controller = null;
  }

  /**
   * Returns the {@link CanvasView}, which displays and manages entity placements on the map.
   *
   * @return the canvas view component
   */
  public CanvasView getCanvasView() {
    return canvasView;
  }

  /**
   * Returns the {@link EntitySelectorView}, which allows users to choose from defined entity types.
   *
   * @return the entity selector view component
   */
  public EntitySelectorView getEntitySelectorView() {
    return selectorView;
  }

  /**
   * Returns the {@link EntityEditorView}, which provides controls for editing a selected entity type.
   *
   * @return the entity editor view component
   */
  public EntityEditorView getEntityEditorView() {
    return entityEditorView;
  }

  /**
   * Returns the view for editing global game settings.
   *
   * @return the game settings view component
   */
  public GameSettingsView getGameSettingsView() {
    return gameSettingsView;
  }

  /**
   * Returns the view for editing collision rules between entities.
   *
   * @return the collision rule editor view component
   */
  public CollisionRuleEditorView getCollisionEditorView() {
    return collisionEditorView;
  }

  /**
   * Sets the {@link AuthoringController} to be used by all child components.
   * Also initializes the subviews and injects the controller into them.
   *
   * @param controller the controller coordinating model and view interactions
   */
  public void setController(AuthoringController controller) {
    this.controller = controller;
    setupSubViews(); // Inject controller into subviews
  }

  /**
   * Initializes the main subviews and arranges them according to the specified layout.
   * Called internally after setting the controller.
   */
  private void setupSubViews() {
    // Initialize all views
    canvasView = new CanvasView(controller);
    selectorView = new EntitySelectorView(controller);
    entityEditorView = new EntityEditorView(controller);
    levelSelectorView = new LevelSelectorView(controller.getLevelController());
    gameSettingsView = new GameSettingsView(controller);

    controller.getLevelController().initDefaultLevelIfEmpty();


    // Set size constraints for panels to ensure proportional layout
    levelSelectorView.setPrefWidth(200);
    levelSelectorView.setMinWidth(150);

    selectorView.setPrefHeight(300);
    selectorView.setMinHeight(200);

    entityEditorView.setPrefHeight(400);
    entityEditorView.setMinHeight(300);

    // Create right panel with proper constraints
    VBox rightPanel = new VBox();
    rightPanel.getChildren().addAll(selectorView, entityEditorView);
    rightPanel.setPrefWidth(300);
    rightPanel.setMinWidth(250);
    rightPanel.setSpacing(5);

    // Make sure canvas expands to fill available space
    canvasView.setMinSize(400, 400);

    // Create the layout structure
    BorderPane mainContent = new BorderPane();

    // Left panel for level selector
    mainContent.setLeft(levelSelectorView);

    // Center panel for canvas
    mainContent.setCenter(canvasView);

    // Right panel for entity selector and editor
    mainContent.setRight(rightPanel);

    // Make the main content expand to fill available space
    VBox.setVgrow(mainContent, Priority.ALWAYS);

    // Set up the main structure
    this.setCenter(mainContent);

    // Add game settings at the bottom with fixed height
    Node settingsNode = gameSettingsView.getNode();
    this.setBottom(settingsNode);

    // Apply styling
    levelSelectorView.getStyleClass().add("level-selector");
    canvasView.getStyleClass().add("canvas-view");
    rightPanel.getStyleClass().add("right-panel");
    settingsNode.getStyleClass().add("game-settings-view");

    // Setup event listener to maximize window on startup
    Platform.runLater(() -> {
      Stage stage = (Stage) this.getScene().getWindow();
      stage.setMaximized(true); // Start maximized to ensure everything fits
    });
  }

  /**
   * Get level selector view
   * @return level selector view
   */
  public LevelSelectorView getLevelSelectorView() {
    return levelSelectorView;
  }
}
