package oogasalad.authoring.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;

/**
 * Top-level view for the Authoring Environment. Combines all major authoring UI panels.
 */
public class AuthoringView extends BorderPane {

  private EntitySelectorView selectorView;
  private CanvasView canvasView;
  private EntityEditorView entityEditorView;
  private AuthoringController controller;
//  private final GameSettingsView gameSettingsView;
//  private final CollisionRuleEditorView collisionEditorView;

  /**
   * Constructs the full authoring environment interface.
   */
  public AuthoringView() {
    this.controller = null;
  }

  public CanvasView getCanvasView() {
    return canvasView;
  }

  public EntitySelectorView getEntitySelectorView() {
    return selectorView;
  }

  public EntityEditorView getEntityEditorView() {
    return entityEditorView;
  }
//
//  public GameSettingsView getGameSettingsView() {
//    return gameSettingsView;
//  }
//
//  public CollisionRuleEditorView getCollisionEditorView() {
//    return collisionEditorView;
//  }

  public void setController(AuthoringController controller) {
    this.controller = controller;
    setupSubViews(); // you might call this to inject controller into EntitySelectorView, CanvasView, etc.
  }

  private void setupSubViews() {
    // Main canvas area
    canvasView = new CanvasView(controller);

    // Sidebar - entity selector and editor
    selectorView = new EntitySelectorView(controller);
    entityEditorView = new EntityEditorView(controller);
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
//    this.setBottom(bottomPanel);
//    this.getStyleClass().add("authoring-view");
  }
}
