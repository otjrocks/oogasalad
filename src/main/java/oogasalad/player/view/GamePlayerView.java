package oogasalad.player.view;

import static oogasalad.engine.utility.constants.GameConfig.WIDTH;

import javafx.scene.layout.StackPane;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.ConfigModelRecord;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.controller.LevelController;
import oogasalad.player.model.GameStateInterface;

/**
 * The view that displays only the game grid.
 *
 * @author Owen Jennings
 */
public class GamePlayerView {

  public static final String CURRENT_GAME_CONFIG_PATH = "data/games/BasicPacMan/";
  public static final String GAME_CONFIG_JSON = "gameConfig.json";
  private final StackPane myPane;
  private final MainController myMainController;
  private final GameStateInterface myGameState;
  private GameView myGameView;
  private ConfigModelRecord myConfigModel = null;

  /**
   * Create the Game Player View.
   */
  public GamePlayerView(MainController controller, GameStateInterface gameState) {
    myPane = new StackPane();
    myMainController = controller;
    myGameState = gameState;

    myPane.setPrefWidth(WIDTH);
    myPane.getStyleClass().add("game-player-view");

    createExampleMap();
  }

  /**
   * Get the root stack pane that is used to display elements in the view.
   *
   * @return A StackPane JavaFX object that is added to for this view.
   */
  public StackPane getPane() {
    return myPane;
  }

  private void createExampleMap() {
    loadConfigFromFile();
    loadGameViewFromConfig();
    myPane.getChildren().add(myGameView.getRoot());
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

      myGameView.setNextLevelAction(() -> loadNextLevel(levelController));
      myGameView.setResetAction(() -> resetGame(levelController));
    }
  }

  private void resetGame(LevelController levelController) {
    myPane.getChildren().clear();
    levelController.resetAndUpdateConfig();
    loadConfigFromFile();
    updateGameStateFromConfigurationFile();
    loadGameViewFromConfig();
    myPane.getChildren().add(myGameView.getRoot());
  }

  private void loadNextLevel(LevelController levelController) {
    if (levelController.hasNextLevel()) {
      levelController.incrementAndUpdateConfig();
      myPane.getChildren().clear();
      loadConfigFromFile();
      loadGameViewFromConfig();
      myPane.getChildren().add(myGameView.getRoot());
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
