package oogasalad.player.view;

import static oogasalad.engine.config.GameConfig.HEIGHT;
import static oogasalad.engine.config.GameConfig.WIDTH;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.GameState;

public class GameScreenView extends VBox {

  private final GameState gameState;
  private final Label scoreLabel;
  private final Label livesLabel;
  private final Timeline hudUpdater; // Timeline for periodic updates

  private int lastScore;
  private int lastLives;

  public GameScreenView(MainController controller, GameState gameState) {
    super();
    this.gameState = gameState;

    scoreLabel = new Label(
        String.format(LanguageManager.getMessage("SCORE_LABEL"), gameState.getScore()));
    livesLabel = new Label(
        String.format(LanguageManager.getMessage("LIVES_LABEL"), gameState.getLives()));
    HBox hudContainer = new HBox(scoreLabel, livesLabel);
    hudContainer.getStyleClass().add("hud-container");

    GamePlayerView gamePlayerView = new GamePlayerView(controller, gameState);

    this.getChildren().add(hudContainer);
    this.getChildren().add(gamePlayerView);
    this.getStyleClass().add("game-screen-view");
    this.setPrefSize(WIDTH, HEIGHT);

    // Store initial values
    lastScore = gameState.getScore();
    lastLives = gameState.getLives();

    // Timeline to check for changes every 100ms
    hudUpdater = new Timeline(
        new KeyFrame(Duration.millis(100), event -> checkAndUpdateHud())
    );
    hudUpdater.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
    hudUpdater.play();
  }

  /**
   * Checks if score or lives have changed before updating HUD.
   */
  private void checkAndUpdateHud() {
    if (gameState.getScore() != lastScore || gameState.getLives() != lastLives) {
      updateHud();
      lastScore = gameState.getScore();
      lastLives = gameState.getLives();
    }
  }

  /**
   * Updates the HUD display based on game state changes.
   */
  public void updateHud() {
    scoreLabel.setText("Score: " + gameState.getScore());
    livesLabel.setText("Lives: " + gameState.getLives());
  }

  /**
   * Stops the HUD updater when the game ends.
   */
  public void stopHudUpdater() {
    hudUpdater.stop();
  }
}
