package oogasalad;


import java.io.File;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.api.GameMapFactory;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.view.SplashScreenView;
import oogasalad.player.view.GameView;

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
