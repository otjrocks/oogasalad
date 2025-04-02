package oogasalad.player.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.Pane;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameStateImpl;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.strategies.collision.StopStrategy;

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
    super();
    myGameMap = gameMap;
    initializeMap();
  }

  private void initializeMap() {
    for (Iterator<Entity> it = myGameMap.iterator(); it.hasNext(); ) {
      Entity entity = it.next();
      EntityView entityView = new EntityView(myGameMap, entity.getEntityPlacement());
      entityView.setLayoutX(
          entity.getEntityPlacement().getX() * ((double) GameView.WIDTH / myGameMap.getWidth()));
      entityView.setLayoutY(entity.getEntityPlacement().getY() * ((double) GameView.HEIGHT
          / myGameMap.getHeight()));
      entityViewsMap.put(entity, entityView);
      this.getChildren().add(entityView);
    }
  }

  /**
   * Update the positions of entities in this game map view.
   */
  public void updateEntityPositions() {
    handleExamplePacManWallCollision();
    for (Entity entity : entityViewsMap.keySet()) {
      moveEntity(entity);
      EntityView entityView = entityViewsMap.get(entity);
      entityView.setLayoutX(
          entity.getEntityPlacement().getX() * ((double) GameView.WIDTH / myGameMap.getWidth()));
      entityView.setLayoutY(entity.getEntityPlacement().getY() * ((double) GameView.HEIGHT
          / myGameMap.getHeight()));
    }
  }

  private void moveEntity(Entity entity) {
    entity.getEntityPlacement().setX(entity.getEntityPlacement().getX() + entity.getDx());
    entity.getEntityPlacement().setY(entity.getEntityPlacement().getY() + entity.getDy());
  }

  private void handleExamplePacManWallCollision() { // TODO: remove later, just for testing
    for (List<Entity> collision : checkCollisions()) {
      Entity e1 = collision.get(0);
      Entity e2 = collision.get(1);
      StopStrategy stopStrategy = new StopStrategy();
      if (e1.getEntityPlacement().getType().getType().equals("Pacman") && e2.getEntityPlacement().getType().getType().equals("Wall")) {
        System.out.println("Collision between Pacman and Wall");
        try {
          stopStrategy.handleCollision(e1, e2, myGameMap, new GameStateImpl(5));
        } catch (EntityNotFoundException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  /**
   * Checks for collisions between entity views.
   *
   * @return A list of entity pairs that are colliding.
   */
  public List<List<Entity>> checkCollisions() {
    List<List<Entity>> collisions = new ArrayList<>();

    for (Entity entityA : entityViewsMap.keySet()) {
      EntityView viewA = entityViewsMap.get(entityA);
      for (Entity entityB : entityViewsMap.keySet()) {
        if (entityA == entityB) continue; // Skip self-comparison

        EntityView viewB = entityViewsMap.get(entityB);
        if (viewA.getBoundsInParent().intersects(viewB.getBoundsInParent())) {
          List<Entity> pair = Arrays.asList(entityA, entityB);
          collisions.add(pair);
        }
      }
    }

    return collisions; // Returns a list of all colliding entity pairs
  }


}
