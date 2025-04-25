package oogasalad.authoring.view.mainView;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.utility.LanguageManager;

import java.io.File;
import java.nio.file.Path;

class SaveLoadManager {

  private final AuthoringController controller;

  public SaveLoadManager(AuthoringController controller) {
    this.controller = controller;
  }

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
