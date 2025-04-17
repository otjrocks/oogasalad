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
import oogasalad.engine.records.GameContextRecord;
import oogasalad.player.controller.LevelController;

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
  private int myLevelIndex = 0;

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
    loadConfig();
    this.getChildren().add(myGameView);
  }

  private void loadConfig() {
    LevelController levelController = new LevelController(myMainController, myConfigModel);
    if (levelController.getCurrentLevelMap() != null) {
      myGameView = new GameView(new GameContextRecord(levelController.getCurrentLevelMap(), myGameState),
          myConfigModel, levelController.getCurrentLevelIndex());
      myGameView.setRestartAction(this::restartLevel);
    }
  }

  /**
   * Restarts the current level by clearing the view and reloading the game configuration.
   *
   * <p>This method replaces the current {@code GameView} with a fresh instance,
   * effectively resetting the level state.</p>
   */
  public void restartLevel() {
    this.getChildren().clear();
    loadConfig();
    this.getChildren().add(myGameView);
  }

  /**
   * Returns privately stored GameView.
   */
  public GameView getGameView() {
    return myGameView;
  }
}
