package oogasalad.player.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.strategies.collision.ConsumeStrategy;
import oogasalad.engine.model.strategies.collision.StopStrategy;
import oogasalad.engine.model.strategies.collision.UpdateScoreStrategy;
import oogasalad.engine.records.CollisionContext;
import oogasalad.engine.records.GameContext;

/**
 * A controller that handles all the updates of the game map models whenever the game map view is
 * updated.
 *
 * @author Owen Jennings
 */
public class GameMapController {

  // TODO: This should be removed when hardcoded methods are refactored
  public static final int PACMAN_INITIAL_X = 13;
  public static final int PACMAN_INITIAL_Y = 23;
  private static final double GHOST_INITIAL_POSITION = 15;
  private static final String PACMAN = "Pacman";
  private static final int SPRITE_ANIMATION_SPEED = 6;
  private final GameMap gameMap;
  private final GameState gameState;
  private int frameCount = 0;

  /**
   * Create a game map controller with the provided game context.
   *
   * @param gameContext The game context object for this controller.
   */
  public GameMapController(GameContext gameContext) {
    gameMap = gameContext.gameMap();
    gameState = gameContext.gameState();
  }

  /**
   * Update the entity models that are part of the game map.
   */
  public void updateEntityModels() {
    frameCount++;
    // Move entities and advance animation frame
    for (Iterator<Entity> it = gameMap.iterator(); it.hasNext(); ) {
      Entity entity = it.next();
      entity.getEntityPlacement().setX(
          entity.getEntityPlacement().getX() + entity.getDx());
      entity.getEntityPlacement().setY(
          entity.getEntityPlacement().getY() + entity.getDy());
      if (frameCount % SPRITE_ANIMATION_SPEED == 0) {
        entity.getEntityPlacement().increaseCurrentFrame();
      }
    }
    // Handle collisions
    for (List<Entity> collision : getAllCollisions()) {
      Entity e1 = collision.get(0);
      Entity e2 = collision.get(1);
      handleCollision(e1, e2);
    }
  }

  // TODO: Right now, we have hardcoded the collision strategies for the game player.
  // This should be refactored when the entity configuration is read in from the config file.
  private void handleCollision(Entity e1, Entity e2) {
    handlePacManDeath(e1, e2);
    handleBlueGhost(e1, e2);
    handlePacManDotCollision(e1, e2);
    handleWallCollisions(e1, e2);
  }

  private void handleWallCollisions(Entity e1, Entity e2) {
    // Stop at Wall for Pac-Man and Ghosts
    if (e2.getEntityPlacement().getType().type().equals("Wall")
        && (e1.getEntityPlacement().getType().type().equals(PACMAN)
        || e1.getEntityPlacement().getType().type().endsWith("Ghost"))) {
      try {
        new StopStrategy().handleCollision(new CollisionContext(e1, e2, gameMap, gameState));
      } catch (EntityNotFoundException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  private void handlePacManDotCollision(Entity e1, Entity e2) {
    // Pac-Man eats Dot
    if (e1.getEntityPlacement().getType().type().equals(PACMAN)
        && e2.getEntityPlacement().getType().type().equals("Dot")) {
      try {
        new ConsumeStrategy().handleCollision(new CollisionContext(e1, e2, gameMap, gameState));
      } catch (EntityNotFoundException ex) {
        throw new RuntimeException(ex);
      }
      new UpdateScoreStrategy(10)
          .handleCollision(new CollisionContext(e1, e2, gameMap, gameState));
    }
  }

  private void handleBlueGhost(Entity e1, Entity e2) {
    // Pac-Man eats BlueGhost
    if (e1.getEntityPlacement().getType().type().equals(PACMAN)
        && e2.getEntityPlacement().getType().type().equals("BlueGhost")) {
      try {
        gameMap.removeEntity(e2);
      } catch (EntityNotFoundException ex) {
        throw new RuntimeException(ex);
      }
      new UpdateScoreStrategy(200)
          .handleCollision(new CollisionContext(e1, e2, gameMap, gameState));

    }
  }

  private void handlePacManDeath(Entity e1, Entity e2) {
    // Pac-Man death by RedGhost
    if (e1.getEntityPlacement().getType().type().equals(PACMAN)
        && e2.getEntityPlacement().getType().type().equals("RedGhost")) {
      gameState.updateLives(-1);
      e1.getEntityPlacement().setX(PACMAN_INITIAL_X);
      e1.getEntityPlacement().setY(PACMAN_INITIAL_Y);
      e1.setEntityDirection(' ');
      e2.getEntityPlacement().setX(GHOST_INITIAL_POSITION);
      e2.getEntityPlacement().setY(GHOST_INITIAL_POSITION);
    }
  }

  private List<List<Entity>> getAllCollisions() {
    List<List<Entity>> collisions = new ArrayList<>();
    for (Iterator<Entity> it = gameMap.iterator(); it.hasNext(); ) {
      Entity a = it.next();
      for (Iterator<Entity> iter = gameMap.iterator(); iter.hasNext(); ) {
        Entity b = iter.next();
        if (a != b
            && Math.abs(a.getEntityPlacement().getX() - b.getEntityPlacement().getX()) < 1
            && Math.abs(a.getEntityPlacement().getY() - b.getEntityPlacement().getY()) < 1) {
          collisions.add(Arrays.asList(a, b));
        }
      }
    }
    return collisions;
  }
}
