package oogasalad.authoring.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;

/**
 * Top-level view for the Authoring Environment.
 * Combines and arranges all major UI components including the canvas, entity selector,
 * and entity editor. This class initializes its subviews and injects the controller as needed.
 *
 * Intended to be used as the main root node for the authoring scene.
 *
 * @author Will He, Angela Predolac
 */
public class AuthoringView extends BorderPane {

  private EntitySelectorView selectorView;
  private CanvasView canvasView;
  private EntityTypeEditorView entityTypeEditorView;
  private AuthoringController controller;
  private LevelSelectorView levelSelectorView;
  private GameSettingsView gameSettingsView;
  private CollisionRuleEditorView collisionEditorView;
  private EntityPlacementView entityPlacementView;

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
   * Returns the {@link EntityTypeEditorView}, which provides controls for editing a selected entity type.
   *
   * @return the entity editor view component
   */
  public EntityTypeEditorView getEntityEditorView() {
    return entityTypeEditorView;
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

    entityTypeEditorView = new EntityTypeEditorView(controller);
    entityTypeEditorView.setVisible(false);

    entityPlacementView = new EntityPlacementView(controller);

    levelSelectorView = new LevelSelectorView(controller.getLevelController());
    gameSettingsView = new GameSettingsView(controller);

    // Make sure controller is initialized for level management
    controller.getLevelController().initDefaultLevelIfEmpty();

    // Create a simple layout
    BorderPane mainContent = new BorderPane();

    // Add components to layout
    mainContent.setLeft(levelSelectorView);
    mainContent.setCenter(canvasView);

    VBox rightPanel = new VBox(10);
    rightPanel.getChildren().addAll(selectorView, entityTypeEditorView);
    mainContent.setRight(rightPanel);

    // Create a VBox for the main layout
    VBox fullLayout = new VBox(10);
    fullLayout.getChildren().addAll(mainContent, gameSettingsView.getNode(), entityPlacementView.getNode());
    VBox.setVgrow(mainContent, Priority.ALWAYS);

    // Set the main layout as the center of this BorderPane
    this.setCenter(fullLayout);

    // Direct style settings (no CSS)
    rightPanel.setPrefWidth(300);
    levelSelectorView.setPrefWidth(200);
    gameSettingsView.getNode().setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #cccccc; -fx-border-width: 1px 0 0 0; -fx-padding: 10px;");

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

  // Add this getter method:
  /**
   * Returns the {@link EntityPlacementView}, which provides controls for editing a selected entity placement.
   *
   * @return the entity placement view component
   */
  public EntityPlacementView getEntityPlacementView() {
    return entityPlacementView;
  }



}
