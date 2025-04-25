package oogasalad.authoring.view.mainView;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.utility.LanguageManager;

import java.io.File;

class MenuBarFactory {

  private final AuthoringView view;
  private final AuthoringController controller;

  public MenuBarFactory(AuthoringView view, AuthoringController controller) {
    this.view = view;
    this.controller = controller;
  }

  public MenuBar createMenuBar() {
    MenuBar menuBar = new MenuBar();
    Menu fileMenu = new Menu(LanguageManager.getMessage("FILE"));

    MenuItem saveItem = new MenuItem(LanguageManager.getMessage("SAVE_GAME"));
    saveItem.setOnAction(e -> new SaveLoadManager(controller).openSaveDialog(
        (Stage) view.getNode().getScene().getWindow()));

    MenuItem loadItem = new MenuItem(LanguageManager.getMessage("LOAD_GAME"));
    loadItem.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      File selected = fileChooser.showOpenDialog(view.getNode().getScene().getWindow());
      if (selected != null) {
        try {
          controller.loadProject(selected);
        } catch (ConfigException ex) {
          AlertUtil.showAlert((Stage) view.getNode().getScene().getWindow(),
              LanguageManager.getMessage("LOAD_FAIL"),
              LanguageManager.getMessage("LOAD_FAIL_MESSAGE"),
              Alert.AlertType.ERROR);
        }
      }
    });

    fileMenu.getItems().addAll(saveItem, loadItem);
    menuBar.getMenus().add(fileMenu);
    return menuBar;
  }
}
