package oogasalad.player.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.model.CollisionRule;
import oogasalad.engine.model.GameEndHandler;

import oogasalad.engine.model.GameEndStatus;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.api.CollisionStrategyFactory;
import oogasalad.engine.model.api.GameOutcomeFactory;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.model.strategies.collision.CollisionStrategy;

import oogasalad.engine.model.strategies.gameoutcome.GameOutcomeStrategy;
import oogasalad.engine.records.config.model.CollisionEvent;
import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.engine.records.GameContextRecord;

/**
 * A controller that handles all the updates of the game map models whenever the game map view is
 * updated.
 *
 * @author Owen Jennings
 */
public class GameMapController {

  // TODO: This should be removed when hardcoded methods are refactored
  private static final int SPRITE_ANIMATION_SPEED = 6;
  private final GameMap gameMap;
  private final GameState gameState;
  private final GameContextRecord gameContext;
  private int frameCount = 0;
  private GameEndHandler gameEndHandler;
  private final GameOutcomeStrategy gameOutcomeStrategy;
  private final ConfigModel myConfigModel;

  /**
   * Create a game map controller with the provided game context.
   *
   * @param gameContext The game context object for this controller.
   * @param configModel The game config model for this controller.
   */
  public GameMapController(GameContextRecord gameContext, ConfigModel configModel) {
    gameMap = gameContext.gameMap();
    gameState = gameContext.gameState();
    this.gameContext = gameContext;
    myConfigModel = configModel;
    gameOutcomeStrategy = GameOutcomeFactory.create(configModel.winCondition());
  }

  /**
   * Update the entity models that are part of the game map.
   */
  public void updateEntityModels() throws InvalidPositionException {
    frameCount++;

    for (Entity entity : gameMap) {
      entity.getEntityPlacement().setX(entity.getEntityPlacement().getX() + entity.getDx());
      entity.getEntityPlacement().setY(entity.getEntityPlacement().getY() + entity.getDy());
      if (frameCount % SPRITE_ANIMATION_SPEED == 0) {
        entity.getEntityPlacement().increaseCurrentFrame();
      }
    }

    for (List<Entity> collision : getAllCollisions()) {
      handleCollision(collision.get(0), collision.get(1));
    }

    if (gameOutcomeStrategy.hasGameEnded(gameContext)) {
      gameState.setGameOver(true);
      String result = gameOutcomeStrategy.getGameOutcome(gameContext);
      GameEndStatus status = result.equals("Level Passed") ? GameEndStatus.WIN : GameEndStatus.LOSS;
      stopGameLoop(status);
    }
  }

  // TODO: Right now, we have hardcoded the collision strategies for the game player.
  // This should be refactored when the entity configuration is read in from the config file.
  private void handleCollision(Entity e1, Entity e2) {
    applyCollisionStrategiesFromCollisionRules(e1, e2);
  }

  private void applyCollisionStrategiesFromCollisionRules(Entity e1, Entity e2) {
    for (CollisionRule collisionRule : myConfigModel.collisionRules()) {
      applyEntityACollisionStrategy(e1, e2, collisionRule);
      applyEntityBCollisionStrategy(e1, e2, collisionRule);
    }
  }

  private void applyEntityBCollisionStrategy(Entity e1, Entity e2, CollisionRule collisionRule) {
    if (checkEntityTypesMatch(e1, e2, collisionRule)) {
      for (CollisionEvent eventB : collisionRule.getEventsB()) {
        createAndApplyCollisionStrategy(e1, e2, eventB);
      }
    }
  }

  private void applyEntityACollisionStrategy(Entity e1, Entity e2, CollisionRule collisionRule) {
    if (checkEntityTypesMatch(e1, e2, collisionRule)) {
      for (CollisionEvent eventA : collisionRule.getEventsA()) {
        createAndApplyCollisionStrategy(e1, e2, eventA);
      }
    }
  }

  private void createAndApplyCollisionStrategy(Entity e1, Entity e2,
      CollisionEvent collisionEvent) {
    CollisionStrategy collisionStrategy = CollisionStrategyFactory.createCollisionStrategy(collisionEvent);
    try {
      collisionStrategy.handleCollision(new CollisionContextRecord(e1, e2, gameMap, gameState));
    } catch (EntityNotFoundException e) {
      LoggingManager.LOGGER.warn("Unable to handle collision event: {}", collisionEvent, e);
      throw new RuntimeException(e);
    }
  }

  private static boolean checkEntityTypesMatch(Entity e1, Entity e2, CollisionRule collisionRule) {
    return checkCollisionRuleEntityAMatches(e1, collisionRule) && checkCollisionRuleEntityBMatches(
        e2, collisionRule);
  }

  private static boolean checkCollisionRuleEntityBMatches(Entity e2, CollisionRule collisionRule) {
    return collisionRule.getEntityB().equals(e2.getEntityPlacement().getType().type());
  }

  private static boolean checkCollisionRuleEntityAMatches(Entity e1, CollisionRule collisionRule) {
    return collisionRule.getEntityA().equals(e1.getEntityPlacement().getType().type());
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

  private void stopGameLoop(GameEndStatus status) {
    if (gameEndHandler != null) {
      gameEndHandler.onGameEnd(status);
    }
  }
}
