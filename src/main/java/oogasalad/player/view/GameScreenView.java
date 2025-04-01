package oogasalad.player.view;

import static oogasalad.engine.config.GameConfig.HEIGHT;
import static oogasalad.engine.config.GameConfig.WIDTH;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.GameState;

/**
 * The main screen that contains the HUD elements and the player view.
 */
public class GameScreenView extends VBox {

  private final GameState gameState;
  private final Label scoreLabel;
  private final Label livesLabel;
  private final HBox hudContainer;
  private final GamePlayerView gamePlayerView;

  /**
   * Create the Game Screen View that contains the HUD and GamePlayerView.
   */
  public GameScreenView(MainController controller, GameState gameState) {
    super();
    this.gameState = gameState;

    scoreLabel = new Label(
        String.format(LanguageManager.getMessage("SCORE_LABEL"), gameState.getScore()));
    livesLabel = new Label(
        String.format(LanguageManager.getMessage("LIVES_LABEL"), gameState.getScore()));
    hudContainer = new HBox(scoreLabel, livesLabel);
    hudContainer.getStyleClass().add("hud-container");

    gamePlayerView = new GamePlayerView(controller);

    this.getChildren().add(hudContainer);
    this.getChildren().add(gamePlayerView);
    this.getStyleClass().add("game-screen-view");
    this.setPrefSize(WIDTH, HEIGHT);
  }

  /**
   * Updates the HUD display based on game state changes.
   */
  public void updateHud() {
    scoreLabel.setText("Score: " + gameState.getScore());
    livesLabel.setText("Lives: " + gameState.getLives());
  }

  /**
   * Expose the gamePlayerView for other components if needed.
   */
  public GamePlayerView getGamePlayerView() {
    return gamePlayerView;
  }
}
