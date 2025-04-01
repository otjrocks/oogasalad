package oogasalad;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.view.SplashScreenView;

/**
 * The Main class of the project.
 *
 * @author Owen Jennings
 */
public class Main extends Application {

  private final Group myRoot = new Group();

  @Override
  public void start(Stage stage) throws ConfigException {
    LoggingManager.printStartInfo();
    Scene scene = new Scene(myRoot, GameConfig.WIDTH, GameConfig.HEIGHT);
    stage.setScene(scene);
    stage.setTitle(LanguageManager.getMessage("TITLE"));
    stage.show();
     myRoot.getChildren().add(new SplashScreenView(stage, myRoot)); // Add splash screen for testing.
  }
}
