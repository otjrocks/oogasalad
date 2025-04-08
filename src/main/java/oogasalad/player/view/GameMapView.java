package oogasalad.player.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.Pane;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.strategies.collision.ConsumeStrategy;
import oogasalad.engine.model.strategies.collision.StopStrategy;
import oogasalad.engine.model.strategies.collision.UpdateScoreStrategy;
import oogasalad.engine.model.strategies.gameoutcome.EntityBasedOutcomeStrategy;
import oogasalad.engine.model.strategies.gameoutcome.GameOutcomeStrategy;
import oogasalad.engine.records.CollisionContext;
import oogasalad.engine.records.GameContext;

/**
 * The view used to display the game map.
 *
 * @author Owen Jennings
 */
public class GameMapView extends Pane {

  public static final int PACMAN_INITIAL_X = 13;
  public static final int PACMAN_INITIAL_Y = 23;
  public static final String PACMAN = "Pacman";
  private final GameMap myGameMap;
  private final GameState myGameState;
  private final Map<Entity, EntityView> entityViewsMap = new HashMap<>();
  private static final double GHOST_INITIAL_POSITION = 15;

  /**
   * Create the GameMap view instance.
   *
   * @param gameMap The game map model to use when creating the view.
   */
  public GameMapView(GameMap gameMap, GameState gameState) {
    super();
    myGameMap = gameMap;
    myGameState = gameState;
    updateEntityViewsMap();
  }

  private void updateEntityViewsMap() {
    entityViewsMap.clear();
    this.getChildren().clear();
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
    updateEntityModels(); // update all models. First move objects, then check for collisions
    updateEntityViewsMap(); // update the mapping of entity models to entity views
    updateEntityViewsFromModel(); // update the entity views from their model information
  }

  private void moveEntityModels() {
    for (Entity entity : entityViewsMap.keySet()) {
      moveEntity(entity);
    }
  }

  private void updateEntityViewsFromModel() {
    for (Entity entity : entityViewsMap.keySet()) {
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

  private void updateEntityModels() {
    moveEntityModels();
    for (List<Entity> collision : checkCollisions()) {
      Entity e1 = collision.get(0);
      Entity e2 = collision.get(1);
      handleEntityWallStop(e1, e2, PACMAN);
      handleEntityWallStop(e1, e2, "BlueGhost");
      handleEntityWallStop(e1, e2, "RedGhost");
      handlePacManFoodDot(e1, e2);
      handlePacManDeath(e1, e2);
    }
  }

  private void handlePacManDeath(Entity e1, Entity e2) {
    // TODO: remove hard coded later, just for testing
    if (e1.getEntityPlacement().getType().getType().equals(PACMAN) && e2.getEntityPlacement()
        .getType().getType().equals("RedGhost")) {
      myGameState.updateLives(-1);
      e1.getEntityPlacement().setX(PACMAN_INITIAL_X);
      e1.getEntityPlacement().setY(PACMAN_INITIAL_Y);
      e1.setEntityDirection(' ');
      e2.getEntityPlacement().setX(GHOST_INITIAL_POSITION);
      e2.getEntityPlacement().setY(GHOST_INITIAL_POSITION);
    }
    if (e1.getEntityPlacement().getType().getType().equals(PACMAN) && e2.getEntityPlacement()
        .getType().getType().equals("BlueGhost")) {
      try {
        myGameMap.removeEntity(e2);
      } catch (EntityNotFoundException e) {
        throw new RuntimeException(e);
      }
      myGameState.updateScore(200);
    }
  }

  private void handlePacManFoodDot(Entity e1, Entity e2) {
    // TODO: remove hard coded later, just for testing
    CollisionContext collisionContext = new CollisionContext(e1, e2, myGameMap, myGameState);
    if (e1.getEntityPlacement().getType().getType().equals(PACMAN) && e2.getEntityPlacement()
        .getType().getType().equals("Dot")) {
      ConsumeStrategy consumeStrategy = new ConsumeStrategy();
      try {
        consumeStrategy.handleCollision(collisionContext);
      } catch (EntityNotFoundException e) {
        throw new RuntimeException(e);
      }
      UpdateScoreStrategy scoreStrategy = new UpdateScoreStrategy(10);
      scoreStrategy.handleCollision(collisionContext);
      // working on implementing victory condition
//      GameOutcomeStrategy entityBasedOutcomeStrategy = new EntityBasedOutcomeStrategy("Dot");
//      System.out.println(myGameMap.getEntityCount("Dot"));
//      System.out.println(entityBasedOutcomeStrategy.getGameOutcome(new GameContext(myGameMap, myGameState)));
//      if (entityBasedOutcomeStrategy.getGameOutcome(new GameContext(myGameMap, myGameState)).equals("Victory!")) {
//
//      }
    }
  }

  private void handleEntityWallStop(Entity e1, Entity e2, String entityType) {
    StopStrategy stopStrategy = new StopStrategy();
    // TODO: remove hard coded later, just for testing
    if (e1.getEntityPlacement().getType().getType().equals(entityType) &&
        e2.getEntityPlacement().getType().getType().equals("Wall")) {
      try {
        stopStrategy.handleCollision(new CollisionContext(e1, e2, myGameMap, myGameState));
      } catch (EntityNotFoundException e) {
        throw new RuntimeException(e);
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
      for (Entity entityB : entityViewsMap.keySet()) {
        // Detect collision between two entities from their x and y values.
        // A collision is defined as any two entities that intersect at some point in the "grid".
        if (entityA != entityB &&
            Math.abs(entityA.getEntityPlacement().getX() - entityB.getEntityPlacement().getX()) < 1
            &&
            Math.abs(entityA.getEntityPlacement().getY() - entityB.getEntityPlacement().getY())
                < 1) {
          collisions.add(Arrays.asList(entityA, entityB));
        }
      }
    }
    return collisions; // Returns a list of all colliding entity pairs
  }

}
