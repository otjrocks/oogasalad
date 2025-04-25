package oogasalad.authoring.view.mainView;

import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.view.EntityPlacementView;
import oogasalad.authoring.view.EntitySelectorView;
import oogasalad.authoring.view.EntityTypeEditorView;
import oogasalad.authoring.view.GameSettingsView;
import oogasalad.authoring.view.LevelSelectorView;
import oogasalad.authoring.view.LevelSettingsView;
import oogasalad.authoring.view.canvas.CanvasView;

class AuthoringLayoutBuilder {

  private final AuthoringView view;
  private final AuthoringController controller;

  public AuthoringLayoutBuilder(AuthoringView view, AuthoringController controller) {
    this.view = view;
    this.controller = controller;
  }

  public void buildLayout() {
    controller.getLevelController().initDefaultLevelIfEmpty();
    initializeViews();
    controller.getLevelController().updateLevelDropdown();
    controller.getLevelController().switchToLevel(0);

    VBox fullLayout = new VBox(10);
    fullLayout.getChildren().addAll(createMenuBar(), createMainContent(), view.getGameSettingsView().getNode());
    VBox.setVgrow(fullLayout.getChildren().get(1), javafx.scene.layout.Priority.ALWAYS);

    ((BorderPane) view.getNode()).setCenter(fullLayout);

    StyleManager.applyStyles(view);
  }

  private void initializeViews() {
    view.canvasView = new CanvasView(controller);
    view.selectorView = new EntitySelectorView(controller);
    view.entityTypeEditorView = new EntityTypeEditorView(controller);
    view.entityTypeEditorView.getRoot().setVisible(false);

    view.entityPlacementView = new EntityPlacementView(controller);
    view.entityPlacementView.setVisible(false);

    view.levelSelectorView = new LevelSelectorView(controller.getLevelController());
    view.levelSettingsView = new LevelSettingsView(controller.getLevelController());
    view.gameSettingsView = new GameSettingsView(controller);
  }

  private Parent createMenuBar() {
    MenuBarFactory factory = new MenuBarFactory(view, controller);
    return factory.createMenuBar();
  }

  private BorderPane createMainContent() {
    MainContentFactory factory = new MainContentFactory(view);
    return factory.createMainContent();
  }

  public void refresh() {
    controller.getLevelController().updateLevelDropdown();
    controller.getLevelController().switchToLevel(controller.getModel().getCurrentLevelIndex());
    view.canvasView.loadLevel(controller.getModel().getCurrentLevel());
    view.selectorView.updateEntities(controller.getModel().getEntityTypes().stream().toList());
    view.entityTypeEditorView.getRoot().setVisible(false);
    view.entityPlacementView.setVisible(false);
    view.gameSettingsView.updateFromModel();
  }
}
