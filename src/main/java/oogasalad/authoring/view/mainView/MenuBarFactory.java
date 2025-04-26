package oogasalad.authoring.view.mainView;

import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.utility.LanguageManager;

import java.io.File;

/**
 * Factory class responsible for creating the menu bar for the Authoring Environment.
 *
 * <p>Provides a menu containing options to save and load game configurations,
 * and wires up their corresponding event handlers.</p>
 *
 * <p>Separates menu creation logic from AuthoringView for better modularity and
 * maintainability.</p>
 *
 * @author William He
 * @see SaveLoadManager
 * @see AlertUtil
 * @see AuthoringLayoutBuilder
 */
public class MenuBarFactory {

  private final AuthoringView view;
  private final AuthoringController controller;

  /**
   * Constructs a MenuBarFactory associated with the given AuthoringView and controller.
   *
   * @param view       the parent AuthoringView
   * @param controller the AuthoringController managing application logic
   */
  public MenuBarFactory(AuthoringView view, AuthoringController controller) {
    this.view = view;
    this.controller = controller;
  }

  /**
   * Creates and returns a fully initialized MenuBar.
   *
   * <p>The menu bar includes a "File" menu with "Save Game" and "Load Game" options,
   * each wired to appropriate event handlers.</p>
   *
   * @return the constructed MenuBar
   */
  public MenuBar createMenuBar() {
    MenuBar menuBar = new MenuBar();
    menuBar.getStyleClass().add("menu-bar");
    Menu fileMenu = createFileMenu();
    Menu exitMenu = createExitMenu();
    menuBar.getMenus().addAll(fileMenu, exitMenu);
    return menuBar;
  }

  private Menu createExitMenu() {
    MenuItem exitItem = new MenuItem(LanguageManager.getMessage("EXIT_NORMAL"));
    MenuItem exitAndSaveItem = new MenuItem(LanguageManager.getMessage("EXIT_AND_SAVE"));
    exitItem.setOnAction(e -> controller.hideAuthoringEnvironment());
    exitAndSaveItem.setOnAction(e -> controller.handleExitAndSave());
    Menu exitMenu = new Menu(LanguageManager.getMessage("EXIT"));
    exitMenu.getItems().addAll(exitItem, exitAndSaveItem);
    return exitMenu;
  }

  private Menu createFileMenu() {
    Menu fileMenu = new Menu(LanguageManager.getMessage("FILE"));

    MenuItem saveItem = createSaveGameItem();
    MenuItem loadItem = createLoadGameItem();

    fileMenu.getItems().addAll(saveItem, loadItem);
    return fileMenu;
  }

  private MenuItem createLoadGameItem() {
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
    return loadItem;
  }

  private MenuItem createSaveGameItem() {
    MenuItem saveItem = new MenuItem(LanguageManager.getMessage("SAVE_GAME"));
    saveItem.setOnAction(e -> new SaveLoadManager(controller).openSaveDialog(
        (Stage) view.getNode().getScene().getWindow()));
    return saveItem;
  }
}
