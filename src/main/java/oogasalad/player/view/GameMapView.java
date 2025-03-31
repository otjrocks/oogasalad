package oogasalad.player.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.scene.layout.Pane;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;

/**
 * The view used to display the game map.
 *
 * @author Owen Jennings
 */
public class GameMapView extends Pane {

  private final GameMap myGameMap;
  private final Map<Entity, EntityView> entityViewsMap = new HashMap<>();

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
      EntityView entityView = new EntityView(entity.getEntityData());
      entityView.setLayoutX(entity.getEntityData().getInitialX() * GameView.TILE_WIDTH);
      entityView.setLayoutY(entity.getEntityData().getInitialY() * GameView.TILE_HEIGHT);
      entityViewsMap.put(entity, entityView);
      this.getChildren().add(entityView);
    }
  }

  public void updateEntityPositions() {
    for (Entity entity : entityViewsMap.keySet()) {
      EntityView entityView = entityViewsMap.get(entity);
      entityView.setLayoutX(entity.getEntityData().getInitialX() * GameView.TILE_WIDTH);
      entityView.setLayoutY(entity.getEntityData().getInitialY() * GameView.TILE_HEIGHT);
    }
  }
}
