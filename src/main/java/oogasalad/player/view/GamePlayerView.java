package oogasalad.player.view;

import static oogasalad.engine.utility.constants.GameConfig.WIDTH;

import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.ConfigModelRecord;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.controller.LevelController;
import oogasalad.player.model.GameStateInterface;
import oogasalad.player.model.exceptions.SaveFileException;
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
  private final String gameFolderBasePath;
  private final boolean isRandomized;

  private GameView myGameView;
  private ConfigModelRecord myConfigModel = null;
  private GameSessionManager sessionManager;
  private LevelController levelController;

  public GamePlayerView(MainController controller, GameStateInterface gameState,
      String gameFolderName, boolean randomized) {
    this(controller, gameState, gameFolderName, randomized, "data/games/");
  }

  public GamePlayerView(MainController controller, GameStateInterface gameState,
      String gameFolderName, boolean randomized, String customBasePath) {
    myPane = new StackPane();
    myMainController = controller;
    myGameState = gameState;
    this.isRandomized = randomized;
    this.gameFolderName = gameFolderName;
    this.gameFolderBasePath = customBasePath;

    this.sessionManager = new GameSessionManager(gameFolderName, gameFolderName);

    myPane.setPrefWidth(WIDTH);
    myPane.getStyleClass().add("game-player-view");

    initializeGame();
  }

  public StackPane getPane() {
    return myPane;
  }

  private void initializeGame() {
    loadConfigFromFile();
    loadOrCreateSession();
    updateGameStateFromSession();

    levelController = new LevelController(myMainController, myConfigModel, isRandomized, sessionManager);
    loadGameViewFromSession();
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

  private void loadGameViewFromSession() {
    if (myGameView != null) {
      myPane.getChildren().remove(myGameView.getRoot());
    }

    if (levelController.getCurrentLevelMap() != null) {
      int logicalIndex = levelController.getCurrentLevelIndex();
      int actualMappedIndex = sessionManager.getLevelOrder().get(logicalIndex);

      LoggingManager.LOGGER.info("🧭 Loading mapped level {} (logical index {})", actualMappedIndex, logicalIndex);

      myGameView = new GameView(
          new GameContextRecord(levelController.getCurrentLevelMap(), myGameState),
          myConfigModel,
          logicalIndex,
          sessionManager,
          (gameFolderBasePath + gameFolderName + "/")
      );

      myGameView.setNextLevelAction(this::handleNextLevel);
      myGameView.setResetAction(this::handleResetGame);
      myGameView.setSaveAction(() -> {
        try {
          if (myGameView.isPendingLevelAdvance()) {
            sessionManager.advanceLevel(myGameState.getScore());
            LoggingManager.LOGGER.info("🚀 Level won: saving with next level progress!");
          }
          sessionManager.save();
          LoggingManager.LOGGER.info("💾 Game saved successfully!");
        } catch (SaveFileException e) {
          LoggingManager.LOGGER.warn("Failed to save game progress: {}", e.getMessage());
        }
      });


      myPane.getChildren().add(buildFullView(myGameView.getRoot()));
    }
  }

  private BorderPane buildFullView(StackPane gameViewRoot) {
    BorderPane container = new BorderPane();
    container.setCenter(gameViewRoot);

    return container;
  }

  private void saveProgress() {
    sessionManager.save();
    LoggingManager.LOGGER.info("💾 Manual Save triggered by player");
  }

  private void handleNextLevel() {
    if (levelController.hasNextLevel()) {
      levelController.incrementLevel();
      sessionManager.advanceLevel(myGameState.getScore());

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

    levelController = new LevelController(myMainController, myConfigModel, isRandomized, sessionManager);
    loadGameViewFromSession();
  }

  public GameView getGameView() {
    return myGameView;
  }
}
