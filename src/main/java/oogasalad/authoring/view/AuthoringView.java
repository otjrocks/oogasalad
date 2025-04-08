package oogasalad.authoring.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    // Make sure controller is initialized for level management
    controller.getLevelController().initDefaultLevelIfEmpty();

    // Create the main center area with canvas
    BorderPane centerArea = new BorderPane();
    centerArea.setCenter(canvasView);

    // Create the right sidebar with entity selector and entity editor
    VBox rightSidebar = new VBox();
    rightSidebar.getChildren().addAll(selectorView, entityEditorView);

    // Place the center and right areas
    BorderPane mainArea = new BorderPane();
    mainArea.setCenter(centerArea);
    mainArea.setRight(rightSidebar);
    mainArea.setLeft(levelSelectorView); // Level selection on far right

    // Create a full layout with the settings at the bottom
    BorderPane fullLayout = new BorderPane();
    fullLayout.setCenter(mainArea);
    fullLayout.setBottom(gameSettingsView.getNode()); // Game settings at bottom

    // Set the full layout as the root
    this.setCenter(fullLayout);

    // Apply any styling needed
    rightSidebar.getStyleClass().add("right-sidebar");
    fullLayout.getStyleClass().add("full-layout");
    gameSettingsView.getNode().getStyleClass().add("bottom-settings");
  }

  /**
   * Get level selector view
   * @return level selector view
   */
  public LevelSelectorView getLevelSelectorView() {
    return levelSelectorView;
  }
}
