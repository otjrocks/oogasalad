package oogasalad.authoring.view;

import java.io.File;
import java.nio.file.Path;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import oogasalad.engine.LanguageManager;
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
  private LevelSettingsView levelSettingsView;
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
  public Parent getNode() {
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
    controller.getLevelController().initDefaultLevelIfEmpty();
    initializeViews();
    controller.getLevelController().updateLevelDropdown();
    controller.getLevelController().switchToLevel(0);

        MenuBar menuBar = createMenuBar();
        BorderPane mainContent = createMainContent();

        // Create a VBox for the main layout
        VBox fullLayout = new VBox(10);
        fullLayout.getChildren().addAll(menuBar, mainContent, gameSettingsView.getNode());
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        // Set the main layout as the center of this BorderPane
        root.setCenter(fullLayout);

        applyStyles();
        setupWindowMaximization();
    }

    /**
     * Initializes all view components.
     */
    private void initializeViews() {
        canvasView = new CanvasView(controller);
        selectorView = new EntitySelectorView(controller);

        entityTypeEditorView = new EntityTypeEditorView(controller);
        entityTypeEditorView.getRoot().setVisible(false);

        entityPlacementView = new EntityPlacementView(controller);
        entityPlacementView.setVisible(false);

    levelSelectorView = new LevelSelectorView(controller.getLevelController());
    levelSettingsView = new LevelSettingsView(controller.getLevelController());
    gameSettingsView = new GameSettingsView(controller);
  }

  /**
   * Creates the menu bar with file menu.
   *
   * @return The configured MenuBar
   */
  private MenuBar createMenuBar() {
    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu(LanguageManager.getMessage("FILE"));
    MenuItem saveItem = new MenuItem(LanguageManager.getMessage("SAVE_GAME"));
    saveItem.setOnAction(e -> openSaveDialog());
    fileMenu.getItems().add(saveItem);
    menuBar.getMenus().add(fileMenu);
    return menuBar;
  }

    /**
     * Creates the main content area with all panels.
     *
     * @return The configured BorderPane
     */
    private BorderPane createMainContent() {
        BorderPane mainContent = new BorderPane();

    VBox left = new VBox(10);
    left.getChildren().addAll(levelSelectorView.getRoot(), levelSettingsView.getNode());
    mainContent.setLeft(left);
    mainContent.setCenter(canvasView.getNode());

        AnchorPane editorContainer = createEditorContainer();

        VBox rightPanel = new VBox(10);
        rightPanel.getChildren().addAll(selectorView.getRoot(), editorContainer);
        mainContent.setRight(rightPanel);

        return mainContent;
    }

    /**
     * Creates the container for entity editors.
     *
     * @return The configured AnchorPane
     */
    private AnchorPane createEditorContainer() {
        AnchorPane editorContainer = new AnchorPane();
        editorContainer.setPrefHeight(400);
        editorContainer.setBorder(new Border(
                new BorderStroke(Color.BLUE, BorderStrokeStyle.DASHED, null, new BorderWidths(1))));

        addEntityTypeEditor(editorContainer);
        addEntityPlacementView(editorContainer);

        return editorContainer;
    }

    /**
     * Adds the entity type editor to the container.
     *
     * @param container The container to add the editor to
     */
    private void addEntityTypeEditor(AnchorPane container) {
        container.getChildren().add(entityTypeEditorView.getRoot());
        AnchorPane.setTopAnchor(entityTypeEditorView.getRoot(), 0.0);
        AnchorPane.setLeftAnchor(entityTypeEditorView.getRoot(), 0.0);
        AnchorPane.setRightAnchor(entityTypeEditorView.getRoot(), 0.0);
        AnchorPane.setBottomAnchor(entityTypeEditorView.getRoot(), 0.0);
    }

    /**
     * Adds the entity placement view to the container.
     *
     * @param container The container to add the view to
     */
    private void addEntityPlacementView(AnchorPane container) {
        Node placementNode = entityPlacementView.getNode();
        container.getChildren().add(placementNode);
        AnchorPane.setTopAnchor(placementNode, 0.0);
        AnchorPane.setLeftAnchor(placementNode, 0.0);
        AnchorPane.setRightAnchor(placementNode, 0.0);
        AnchorPane.setBottomAnchor(placementNode, 0.0);
    }

    /**
     * Applies styling to various components.
     */
    private void applyStyles() {
        VBox fullLayout = (VBox) root.getCenter();
        BorderPane mainContent = (BorderPane) fullLayout.getChildren().get(1);
        VBox rightPanel = (VBox) mainContent.getRight();

        rightPanel.setPrefWidth(300);
        levelSelectorView.getRoot().setPrefWidth(200);

        AnchorPane editorContainer = (AnchorPane) rightPanel.getChildren().get(1);
        VBox.setVgrow(editorContainer, Priority.ALWAYS);

        gameSettingsView.getNode().setStyle(
                "-fx-background-color: #f4f4f4; -fx-border-color: #cccccc; -fx-border-width: 1px 0 0 0; -fx-padding: 10px;");
    }

    /**
     * Sets up window maximization on startup.
     */
    private void setupWindowMaximization() {
        Platform.runLater(() -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setMaximized(true);
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
        showAlert(LanguageManager.getMessage("SAVED"),
            LanguageManager.getMessage("SUCCESS_SAVE_JSON"), AlertType.CONFIRMATION);
      } catch (ConfigException e) {
        showAlert(LanguageManager.getMessage("ERROR_SAVING"), e.getMessage(),
            Alert.AlertType.ERROR);
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
