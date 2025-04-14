package oogasalad.player.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.records.GameContextRecord;
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
   * @param configModel The config model for this view.
   * @param levelIndex  The index of the level that is displayed on this view.
   */
  public GameView(GameContextRecord gameContext, ConfigModel configModel, int levelIndex) {
    super();
    GameMapView myGameMapView = new GameMapView(gameContext, configModel);
    this.setPrefSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.setMinSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.setMaxSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.getChildren().add(myGameMapView);
    this.getStyleClass().add("game-view");
    this.setFocusTraversable(true);
    myGameLoopController = new GameLoopController(gameContext, myGameMapView,
        configModel.levels().get(levelIndex));
    myGameMapView.setGameLoopController(myGameLoopController);
    endLabel.setVisible(false);
    endLabel.getStyleClass().add("end-label");
    this.getChildren().add(endLabel); // overlay it on top
    StackPane.setAlignment(endLabel, Pos.CENTER);
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
    endLabel.setText(gameWon ? LanguageManager.getMessage("LEVEL_PASSED")
        : LanguageManager.getMessage("GAME_OVER"));
    endLabel.setVisible(true);
  }
}
