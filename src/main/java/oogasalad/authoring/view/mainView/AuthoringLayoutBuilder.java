package oogasalad.authoring.view.mainView;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.view.EntityPlacementView;
import oogasalad.authoring.view.EntitySelectorView;
import oogasalad.authoring.view.EntityTypeEditorView;
import oogasalad.authoring.view.gameSettings.GameSettingsView;
import oogasalad.authoring.view.LevelSelectorView;
import oogasalad.authoring.view.LevelSettingsView;
import oogasalad.authoring.view.canvas.CanvasView;

/**
 * Responsible for building and initializing the overall layout of the AuthoringView. Delegates
 * creation of menus, main content, and applies styling. Also provides functionality to refresh the
 * view when the underlying model updates.
 *
 * <p>
 * This class separates layout construction from the core AuthoringView logic, promoting cleaner,
 * more modular code.
 * </p>
 *
 * @author William He
 */
public class AuthoringLayoutBuilder {

  private final AuthoringView view;
  private final AuthoringController controller;

  /**
   * Constructs a layout builder for the given AuthoringView and controller.
   *
   * @param view       the main AuthoringView to configure
   * @param controller the controller coordinating model and view
   */
  public AuthoringLayoutBuilder(AuthoringView view, AuthoringController controller) {
    this.view = view;
    this.controller = controller;
  }

  /**
   * Builds and sets up the full authoring interface layout, including initializing subviews,
   * creating the menu bar, and applying styles.
   */
  public void buildLayout() {
    controller.getLevelController().initDefaultLevelIfEmpty();
    initializeViews();
    controller.getLevelController().updateLevelDropdown();
    controller.getLevelController().switchToLevel(0);

    VBox fullLayout = new VBox(10);
    fullLayout.getChildren()
        .addAll(createMenuBar(), createMainContent(), view.getGameSettingsView().getNode());
    VBox.setVgrow(fullLayout.getChildren().get(1), javafx.scene.layout.Priority.ALWAYS);

    ((BorderPane) view.getNode()).setCenter(fullLayout);

    StyleManager.applyStyles(view);
  }

  /**
   * Initializes the core subviews of the authoring environment. Instantiates components like the
   * canvas, selector, and editors.
   */
  private void initializeViews() {
    view.setCanvasView(new CanvasView(controller));
    view.setSelectorView(new EntitySelectorView(controller));
    view.setEntityPlacementView(new EntityPlacementView(controller));
    view.setEntityTypeEditorView(new EntityTypeEditorView(controller));
    view.getEntityTypeEditorView().getRoot().setVisible(false);
    view.getEntityPlacementView().setVisible(false);

    view.setLevelSelectorView(new LevelSelectorView(controller.getLevelController()));
    view.setLevelSettingsView(new LevelSettingsView(controller.getLevelController()));
    view.setGameSettingsView(new GameSettingsView(controller));
  }

  /**
   * Creates and returns the menu bar component for the authoring interface.
   *
   * @return the menu bar
   */
  private Parent createMenuBar() {
    MenuBarFactory factory = new MenuBarFactory(view, controller);
    return factory.createMenuBar();
  }

  /**
   * Creates and returns the main content area, including the left panel, canvas view, and right
   * panel.
   *
   * @return the configured main content pane
   */
  private BorderPane createMainContent() {
    MainContentFactory factory = new MainContentFactory(view);
    return factory.createMainContent();
  }

  /**
   * Refreshes the views to reflect the latest state of the underlying model, such as updating the
   * entity list, level settings, and canvas content.
   */
  public void refresh() {
    controller.getLevelController().updateLevelDropdown();
    controller.getLevelController().switchToLevel(controller.getModel().getCurrentLevelIndex());
    view.getCanvasView().loadLevel(controller.getModel().getCurrentLevel());
    view.getSelectorView().updateEntities(controller.getModel().getEntityTypes().stream().toList());
    view.getEntityTypeEditorView().getRoot().setVisible(false);
    view.getEntityPlacementView().setVisible(false);
    view.getGameSettingsView().updateFromModel();
  }
}
