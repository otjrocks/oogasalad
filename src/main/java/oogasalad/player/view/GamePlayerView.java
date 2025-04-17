package oogasalad.player.view;

import static oogasalad.engine.config.GameConfig.WIDTH;

import javafx.scene.layout.StackPane;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.GameState;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.player.controller.LevelController;

/**
 * The view that displays only the game grid.
 *
 * @author Owen Jennings
 */
public class GamePlayerView extends StackPane {

  public static final String CURRENT_GAME_CONFIG_PATH = "data/games/BasicPacMan/";
  public static final String GAME_CONFIG_JSON = "gameConfig.json";
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
    loadConfigFromFile();
    loadGameViewFromConfig();
    this.getChildren().add(myGameView);
    updateGameStateFromConfigurationFile();
  }

  private void updateGameStateFromConfigurationFile() {
    myGameState.resetState();
    myGameState.updateLives(myConfigModel.settings().startingLives());
    myGameState.updateScore(myConfigModel.settings().initialScore());
  }

  private void loadConfigFromFile() {
    JsonConfigParser configParser = new JsonConfigParser();
    try {
      myConfigModel = configParser.loadFromFile(CURRENT_GAME_CONFIG_PATH + GAME_CONFIG_JSON);
    } catch (ConfigException e) {
      LoggingManager.LOGGER.warn("Failed to reload updated config", e);
    }
  }

  private void loadGameViewFromConfig() {
    LevelController levelController = new LevelController(myMainController, myConfigModel);
    if (levelController.getCurrentLevelMap() != null) {
      myGameView = new GameView(
          new GameContextRecord(levelController.getCurrentLevelMap(), myGameState),
          myConfigModel, levelController.getCurrentLevelIndex());

      myGameView.setRestartAction(this::restartLevel);
      myGameView.setNextLevelAction(() -> loadNextLevel(levelController));
    }
  }

  private void restartLevel() {
    this.getChildren().clear();
    // currently resets the score to 0. can change to set score to score at level start
    updateGameStateFromConfigurationFile();
    loadGameViewFromConfig();
    this.getChildren().add(myGameView);
  }

  private void loadNextLevel(LevelController levelController) {
    if (levelController.hasNextLevel()) {
      levelController.incrementAndUpdateConfig();
      this.getChildren().clear();
      loadGameViewFromConfig();
      this.getChildren().add(myGameView);
    } else {
      LoggingManager.LOGGER.info("No more levels to load.");
    }
  }

  /**
   * Returns privately stored GameView.
   */
  public GameView getGameView() {
    return myGameView;
  }
}
