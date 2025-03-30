package oogasalad.player.view;

import java.util.Iterator;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import oogasalad.engine.model.Entity;
import oogasalad.engine.model.GameMap;

/**
 * The view used to display the game map.
 *
 * @author Owen Jennings
 */
public class GameMapView extends Pane {

  private final GameMap myGameMap;

  /**
   * Create the GameMap view instance.
   *
   * @param gameMap The game map model to use when creating the view.
   */
  public GameMapView(GameMap gameMap) {
    myGameMap = gameMap;
    initializeMap();
  }

  private void initializeMap() {
    for (Iterator<Entity> it = myGameMap.iterator(); it.hasNext(); ) {
      Entity entity = it.next();
      EntityView entityView = new EntityView(entity.entityData());
      entityView.setLayoutX(entity.entityData().getInitialX() * GameView.TILE_WIDTH);
      entityView.setLayoutY(entity.entityData().getInitialY() * GameView.TILE_HEIGHT);
      this.getChildren().add(entityView);
    }
  }
}
