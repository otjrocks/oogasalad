package oogasalad.player.view;

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

public class GameScreenView extends VBox {

  private final GameState gameState;
  private final Label scoreLabel;
  private final Label livesLabel;
  private final Timeline hudUpdater;

  private int lastScore;
  private int lastLives;

  public GameScreenView(MainController controller, GameState gameState) {
    super();
    this.gameState = gameState;

    scoreLabel = new Label(String.format(LanguageManager.getMessage("SCORE_LABEL"), gameState.getScore()));
    livesLabel = new Label(String.format(LanguageManager.getMessage("LIVES_LABEL"), gameState.getLives()));
    HBox hudContainer = new HBox(scoreLabel, livesLabel);
    hudContainer.getStyleClass().add("hud-container");

    GamePlayerView gamePlayerView = new GamePlayerView(controller, gameState);
    HBox controlBox = getHBox(gamePlayerView);

    this.getChildren().addAll(hudContainer, controlBox, gamePlayerView);
    this.getStyleClass().add("game-screen-view");
    this.setPrefSize(WIDTH, HEIGHT);

    lastScore = gameState.getScore();
    lastLives = gameState.getLives();

    hudUpdater = new Timeline(
        new KeyFrame(Duration.millis(100), event -> checkAndUpdateHud())
    );
    hudUpdater.setCycleCount(Timeline.INDEFINITE);
    hudUpdater.play();
  }

  private static HBox getHBox(GamePlayerView gamePlayerView) {
    GameView gameView = gamePlayerView.getGameView();

    Button pauseButton = new Button("⏸");
    Button playButton = new Button("▶");
    pauseButton.setFocusTraversable(false);
    playButton.setFocusTraversable(false);
    pauseButton.setOnAction(e -> {
      gameView.pauseGame();
      gameView.requestFocus();
    });

    playButton.setOnAction(e -> {
      gameView.resumeGame();
      gameView.requestFocus();
    });
    HBox buttonBox = new HBox(10, playButton, pauseButton);
    buttonBox.getStyleClass().add("hud-container");
    return buttonBox;
  }

  private void checkAndUpdateHud() {
    if (gameState.getScore() != lastScore || gameState.getLives() != lastLives) {
      updateHud();
      lastScore = gameState.getScore();
      lastLives = gameState.getLives();
    }
  }

  public void updateHud() {
    scoreLabel.setText("Score: " + gameState.getScore());
    livesLabel.setText("Lives: " + gameState.getLives());
  }

  public void stopHudUpdater() {
    hudUpdater.stop();
  }
}
