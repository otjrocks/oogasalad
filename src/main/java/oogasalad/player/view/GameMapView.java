package oogasalad.player.view;

import java.util.Iterator;
import javafx.scene.Group;
import oogasalad.engine.model.Entity;
import oogasalad.engine.model.GameMap;

/**
 * The view used to display the game map.
 *
 * @author Owen Jennings
 */
public class GameMapView extends Group {

  private final GameMap myGameMap;

  public GameMapView(GameMap gameMap, int width, int height) {
    myGameMap = gameMap;
    initializeMap();
  }

  private void initializeMap() {
    for (Iterator<Entity> it = myGameMap.iterator(); it.hasNext(); ) {
      Entity entity = it.next();
      EntityView entityView = new EntityView(entity.entityData());
      this.getChildren().add(entityView);
    }
  }
}
