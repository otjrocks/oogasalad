package oogasalad.player.view;

import static oogasalad.engine.config.GameConfig.HEIGHT;
import static oogasalad.engine.config.GameConfig.WIDTH;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.GameStateInterface;
import oogasalad.player.view.components.HudView;

/**
 * A view that displays the Heads-up display (HUD) and the game player view.
 *
 * @author Owen Jennings, Troy Ludwig
 */
public class GameScreenView extends VBox {

  private final MainController mainController;
  private final GameStateInterface gameState;
  private final HudView hudView;
  private int lastScore;
  private int lastLives;

  /**
   * Create a game screen view.
   *
   * @param controller The main controller for the player view.
   * @param gameState  The game state object for this current game.
   */
  public GameScreenView(MainController controller, GameStateInterface gameState) {
    super();
    this.gameState = gameState;
    this.mainController = controller;

    GamePlayerView gamePlayerView = new GamePlayerView(controller, gameState);
    GameView gameView = gamePlayerView.getGameView();

    hudView = new HudView(
        gameState,
        gameView,
        this::handleReturnToMainMenu
    );

    this.getChildren().addAll(hudView, gamePlayerView);
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

  private void handleReturnToMainMenu() {
    mainController.getInputManager().getRoot().getChildren().remove(this);
    mainController.showSplashScreen();
  }


  /**
   * Checks if score or lives have changed before updating HUD.
   */
  private void checkAndUpdateHud() {
    if (gameState.getScore() != lastScore || gameState.getLives() != lastLives) {
      hudView.update(gameState);
      lastScore = gameState.getScore();
      lastLives = gameState.getLives();
    }
  }


}
