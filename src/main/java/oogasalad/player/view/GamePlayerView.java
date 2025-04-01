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
import oogasalad.engine.model.GameState;

/**
 * The view that displays only the game grid.
 */
public class GamePlayerView extends Pane {

  private final MainController myMainController;
  private final GameState gameState;

  /**
   * Create the Game Player View.
   */
  public GamePlayerView(MainController controller, GameState gameState) {
    super();
    myMainController = controller;
    this.gameState = gameState;

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
        gameMap = GameMapFactory.createGameMap(myMainController.getInputManager(),
                configModel, 20, 20);
      }
    } catch (InvalidPositionException e) {
      LoggingManager.LOGGER.warn(e);
    }
    if (gameMap != null) {
      GameView gameView = new GameView(gameMap);
      this.getChildren().add(gameView);
    }
  }
}
