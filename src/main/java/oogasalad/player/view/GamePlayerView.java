package oogasalad.player.view;

import static oogasalad.engine.config.GameConfig.WIDTH;

import javafx.scene.layout.StackPane;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.api.GameMapFactory;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.records.GameContext;

/**
 * The view that displays only the game grid.
 *
 * @author Owen Jennings
 */
public class GamePlayerView extends StackPane {

  private final MainController myMainController;
  private final GameState myGameState;
  private GameView myGameView;
  private ConfigModel myConfigModel = null;

  /**
   * Create the Game Player View.
   */
  public GamePlayerView(MainController controller, GameState gameState) {
    super();
    myMainController = controller;
    myGameState = gameState;

    this.setPrefWidth(WIDTH);
    this.getStyleClass().add("game-player-view");

    createExampleMap();
  }

  private void createExampleMap() {
    JsonConfigParser configParser = new JsonConfigParser();
    try {
      myConfigModel = configParser.loadFromFile("data/games/BasicPacMan/gameConfig.json");
    } catch (ConfigException e) {
      LoggingManager.LOGGER.warn("Failed to load configuration file: ", e);
    }

    GameMap gameMap = null;

    try {
      if (myConfigModel != null) {
        gameMap = GameMapFactory.createGameMap(myMainController.getInputManager(), myConfigModel,
            0);
      }
    } catch (InvalidPositionException e) {
      LoggingManager.LOGGER.warn("Failed to create or populate GameMap: ", e);
    }

    if (gameMap != null) {
      myGameView = new GameView(new GameContext(gameMap, myGameState), myConfigModel);
      this.getChildren().add(myGameView);
    }
  }

  /**
   * Returns privately stored GameView
   */
  public GameView getGameView() {
    return myGameView;
  }
}
