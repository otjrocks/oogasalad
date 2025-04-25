package oogasalad.authoring.view;

import java.io.File;
import java.nio.file.Path;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.help.SimpleHelpSystem;
import oogasalad.authoring.view.canvas.CanvasView;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.constants.GameConfig;

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

  private final BorderPane root;
  private EntitySelectorView selectorView;
  private CanvasView canvasView;
  private EntityTypeEditorView entityTypeEditorView;
  private AuthoringController controller;
  private LevelSelectorView levelSelectorView;
  private LevelSettingsView levelSettingsView;
  private GameSettingsView gameSettingsView;
  private EntityPlacementView entityPlacementView;
  private SimpleHelpSystem helpSystem;

  /**
   * Constructs the full authoring environment interface without a controller. The controller should
   * be set separately via {@link #setController(AuthoringController)}.
   */
  public AuthoringView() {
    this.root = new BorderPane();
    this.controller = null;
    root.setPrefWidth(GameConfig.WIDTH);
    root.setPrefHeight(GameConfig.HEIGHT);

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
    Platform.runLater(this::setupHelpSystem);
  }

  /**
   * Sets up the help system for the authoring environment.
   */
  private void setupHelpSystem() {
    Stage stage = getStage();
    this.helpSystem = new SimpleHelpSystem(controller, this, stage);

    addHelpMenu();
    addHelpButton();
    setupHelpKeyboardShortcuts();
  }

  private Stage getStage() {
    return (Stage) root.getScene().getWindow();
  }

  /**
   * Adds a help button to the main view.
   */
  private void addHelpButton() {
    Button helpButton = new Button("?");
    helpButton.setId("helpButton");
    helpButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
        "-fx-background-color: #3498db; -fx-text-fill: white; " +
        "-fx-background-radius: 50%; -fx-min-width: 30px; " +
        "-fx-min-height: 30px; -fx-max-width: 30px; -fx-max-height: 30px;");
    helpButton.setTooltip(new Tooltip("Show Help"));
    helpButton.setOnAction(e -> helpSystem.showHelpDialog());

    // Get the BorderPane that contains the canvas
    VBox fullLayout = (VBox) root.getCenter();
    BorderPane mainContent = (BorderPane) fullLayout.getChildren().get(1);

    // Create a StackPane to overlay the help button on the canvas
    StackPane canvasWithHelp = new StackPane();
    canvasWithHelp.getChildren().addAll(canvasView.getNode(), helpButton);
    mainContent.setCenter(canvasWithHelp);

    // Position the help button in the top-right corner
    StackPane.setAlignment(helpButton, javafx.geometry.Pos.TOP_RIGHT);
    StackPane.setMargin(helpButton, new Insets(10));
  }

  /**
   * Adds a help menu to the menu bar.
   */
  private void addHelpMenu() {
    MenuBar menuBar = (MenuBar) ((VBox) root.getCenter()).getChildren().get(0);

    // Check if Help menu already exists
    for (Menu menu : menuBar.getMenus()) {
      if (menu.getText().equals(LanguageManager.getMessage("HELP"))) {
        return; // Help menu already exists
      }
    }

    // Create Help menu
    Menu helpMenu = new Menu(LanguageManager.getMessage("HELP"));
    MenuItem helpContentsItem = new MenuItem(LanguageManager.getMessage("HELP_CONTENTS"));
    helpContentsItem.setOnAction(e -> helpSystem.showHelpDialog());

    MenuItem aboutItem = new MenuItem(LanguageManager.getMessage("ABOUT"));
    aboutItem.setOnAction(e -> showAboutDialog());

    helpMenu.getItems().addAll(helpContentsItem, new SeparatorMenuItem(), aboutItem);
    menuBar.getMenus().add(helpMenu);
  }

  /**
   * Shows the about dialog.
   */
  private void showAboutDialog() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(LanguageManager.getMessage("ABOUT"));
    alert.setHeaderText("Game Authoring Environment");
    alert.setContentText(
        """
            Version: 1.0
            A powerful tool for creating 2D games without writing code.
            
            Part of the OOGASalad project."""
    );

    alert.showAndWait();
  }

  /**
   * Sets up keyboard shortcuts for the help system.
   */
  private void setupHelpKeyboardShortcuts() {
    root.getScene().getAccelerators().put(
        new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.F1),
        () -> helpSystem.showHelpDialog()
    );
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
    MenuItem loadGameItem = new MenuItem(LanguageManager.getMessage("LOAD_GAME"));
    saveItem.setOnAction(e -> openSaveDialog());
    loadGameItem.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      File selected = fileChooser.showOpenDialog(getNode().getScene().getWindow());
      if (selected != null) {
        controller.loadProject(selected);
      }
    });
    fileMenu.getItems().add(saveItem);
    fileMenu.getItems().add(loadGameItem);

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

    // === LEFT PANEL ===
    VBox left = new VBox(10);
    left.getChildren().addAll(levelSelectorView.getRoot(), levelSettingsView.getNode());
    mainContent.setLeft(left);

    // === CENTER CANVAS VIEW ===
    Node canvasNode = canvasView.getNode();
    VBox canvasWrapper = new VBox(canvasNode);
    canvasWrapper.setPadding(new Insets(0));
    VBox.setVgrow(canvasNode, Priority.ALWAYS);
    VBox.setVgrow(canvasWrapper, Priority.ALWAYS);
    mainContent.setCenter(canvasWrapper);

    // === RIGHT PANEL ===
    VBox rightPanel = new VBox(10);

    // Create editor container once
    AnchorPane editorContainer = createEditorContainer();

    // Only the selector view should expand vertically
    Node selectorNode = selectorView.getRoot();
    VBox.setVgrow(selectorNode, Priority.ALWAYS);
    VBox.setVgrow(editorContainer, Priority.NEVER);

    rightPanel.getChildren().addAll(selectorNode, editorContainer);
    rightPanel.setFillWidth(true);
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
    editorContainer.setMaxHeight(400);

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
    Node editorRoot = entityTypeEditorView.getRoot();

    ScrollPane scrollPane = new ScrollPane(editorRoot);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    scrollPane.setMaxHeight(Double.MAX_VALUE);
    scrollPane.setPrefHeight(Region.USE_COMPUTED_SIZE);

    container.getChildren().add(scrollPane);
    AnchorPane.setTopAnchor(scrollPane, 0.0);
    AnchorPane.setLeftAnchor(scrollPane, 0.0);
    AnchorPane.setRightAnchor(scrollPane, 0.0);
    AnchorPane.setBottomAnchor(scrollPane, 0.0);
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

    // Give more space to the game settings view using the new methods
    gameSettingsView.setPreferredHeight(200);
    gameSettingsView.setMinimumHeight(180);

    // Add bottom margin
    VBox.setMargin(gameSettingsView.getNode(), new Insets(0, 0, 20, 0));

    gameSettingsView.getNode().setStyle(
        "-fx-background-color: #f4f4f4; -fx-border-color: #cccccc; -fx-border-width: 1px 0 0 0; -fx-padding: 10px;");

    fullLayout.setSpacing(10);
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


  /**
   * Get view for level settings
   *
   * @return LevelSettingsView
   */
  public LevelSettingsView getLevelSettingsView() {
    return levelSettingsView;
  }

  public void refreshUI() {
    // Reload the canvas with the current level
    canvasView.loadLevel(controller.getModel().getCurrentLevel());

    // Refresh entity selector view
    selectorView.updateEntities(controller.getModel().getEntityTypes().stream().toList());

    // Hide editor views by default
    entityTypeEditorView.getRoot().setVisible(false);
    entityPlacementView.setVisible(false);

    // Update game and level settings
    gameSettingsView.updateFromModel();  // assumes method exists
//    levelSettingsView.updateFromLevel(controller.getModel().getCurrentLevel());  // assumes method exists

    // Refresh level selector dropdown
    controller.getLevelController().updateLevelDropdown();

    // Set canvas to current level
    controller.getLevelController().switchToLevel(controller.getModel().getCurrentLevelIndex());
  }

}
