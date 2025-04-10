package oogasalad.player.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.records.GameContext;
import oogasalad.player.controller.GameLoopController;

/**
 * The main game view of the player. Primarily encapsulates the game map view.
 *
 * @author Owen Jennings
 */
public class GameView extends StackPane {

  public static final int GAME_VIEW_WIDTH = GameConfig.WIDTH - 2 * GameConfig.MARGIN;
  public static final int GAME_VIEW_HEIGHT = GameConfig.HEIGHT - 2 * GameConfig.MARGIN;

  private final GameLoopController myGameLoopController;
  private final Label endLabel = new Label();

  /**
   * Create the game view.
   *
   * @param gameContext The game context for this view.
   */
  public GameView(GameContext gameContext) {
    super();
    GameMapView myGameMapView = new GameMapView(gameContext);
    this.setPrefSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.setMinSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.setMaxSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.getChildren().add(myGameMapView);
    this.getStyleClass().add("game-view");
    this.setFocusTraversable(true);
    myGameLoopController = new GameLoopController(gameContext, myGameMapView);
    myGameMapView.setGameLoopController(myGameLoopController);
    endLabel.setVisible(false);
    endLabel.getStyleClass().add("end-label");
    // temporary css styling
    endLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: white;"
        + " -fx-background-color: rgba(0,0,0,0.7); -fx-padding: 20px;");
    this.getChildren().add(endLabel); // overlay it on top
    StackPane.setAlignment(endLabel, Pos.CENTER);
    myGameMapView.setGameLoopController(myGameLoopController);
    myGameMapView.setEndGameCallback(this::showEndMessage);
  }

  /**
   * Pause the game loop from the game loop controller associated with this game view.
   */
  public void pauseGame() {
    myGameLoopController.pauseGame();
  }

  /**
   * Resume the game loop from the game loop controller associated with this game view.
   */
  public void resumeGame() {
    myGameLoopController.resumeGame();
  }

  private void showEndMessage(boolean gameWon) {
    endLabel.setText(gameWon ? LanguageManager.getMessage("LEVEL_PASSED") : LanguageManager.getMessage("GAME_OVER"));
    endLabel.setVisible(true);
  }
}
