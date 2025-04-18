package oogasalad;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.engine.utility.constants.GameConfig;

/**
 * The Main class of the project.
 *
 * @author Owen Jennings
 */
public class Main extends Application {

  private final Group myRoot = new Group();

  @Override
  public void start(Stage stage) {
    LoggingManager.printStartInfo();
    Scene scene = new Scene(myRoot, GameConfig.WIDTH, GameConfig.HEIGHT);
    stage.setScene(scene);
    stage.setTitle(LanguageManager.getMessage("TITLE"));
    stage.show();
    new MainController(stage, myRoot);
  }
}
