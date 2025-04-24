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

    initializeGame();
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

    LevelController levelController = new LevelController(myMainController, myConfigModel, isRandomized, sessionManager);
    loadGameViewFromSession(levelController);
  }

  private void loadOrCreateSession() {
    try {
      sessionManager.loadExistingSession();
      LoggingManager.LOGGER.info("✅ Loaded existing save file for '{}'", gameFolderName);
    } catch (IOException e) {
      sessionManager.startNewSession(myConfigModel);
      LoggingManager.LOGGER.info("📁 No save found, created new session for '{}'", gameFolderName);
    }
  }

  private void updateGameStateFromSession() {
    myGameState.resetState();
    myGameState.updateLives(sessionManager.getLives());
    myGameState.updateScore(sessionManager.getCurrentScore());
  }

  private void loadConfigFromFile() {
    JsonConfigParser configParser = new JsonConfigParser();
    try {
      myConfigModel = configParser.loadFromFile(GAME_FOLDER + gameFolderName + "/" + GAME_CONFIG_JSON);
    } catch (ConfigException e) {
      LoggingManager.LOGGER.warn("Failed to load config file: {}", GAME_CONFIG_JSON, e);
    }
  }

  private void loadGameViewFromSession(LevelController levelController) {
    myPane.getChildren().removeIf(node -> node == myGameView.getRoot()); // Clear old GameView

    if (levelController.getCurrentLevelMap() != null) {
      int logicalIndex = sessionManager.getCurrentLevel();
      int actualMappedIndex = sessionManager.getLevelOrder().get(logicalIndex);

      LoggingManager.LOGGER.info("🧭 Loading mapped level {} (logical index {})", actualMappedIndex, logicalIndex);

      myGameView = new GameView(
          new GameContextRecord(levelController.getCurrentLevelMap(), myGameState),
          myConfigModel,
          logicalIndex,
          sessionManager
      );

      myGameView.setNextLevelAction(this::handleNextLevel);
      myGameView.setResetAction(this::handleResetGame);

      myPane.getChildren().add(myGameView.getRoot());
    }
  }

  private void handleNextLevel() {
    if (sessionManager.getCurrentLevel() + 1 < sessionManager.getLevelOrder().size()) {
      sessionManager.advanceLevel(myGameState.getScore());

      try {
        sessionManager.loadExistingSession();
        LoggingManager.LOGGER.info("📂 Reloaded session after advancing to level {}", sessionManager.getCurrentLevel());
      } catch (IOException e) {
        LoggingManager.LOGGER.warn("Failed to reload session after level advance", e);
      }

      refreshGame();
    } else {
      LoggingManager.LOGGER.info("🎉 All levels complete — cannot advance further.");
    }
  }


  private void handleResetGame() {
    myGameState.resetTimeElapsed();
    sessionManager.resetSession(myConfigModel);
    refreshGame();
  }

  private void refreshGame() {
    myPane.getChildren().clear();

    updateGameStateFromSession();

    LevelController newLevelController = new LevelController(myMainController, myConfigModel, isRandomized, sessionManager);
    loadGameViewFromSession(newLevelController);
  }


  /**
   * Returns privately stored GameView.
   */
  public GameView getGameView() {
    return myGameView;
  }
}
