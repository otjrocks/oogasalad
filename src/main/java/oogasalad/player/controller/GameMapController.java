package oogasalad.player.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.model.CollisionRule;
import oogasalad.engine.model.GameEndHandler;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.api.StrategyFactory;
import oogasalad.engine.model.entity.BasicEntity;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.model.strategies.collision.CollisionStrategy;
import oogasalad.engine.model.strategies.collision.ConsumeStrategy;
import oogasalad.engine.model.strategies.collision.StopStrategy;
import oogasalad.engine.model.strategies.collision.UpdateScoreStrategy;
import oogasalad.engine.model.strategies.gameoutcome.EntityBasedOutcomeStrategy;
import oogasalad.engine.records.CollisionContext;
import oogasalad.engine.records.GameContext;
import oogasalad.player.view.GameMapView;

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
  public static final int FRUIT_INITIAL_X = 13;
  public static final int FRUIT_INITIAL_Y = 17;
  private static final double GHOST_INITIAL_POSITION = 15;
  private static final String PACMAN = "Pacman";
  private static final int SPRITE_ANIMATION_SPEED = 6;
  private final GameMap gameMap;
  private final GameState gameState;
  private final GameMapView gameView;
  private int frameCount = 0;
  private GameEndHandler gameEndHandler;
  private ConfigModel myConfigModel;

  /**
   * Create a game map controller with the provided game context.
   *
   * @param gameContext The game context object for this controller.
   * @param view        The game map view.
   * @param configModel The game's config model information.
   */
  public GameMapController(GameContext gameContext, GameMapView view, ConfigModel configModel) {
    gameMap = gameContext.gameMap();
    gameState = gameContext.gameState();
    gameView = view;
    myConfigModel = configModel;
  }

  /**
   * Update the entity models that are part of the game map.
   */
  public void updateEntityModels() throws InvalidPositionException {
    frameCount++;
    addFruitAfterScoreGoal();
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

  private void addFruitAfterScoreGoal() throws InvalidPositionException {
    if (gameState.getScore() == 700) {
      EntityType fruitType = new EntityType("fruit", "wall", null, null, null, null);
      EntityPlacement fruitPlacement = new EntityPlacement(fruitType, FRUIT_INITIAL_X,
          FRUIT_INITIAL_Y, "Default");
      Entity fruit = new BasicEntity(fruitPlacement);
      gameMap.addEntity(fruit);
    }
  }

  private void handleCollision(Entity e1, Entity e2) {
    appleCollisionStrategiesFromCollisionRules(e1, e2);
  }

  private void appleCollisionStrategiesFromCollisionRules(Entity e1, Entity e2) {
    for (CollisionRule collisionRule : myConfigModel.collisionRules()) {
      if (checkCollisionRuleEntityAMatches(e1, collisionRule)) {
        for (String eventA : collisionRule.getEventsA()) {
          createAndApplyCollisionStrategy(e1, e2, eventA);
        }
      }
      if (checkCollisionRuleEntityBMatches(e2, collisionRule)) {
        for (String eventB : collisionRule.getEventsB()) {
          createAndApplyCollisionStrategy(e1, e2, eventB);
        }
      }
    }
  }

  private void createAndApplyCollisionStrategy(Entity e1, Entity e2, String eventName) {
    CollisionStrategy collisionStrategy = StrategyFactory.createCollisionStrategy(eventName, 1);
    try {
      collisionStrategy.handleCollision(new CollisionContext(e1, e2, gameMap, gameState));
    } catch (EntityNotFoundException e) {
      LoggingManager.LOGGER.warn("Unable to handle collision event: {}", eventName, e);
      throw new RuntimeException(e);
    }
  }

  private static boolean checkCollisionRuleEntityBMatches(Entity e2, CollisionRule collisionRule) {
    return collisionRule.getEntityTypeB().equals(e2.getEntityPlacement().getType().type());
  }

  private static boolean checkCollisionRuleEntityAMatches(Entity e1, CollisionRule collisionRule) {
    return collisionRule.getEntityTypeA().equals(e1.getEntityPlacement().getType().type());
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

  /**
   * Sets the handler to be called when the game ends.
   *
   * @param handler the callback to execute when the game ends
   */
  public void setGameEndHandler(GameEndHandler handler) {
    this.gameEndHandler = handler;
  }

}
