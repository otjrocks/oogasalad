package oogasalad.player.view;

import javafx.scene.layout.StackPane;
import oogasalad.engine.model.GameMap;

/**
 * The main game view of the player. Primarily encapsulates the game map view.
 *
 * @author Owen Jennings
 */
public class GameView extends StackPane {

  public static final int WIDTH = 500;
  public static final int HEIGHT = 500;
  public static int TILE_WIDTH = 0;
  public static int TILE_HEIGHT = 0;

  /**
   * Create the game view.
   *
   * @param gameMap The game map model you wish to use.
   */
  public GameView(GameMap gameMap) {
    TILE_WIDTH = WIDTH / gameMap.getWidth();
    TILE_HEIGHT = HEIGHT / gameMap.getHeight();
    GameMapView myGameMapView = new GameMapView(gameMap);
    this.setPrefSize(WIDTH, HEIGHT);
    this.setMinSize(WIDTH, HEIGHT);
    this.setMaxSize(WIDTH, HEIGHT);
    this.getChildren().add(myGameMapView);
    this.getStyleClass().add("game-view");
  }

}
