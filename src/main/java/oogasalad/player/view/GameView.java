package oogasalad.player.view;

import javafx.scene.layout.StackPane;
import oogasalad.engine.config.ConfigModel;
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

  /**
   * Create the game view.
   *
   * @param gameContext The game context for this view.
   * @param configModel The configuration model for this game view.
   */
  public GameView(GameContext gameContext, ConfigModel configModel) {
    super();
    GameMapView myGameMapView = new GameMapView(gameContext, configModel);
    this.setPrefSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.setMinSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.setMaxSize(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
    this.getChildren().add(myGameMapView);
    this.getStyleClass().add("game-view");
    this.setFocusTraversable(true);
    myGameLoopController = new GameLoopController(gameContext, myGameMapView);
    myGameMapView.setGameLoopController(myGameLoopController);
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
}
