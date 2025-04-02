package oogasalad.authoring.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Top-level view for the Authoring Environment.
 * Combines all major authoring UI panels.
 */
public class AuthoringView extends BorderPane {

  private final EntitySelectorView selectorView;
//  private final CanvasView canvasView;
//  private final EntityEditorView entityEditorView;
//  private final GameSettingsView gameSettingsView;
//  private final CollisionRuleEditorView collisionEditorView;

  /**
   * Constructs the full authoring environment interface.
   */
  public AuthoringView() {
    // Main canvas area
//    canvasView = new CanvasView(controller);

    // Sidebar - entity selector and editor
    selectorView = new EntitySelectorView();
//    entityEditorView = new EntityEditorView(controller);
//    VBox leftPanel = new VBox(selectorView, entityEditorView);
//    leftPanel.getStyleClass().add("left-panel");

    // Bottom panel - game settings and collision rules
//    gameSettingsView = new GameSettingsView(controller);
//    collisionEditorView = new CollisionRuleEditorView(controller);
//    HBox bottomPanel = new HBox(gameSettingsView, collisionEditorView);
//    bottomPanel.getStyleClass().add("bottom-panel");

    // Layout
    this.setBottom(selectorView);
//    this.setCenter(canvasView);
//    this.setBottom(bottomPanel);
//    this.getStyleClass().add("authoring-view");
  }

//  public CanvasView getCanvasView() {
//    return canvasView;
//  }

  public EntitySelectorView getEntitySelectorView() {
    return selectorView;
  }

//  public EntityEditorView getEntityEditorView() {
//    return entityEditorView;
//  }
//
//  public GameSettingsView getGameSettingsView() {
//    return gameSettingsView;
//  }
//
//  public CollisionRuleEditorView getCollisionEditorView() {
//    return collisionEditorView;
//  }
}
