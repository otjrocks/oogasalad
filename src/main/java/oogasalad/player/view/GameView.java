package oogasalad.player.view;

import javafx.scene.Group;
import oogasalad.engine.model.GameMap;

/**
 * The main game view of the player. Primarily encapsulates the game map view.
 *
 * @author Owen Jennings
 */
public class GameView extends Group {

  private final GameMapView myGameMapView;

  /**
   * Create the game view.
   *
   * @param gameMap The game map model you wish to use.
   * @param width   The width of the view.
   * @param height  The height of the view.
   */
  public GameView(GameMap gameMap, int width, int height) {
    myGameMapView = new GameMapView(gameMap, width, height);
    this.getChildren().add(myGameMapView);
  }

}
