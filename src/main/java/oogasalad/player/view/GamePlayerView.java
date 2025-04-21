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


  public static final String GAME_FOLDER = "data/games/";
  public static final String GAME_CONFIG_JSON = "gameConfig.json";

  public final String gameFolderName;
  private final StackPane myPane;
  private final MainController myMainController;
  private final GameStateInterface myGameState;
  private final boolean isRandomized;
  private GameView myGameView;
  private ConfigModelRecord myConfigModel = null;

  /**
   * Create the Game Player View.
   *
   * @param gameFolderName name of game folder to create
   * @param randomized     if levels should be randomized
   */
  public GamePlayerView(MainController controller, GameStateInterface gameState,
      String gameFolderName, boolean randomized) {
    myPane = new StackPane();
    myMainController = controller;
    myGameState = gameState;
    isRandomized = randomized;

    this.gameFolderName = gameFolderName;

    myPane.setPrefWidth(WIDTH);
    myPane.getStyleClass().add("game-player-view");

    createMap();
  }

  /**
   * Get the root stack pane that is used to display elements in the view.
   *
   * @return A StackPane JavaFX object that is added to for this view.
   */
  public StackPane getPane() {
    return myPane;
  }

  private void createMap() {
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
      myConfigModel = configParser.loadFromFile(
          GAME_FOLDER + gameFolderName + "/" + GAME_CONFIG_JSON);
    } catch (ConfigException e) {
      LoggingManager.LOGGER.warn("Failed to reload updated config", e);
    }
  }

  private void loadGameViewFromConfig() {
    LevelController levelController = new LevelController(myMainController, myConfigModel,
        isRandomized);
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
