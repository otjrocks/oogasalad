package oogasalad.authoring.view.mainView;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.view.EntityPlacementView;
import oogasalad.authoring.view.EntitySelectorView;
import oogasalad.authoring.view.EntityTypeEditorView;
import oogasalad.authoring.view.GameSettingsView;
import oogasalad.authoring.view.LevelSelectorView;
import oogasalad.authoring.view.LevelSettingsView;
import oogasalad.authoring.view.canvas.CanvasView;
import oogasalad.engine.utility.constants.GameConfig;

/**
 * Top-level view for the Authoring Environment.
 * Coordinates subviews and delegates layout, save/load, and help logic to manager classes.
 */
public class AuthoringView {

  final BorderPane root;
  AuthoringController controller;

  // Subviews
  EntitySelectorView selectorView;
  CanvasView canvasView;
  EntityTypeEditorView entityTypeEditorView;
  LevelSelectorView levelSelectorView;
  LevelSettingsView levelSettingsView;
  GameSettingsView gameSettingsView;
  EntityPlacementView entityPlacementView;

  // Managers
  private AuthoringLayoutBuilder layoutBuilder;
  private HelpManager helpManager;

  public AuthoringView() {
    root = new BorderPane();
    root.setPrefWidth(GameConfig.WIDTH);
    root.setPrefHeight(GameConfig.HEIGHT);
  }

  public Parent getNode() {
    return root;
  }

  public void setController(AuthoringController controller) {
    this.controller = controller;
    layoutBuilder = new AuthoringLayoutBuilder(this, controller);
    helpManager = new HelpManager(this, controller);

    layoutBuilder.buildLayout();
    helpManager.setupHelpSystem();
  }

  public CanvasView getCanvasView() {
    return canvasView;
  }

  public EntitySelectorView getEntitySelectorView() {
    return selectorView;
  }

  public EntityTypeEditorView getEntityEditorView() {
    return entityTypeEditorView;
  }

  public LevelSelectorView getLevelSelectorView() {
    return levelSelectorView;
  }

  public GameSettingsView getGameSettingsView() {
    return gameSettingsView;
  }

  public EntityPlacementView getEntityPlacementView() {
    return entityPlacementView;
  }

  public LevelSettingsView getLevelSettingsView() {
    return levelSettingsView;
  }

  public void refreshUI() {
    layoutBuilder.refresh();
  }

  // These two methods are called by layout builders
  void addEntityTypeEditor(AnchorPane container) {
    var editorRoot = entityTypeEditorView.getRoot();
    var scrollPane = new javafx.scene.control.ScrollPane(editorRoot);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
    container.getChildren().add(scrollPane);
    AnchorPane.setTopAnchor(scrollPane, 0.0);
    AnchorPane.setLeftAnchor(scrollPane, 0.0);
    AnchorPane.setRightAnchor(scrollPane, 0.0);
    AnchorPane.setBottomAnchor(scrollPane, 0.0);
  }

  void addEntityPlacementView(AnchorPane container) {
    var placementNode = entityPlacementView.getNode();
    container.getChildren().add(placementNode);
    AnchorPane.setTopAnchor(placementNode, 0.0);
    AnchorPane.setLeftAnchor(placementNode, 0.0);
    AnchorPane.setRightAnchor(placementNode, 0.0);
    AnchorPane.setBottomAnchor(placementNode, 0.0);
  }
}
