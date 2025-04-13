package oogasalad.player.view;

import static oogasalad.engine.config.GameConfig.ELEMENT_SPACING;
import static oogasalad.engine.config.GameConfig.HEIGHT;
import static oogasalad.engine.config.GameConfig.WIDTH;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.GameState;
import oogasalad.player.view.components.HudView;

/**
 * A view that displays the Heads-up display (HUD) and the game player view.
 *
 * @author Owen Jennings, Troy Ludwig
 */
public class GameScreenView extends VBox {

  private final MainController mainController;
  private final GameState gameState;
  private final HudView hudView;
  private int lastScore;
  private int lastLives;

  /**
   * Create a game screen view.
   *
   * @param controller The main controller for the player view.
   * @param gameState  The game state object for this current game.
   */
  public GameScreenView(MainController controller, GameState gameState) {
    super();
    this.gameState = gameState;
    this.mainController = controller;

    hudView = new HudView(gameState);

    GamePlayerView gamePlayerView = new GamePlayerView(controller, gameState);
    HBox controlBox = getHBox(gamePlayerView);

    this.getChildren().addAll(hudView, controlBox, gamePlayerView);
    this.getStyleClass().add("game-screen-view");
    this.setPrefSize(WIDTH, HEIGHT);

    // Store initial values
    lastScore = gameState.getScore();
    lastLives = gameState.getLives();

    // Timeline to check for changes every 100ms
    Timeline hudUpdater = new Timeline(
        new KeyFrame(Duration.millis(100), event -> checkAndUpdateHud())
    );
    hudUpdater.setCycleCount(Timeline.INDEFINITE);
    hudUpdater.play();
  }

  /**
   * Returns Horizontal Box With Pause and Play
   */
  private HBox getHBox(GamePlayerView gamePlayerView) {
    GameView gameView = gamePlayerView.getGameView();

    Button pauseButton = new Button("⏸");
    Button playButton = new Button("▶");
    Button returnToMenuButton = new Button(LanguageManager.getMessage("RETURN_TO_MENU"));

    pauseButton.setFocusTraversable(false);
    playButton.setFocusTraversable(false);
    returnToMenuButton.setFocusTraversable(false);

    pauseButton.setOnAction(e -> {
      gameView.pauseGame();
      gameView.requestFocus();
    });

    playButton.setOnAction(e -> {
      gameView.resumeGame();
      gameView.requestFocus();
    });

    returnToMenuButton.setOnAction(e -> {
      mainController.getInputManager().getRoot().getChildren().remove(this);
      mainController.showSplashScreen();
    });

    HBox buttonBox = new HBox(ELEMENT_SPACING, playButton, pauseButton, returnToMenuButton);
    buttonBox.getStyleClass().add("hud-container");
    return buttonBox;
  }

  /**
   * Checks if score or lives have changed before updating HUD.
   */
  private void checkAndUpdateHud() {
    if (gameState.getScore() != lastScore || gameState.getLives() != lastLives) {
      hudView.update();
      lastScore = gameState.getScore();
      lastLives = gameState.getLives();
    }
  }

}
