package oogasalad.player.view;

import static oogasalad.engine.config.GameConfig.HEIGHT;
import static oogasalad.engine.config.GameConfig.WIDTH;

import javafx.scene.layout.Pane;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.api.GameMapFactory;
import oogasalad.engine.model.exceptions.InvalidPositionException;

/**
 * The view that displays the entire player view.
 *
 * @author Owen Jennings
 */
public class GamePlayerView extends Pane {

  private final MainController myMainController;
  /**
   * Create the Game Player View.
   */
  public GamePlayerView(MainController controller) {
    super();
    myMainController = controller;
    this.setPrefSize(WIDTH, HEIGHT);
    this.getStyleClass().add("game-player-view");
    createExampleMap();
  }

  private void createExampleMap() {
    JsonConfigParser configParser = new JsonConfigParser();
    ConfigModel configModel = null;
    try {
      configModel = configParser.loadFromFile("data/basic.json");
    } catch (ConfigException e) {
      LoggingManager.LOGGER.warn(e);
    }
    GameMap gameMap = null;
    try {
      if (configModel != null) {
        gameMap = GameMapFactory.createGameMap(myMainController.getStage().getScene(), configModel, 20, 20);
      }
    } catch (InvalidPositionException e) {
      LoggingManager.LOGGER.warn(e);
    }
    GameView gameView;
    if (gameMap != null) {
      gameView = new GameView(gameMap);
      this.getChildren().add(gameView);
    }
  }
}
