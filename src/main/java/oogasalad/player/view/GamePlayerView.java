package oogasalad.player.view;

import static oogasalad.engine.utility.constants.GameConfig.WIDTH;

import java.io.IOException;
import javafx.scene.layout.StackPane;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.ConfigModelRecord;
import oogasalad.engine.records.config.model.SaveConfigRecord;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.controller.LevelController;
import oogasalad.player.model.GameStateInterface;
import oogasalad.player.model.save.GameSessionManager;

/**
 * The view that displays only the game grid.
 *
 * @author Owen Jennings
 */
public class GamePlayerView {


  public static final String GAME_FOLDER = "data/games/";
  public static final String GAME_CONFIG_JSON = "gameConfig.json";

  private final String gameFolderName;
  private final StackPane myPane;
  private final MainController myMainController;
  private final GameStateInterface myGameState;
  private final boolean isRandomized;

  private GameView myGameView;
  private ConfigModelRecord myConfigModel = null;
  private GameSessionManager sessionManager;

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

    this.sessionManager = new GameSessionManager(gameFolderName, gameFolderName);

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

  private void initializeGame() {
    loadConfigFromFile();
    loadOrCreateSession();
    updateGameStateFromSession();
    loadGameViewFromSession();
  }

  private void loadOrCreateSession() {
    try {
      sessionManager.loadExistingSession();
    } catch (IOException e) {
      sessionManager.startNewSession(myConfigModel);
    }
  }

  private void updateGameStateFromSession() {
    myGameState.resetState();
    myGameState.updateLives(sessionManager.getLives());
    myGameState.updateScore(sessionManager.getCurrentScore());
  }


  private void createMap() {
    loadConfigFromFile();
    try {
      sessionManager.loadExistingSession();
    } catch (IOException e) {
      sessionManager.startNewSession(myConfigModel);
    }
    loadGameViewFromSession();
    updateGameStateFromSave();
  }


  private void updateGameStateFromSave() {
    myGameState.resetState();
    myGameState.updateLives(sessionManager.getLives());
    myGameState.updateScore(sessionManager.getCurrentScore());
  }

  private void updateGameStateFromConfigurationFile() {
    myGameState.resetState();
    myGameState.updateLives(myConfigModel.settings().startingLives());
    myGameState.updateScore(myConfigModel.settings().initialScore());
  }

  private void loadConfigFromFile() {
    JsonConfigParser configParser = new JsonConfigParser();
    try {
      myConfigModel = configParser.loadFromFile(GAME_FOLDER + gameFolderName + "/" + GAME_CONFIG_JSON);
    } catch (ConfigException e) {
      LoggingManager.LOGGER.warn("Failed to reload updated config", e);
    }
  }

  private void loadGameViewFromSession() {
    LevelController levelController = new LevelController(myMainController, myConfigModel,
        isRandomized, sessionManager);

    if (levelController.getCurrentLevelMap() != null) {
      myGameView = new GameView(
          new GameContextRecord(levelController.getCurrentLevelMap(), myGameState),
          myConfigModel, levelController.getCurrentLevelIndex()
      );

      myGameView.setNextLevelAction(() -> handleNextLevel(levelController));
      myGameView.setResetAction(() -> handleResetGame(levelController));

      myPane.getChildren().add(myGameView.getRoot());
    }
  }

  private void handleNextLevel(LevelController levelController) {
    if (levelController.hasNextLevel()) {
      sessionManager.advanceLevel(myGameState.getScore());

      try {
        sessionManager.loadExistingSession();
      } catch (IOException e) {
        LoggingManager.LOGGER.warn("Failed to reload session after level advance", e);
      }

      refreshGame(levelController);
    } else {
      LoggingManager.LOGGER.info("All levels complete — cannot advance further.");
    }
  }


  private void handleResetGame(LevelController levelController) {
    myGameState.resetTimeElapsed();
    sessionManager.resetSession(myConfigModel);
    refreshGame(levelController);
  }

  private void refreshGame(LevelController levelController) {
    myPane.getChildren().clear();
    loadConfigFromFile(); // optionally reload fresh config
    updateGameStateFromSession();
    loadGameViewFromSession();
  }

  /**
   * Returns privately stored GameView.
   */
  public GameView getGameView() {
    return myGameView;
  }
}
