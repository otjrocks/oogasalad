package oogasalad.authoring.view;

import java.io.File;
import java.nio.file.Path;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.config.ConfigException;

/**
 * Top-level view for the Authoring Environment. Combines and arranges all major UI components
 * including the canvas, entity selector, and entity editor. This class initializes its subviews and
 * injects the controller as needed.
 * <p>
 * Intended to be used as the main root node for the authoring scene.
 *
 * @author Will He, Angela Predolac, Ishan Madan
 */
public class AuthoringView {

  private BorderPane root;
  private EntitySelectorView selectorView;
  private CanvasView canvasView;
  private EntityTypeEditorView entityTypeEditorView;
  private AuthoringController controller;
  private LevelSelectorView levelSelectorView;
  private GameSettingsView gameSettingsView;
  private CollisionRuleEditorView collisionEditorView;
  private EntityPlacementView entityPlacementView;

  /**
   * Constructs the full authoring environment interface without a controller. The controller should
   * be set separately via {@link #setController(AuthoringController)}.
   */
  public AuthoringView() {
    this.root = new BorderPane();
    this.controller = null;
  }

  /**
   * Returns the root JavaFX node for this view.
   *
   * @return the root node that can be added to a scene
   */
  public Node getNode() {
    return root;
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
   * Returns the {@link EntitySelectorView}, which allows users to choose from defined entity
   * types.
   *
   * @return the entity selector view component
   */
  public EntitySelectorView getEntitySelectorView() {
    return selectorView;
  }

  /**
   * Returns the {@link EntityTypeEditorView}, which provides controls for editing a selected entity
   * type.
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
   * Sets the {@link AuthoringController} to be used by all child components. Also initializes the
   * subviews and injects the controller into them.
   *
   * @param controller the controller coordinating model and view interactions
   */
  public void setController(AuthoringController controller) {
    this.controller = controller;
    setupSubViews(); // Inject controller into subviews
  }

  /**
   * Initializes the main subviews and arranges them according to the specified layout. Called
   * internally after setting the controller.
   */
  private void setupSubViews() {
    // Initialize all views
    canvasView = new CanvasView(controller);
    selectorView = new EntitySelectorView(controller);

    entityTypeEditorView = new EntityTypeEditorView(controller);
    entityTypeEditorView.setVisible(false);

    entityPlacementView = new EntityPlacementView(controller);
    entityPlacementView.setVisible(false);

    levelSelectorView = new LevelSelectorView(controller.getLevelController());
    gameSettingsView = new GameSettingsView(controller);

    // Make sure controller is initialized for level management
    controller.getLevelController().initDefaultLevelIfEmpty();

    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu("File");
    MenuItem saveItem = new MenuItem("Save Game");
    saveItem.setOnAction(e -> openSaveDialog());
    fileMenu.getItems().add(saveItem);
    menuBar.getMenus().add(fileMenu);

    // Create a simple layout
    BorderPane mainContent = new BorderPane();

    // Add components to layout
    mainContent.setLeft(levelSelectorView);
    mainContent.setCenter(canvasView);

    AnchorPane editorContainer = new AnchorPane();
    editorContainer.setPrefHeight(400);
    editorContainer.setBorder(new Border(
        new BorderStroke(Color.BLUE, BorderStrokeStyle.DASHED, null, new BorderWidths(1))));

    editorContainer.getChildren().add(entityTypeEditorView);
    AnchorPane.setTopAnchor(entityTypeEditorView, 0.0);
    AnchorPane.setLeftAnchor(entityTypeEditorView, 0.0);
    AnchorPane.setRightAnchor(entityTypeEditorView, 0.0);
    AnchorPane.setBottomAnchor(entityTypeEditorView, 0.0);

    Node placementNode = entityPlacementView.getNode();
    editorContainer.getChildren().add(placementNode);
    AnchorPane.setTopAnchor(placementNode, 0.0);
    AnchorPane.setLeftAnchor(placementNode, 0.0);
    AnchorPane.setRightAnchor(placementNode, 0.0);
    AnchorPane.setBottomAnchor(placementNode, 0.0);

    VBox rightPanel = new VBox(10);
    rightPanel.getChildren().addAll(selectorView, editorContainer);
    mainContent.setRight(rightPanel);

    // Create a VBox for the main layout
    VBox fullLayout = new VBox(10);
    fullLayout.getChildren().addAll(menuBar, mainContent, gameSettingsView.getNode());
    VBox.setVgrow(mainContent, Priority.ALWAYS);

    // Set the main layout as the center of this BorderPane
    root.setCenter(fullLayout);

    // Direct style settings (no CSS)
    rightPanel.setPrefWidth(300);
    levelSelectorView.setPrefWidth(200);
    VBox.setVgrow(editorContainer, Priority.ALWAYS);
    gameSettingsView.getNode().setStyle(
        "-fx-background-color: #f4f4f4; -fx-border-color: #cccccc; -fx-border-width: 1px 0 0 0; -fx-padding: 10px;");

    // Setup event listener to maximize window on startup
    Platform.runLater(() -> {
      Stage stage = (Stage) root.getScene().getWindow();
      stage.setMaximized(true); // Start maximized to ensure everything fits
    });
  }

  /**
   * Get level selector view
   *
   * @return level selector view
   */
  public LevelSelectorView getLevelSelectorView() {
    return levelSelectorView;
  }

  // Add this getter method:

  /**
   * Returns the {@link EntityPlacementView}, which provides controls for editing a selected entity
   * placement.
   *
   * @return the entity placement view component
   */
  public EntityPlacementView getEntityPlacementView() {
    return entityPlacementView;
  }


  private void openSaveDialog() {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Choose Save Location");

    File selectedDirectory = chooser.showDialog(root.getScene().getWindow());
    if (selectedDirectory != null) {
      Path savePath = selectedDirectory.toPath().resolve("output");
      try {
        controller.getModel().saveGame(savePath);
        showAlert("Save Successful", "Successfully saved json to output folder", AlertType.CONFIRMATION);
      } catch (ConfigException e) {
        showAlert("Error saving", e.getMessage(), Alert.AlertType.ERROR);
      }
    }
  }

  private void showAlert(String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type, message, ButtonType.OK);
    alert.setTitle(title);
    alert.initOwner(root.getScene().getWindow());
    alert.showAndWait();
  }


}
