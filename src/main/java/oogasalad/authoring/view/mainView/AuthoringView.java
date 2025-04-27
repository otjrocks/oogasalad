package oogasalad.authoring.view.mainView;

import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.view.EntityPlacementView;
import oogasalad.authoring.view.EntitySelectorView;
import oogasalad.authoring.view.EntityTypeEditorView;
import oogasalad.authoring.view.gameSettings.GameSettingsView;
import oogasalad.authoring.view.LevelSelectorView;
import oogasalad.authoring.view.LevelSettingsView;
import oogasalad.authoring.view.canvas.CanvasView;
import oogasalad.engine.utility.constants.GameConfig;

/**
 * Top-level view for the Authoring Environment.
 *
 * <p>This class coordinates all major UI components, delegates layout setup
 * and event handling to helper classes, and provides access to subviews
 * for interaction with the AuthoringController.</p>
 *
 * <p>AuthoringView itself focuses on maintaining references to components
 * and delegating their initialization and updates.</p>
 *
 * @author William He
 */
public class AuthoringView {

  private final BorderPane root;

  // Subviews
  private EntitySelectorView selectorView;
  private CanvasView canvasView;
  private EntityTypeEditorView entityTypeEditorView;
  private LevelSelectorView levelSelectorView;
  private LevelSettingsView levelSettingsView;
  private GameSettingsView gameSettingsView;
  private EntityPlacementView entityPlacementView;

  // Managers
  private AuthoringLayoutBuilder layoutBuilder;

  /**
   * Constructs an empty AuthoringView with preset dimensions.
   */
  public AuthoringView() {
    root = new BorderPane();
    root.getStyleClass().add("root");
    root.setPrefWidth(GameConfig.WIDTH);
    root.setPrefHeight(GameConfig.HEIGHT);
  }

  /**
   * Returns the JavaFX root node representing the entire authoring interface.
   *
   * @return the root node
   */
  public Parent getNode() {
    return root;
  }

  /**
   * Sets the AuthoringController and initializes the layout and help system.
   *
   * @param controller the controller coordinating the model and views
   */
  public void setController(AuthoringController controller) {
    layoutBuilder = new AuthoringLayoutBuilder(this, controller);
    HelpManager helpManager = new HelpManager(this, controller);

    layoutBuilder.buildLayout();
    helpManager.setupHelpSystem();
  }

  /**
   * Returns the CanvasView for placing and editing entities.
   *
   * @return the canvas view
   */
  public CanvasView getCanvasView() {
    return canvasView;
  }

  /**
   * Returns the EntitySelectorView for browsing entity types.
   *
   * @return the selector view
   */
  public EntitySelectorView getEntitySelectorView() {
    return selectorView;
  }

  /**
   * Returns the EntityTypeEditorView for editing selected entity types.
   *
   * @return the entity type editor view
   */
  public EntityTypeEditorView getEntityEditorView() {
    return entityTypeEditorView;
  }

  /**
   * Returns the LevelSelectorView for switching between different levels.
   *
   * @return the level selector view
   */
  public LevelSelectorView getLevelSelectorView() {
    return levelSelectorView;
  }

  /**
   * Returns the GameSettingsView for editing global game settings.
   *
   * @return the game settings view
   */
  public GameSettingsView getGameSettingsView() {
    return gameSettingsView;
  }

  /**
   * Returns the EntityPlacementView for editing individual entity placements.
   *
   * @return the entity placement view
   */
  public EntityPlacementView getEntityPlacementView() {
    return entityPlacementView;
  }

  /**
   * Returns the LevelSettingsView for configuring level-specific settings.
   *
   * @return the level settings view
   */
  public LevelSettingsView getLevelSettingsView() {
    return levelSettingsView;
  }

  /**
   * Refreshes the UI to reflect updates to the underlying model.
   */
  public void refreshUI() {
    layoutBuilder.refresh();
  }

  /**
   * Adds the EntityTypeEditorView to the specified container.
   *
   * @param container the AnchorPane to which the editor will be added
   */
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

  /**
   * Adds the EntityPlacementView to the specified container.
   *
   * @param container the AnchorPane to which the placement view will be added
   */
  void addEntityPlacementView(AnchorPane container) {
    var placementNode = entityPlacementView.getNode();
    container.getChildren().add(placementNode);
    AnchorPane.setTopAnchor(placementNode, 0.0);
    AnchorPane.setLeftAnchor(placementNode, 0.0);
    AnchorPane.setRightAnchor(placementNode, 0.0);
    AnchorPane.setBottomAnchor(placementNode, 0.0);
  }

  // === Getter and Setter Methods for Subviews ===

  /**
   * Returns the EntitySelectorView.
   *
   * @return the selector view
   */
  public EntitySelectorView getSelectorView() {
    return selectorView;
  }

  /**
   * Sets the EntitySelectorView.
   *
   * @param selectorView the new selector view
   */
  public void setSelectorView(EntitySelectorView selectorView) {
    this.selectorView = selectorView;
  }

  /**
   * Sets the CanvasView.
   *
   * @param canvasView the new canvas view
   */
  public void setCanvasView(CanvasView canvasView) {
    this.canvasView = canvasView;
  }

  /**
   * Returns the EntityTypeEditorView.
   *
   * @return the entity type editor view
   */
  public EntityTypeEditorView getEntityTypeEditorView() {
    return entityTypeEditorView;
  }

  /**
   * Sets the EntityTypeEditorView.
   *
   * @param entityTypeEditorView the new editor view
   */
  public void setEntityTypeEditorView(EntityTypeEditorView entityTypeEditorView) {
    this.entityTypeEditorView = entityTypeEditorView;
  }

  /**
   * Sets the LevelSelectorView.
   *
   * @param levelSelectorView the new level selector view
   */
  public void setLevelSelectorView(LevelSelectorView levelSelectorView) {
    this.levelSelectorView = levelSelectorView;
  }

  /**
   * Sets the LevelSettingsView.
   *
   * @param levelSettingsView the new level settings view
   */
  public void setLevelSettingsView(LevelSettingsView levelSettingsView) {
    this.levelSettingsView = levelSettingsView;
  }

  /**
   * Sets the GameSettingsView.
   *
   * @param gameSettingsView the new game settings view
   */
  public void setGameSettingsView(GameSettingsView gameSettingsView) {
    this.gameSettingsView = gameSettingsView;
  }

  /**
   * Sets the EntityPlacementView.
   *
   * @param entityPlacementView the new entity placement view
   */
  public void setEntityPlacementView(EntityPlacementView entityPlacementView) {
    this.entityPlacementView = entityPlacementView;
  }
}
