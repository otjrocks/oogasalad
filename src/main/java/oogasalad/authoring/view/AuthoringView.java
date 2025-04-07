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

//  private final GameSettingsView gameSettingsView;
//  private final CollisionRuleEditorView collisionEditorView;

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

//  /**
//   * Returns the view for editing global game settings.
//   *
//   * @return the game settings view component
//   */
//  public GameSettingsView getGameSettingsView() {
//    return gameSettingsView;
//  }
//
//  /**
//   * Returns the view for editing collision rules between entities.
//   *
//   * @return the collision rule editor view component
//   */
//  public CollisionRuleEditorView getCollisionEditorView() {
//    return collisionEditorView;
//  }

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
   * Initializes the main subviews (canvas, entity selector, editor),
   * and adds them to the appropriate layout positions.
   * Called internally after setting the controller.
   */
  private void setupSubViews() {
    // Main canvas area
    canvasView = new CanvasView(controller);

    // Sidebar - entity selector and editor
    selectorView = new EntitySelectorView(controller);
    entityEditorView = new EntityEditorView(controller);
    levelSelectorView = new LevelSelectorView(controller);
//    VBox leftPanel = new VBox(selectorView, entityEditorView);
//    leftPanel.getStyleClass().add("left-panel");

    // Bottom panel - game settings and collision rules
//    gameSettingsView = new GameSettingsView(controller);
//    collisionEditorView = new CollisionRuleEditorView(controller);
//    HBox bottomPanel = new HBox(gameSettingsView, collisionEditorView);
//    bottomPanel.getStyleClass().add("bottom-panel");

    // Layout
    this.setBottom(selectorView);
    this.setCenter(canvasView);
    this.setRight(entityEditorView);
    this.setLeft(levelSelectorView);
//    this.setBottom(bottomPanel);
//    this.getStyleClass().add("authoring-view");
  }

  public LevelSelectorView getLevelSelectorView() {
    return levelSelectorView;
  }
}
