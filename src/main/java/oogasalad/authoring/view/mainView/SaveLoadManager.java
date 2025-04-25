package oogasalad.authoring.view.mainView;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.utility.LanguageManager;

import java.io.File;
import java.nio.file.Path;

/**
 * Manages the saving of game configurations within the Authoring Environment.
 *
 * <p>Handles user interaction for selecting a save location and writing project files.
 * Displays appropriate confirmation or error alerts based on save success or failure.</p>
 *
 * <p>Separates save/load logic from AuthoringView to improve modularity and maintainability.</p>
 *
 * @see AlertUtil
 * @see MenuBarFactory
 * @see AuthoringLayoutBuilder
 *
 * @author William He
 */
public class SaveLoadManager {

  private final AuthoringController controller;

  /**
   * Constructs a SaveLoadManager for the given AuthoringController.
   *
   * @param controller the AuthoringController managing the project model
   */
  public SaveLoadManager(AuthoringController controller) {
    this.controller = controller;
  }

  /**
   * Opens a directory chooser dialog prompting the user to select a save location.
   *
   * <p>Attempts to save the current project to the selected directory.
   * Displays success or error alerts based on the outcome.</p>
   *
   * @param owner the Stage to associate with the directory chooser and alerts
   */
  public void openSaveDialog(Stage owner) {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle(LanguageManager.getMessage("CHOOSE_SAVE_LOCATION"));
    File selectedDirectory = chooser.showDialog(owner);

    if (selectedDirectory != null) {
      Path savePath = selectedDirectory.toPath().resolve("output");
      try {
        controller.getModel().saveGame(savePath);
        AlertUtil.showAlert(owner, LanguageManager.getMessage("SAVED"),
            LanguageManager.getMessage("SUCCESS_SAVE_JSON"), javafx.scene.control.Alert.AlertType.CONFIRMATION);
      } catch (ConfigException e) {
        AlertUtil.showAlert(owner, LanguageManager.getMessage("ERROR_SAVING"),
            e.getMessage(), javafx.scene.control.Alert.AlertType.ERROR);
      }
    }
  }
}
