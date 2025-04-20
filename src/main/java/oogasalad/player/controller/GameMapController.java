package oogasalad.player.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oogasalad.engine.config.CollisionRule;
import oogasalad.engine.exceptions.EntityNotFoundException;
import oogasalad.engine.exceptions.InvalidPositionException;
import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.ConfigModelRecord;
import oogasalad.engine.records.config.model.CollisionEventInterface;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.engine.utility.constants.GameEndStatus;
import oogasalad.player.controller.api.GameEndHandlerInterface;
import oogasalad.player.model.Entity;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.GameStateInterface;
import oogasalad.player.model.api.CollisionStrategyFactory;
import oogasalad.player.model.api.GameOutcomeFactory;
import oogasalad.player.model.strategies.collision.CollisionStrategyInterface;
import oogasalad.player.model.strategies.gameoutcome.GameOutcomeStrategyInterface;

/**
 * A controller that handles all the updates of the game map models whenever the game map view is
 * updated.
 *
 * @author Owen Jennings
 */
public class GameMapController {

  // TODO: This should be removed when hardcoded methods are refactored
  private static final int SPRITE_ANIMATION_SPEED = 6;
  private final GameMapInterface gameMap;
  private final GameStateInterface gameState;
  private final GameContextRecord gameContext;
  private int frameCount = 0;
  private GameEndHandlerInterface gameEndHandler;
  private final GameOutcomeStrategyInterface winGameOutcomeStrategy;
  private final GameOutcomeStrategyInterface loseGameOutcomeStrategy;
  private final ConfigModelRecord myConfigModel;

  /**
   * Create a game map controller with the provided game context.
   *
   * @param gameContext The game context object for this controller.
   * @param configModel The game config model for this controller.
   */
  public GameMapController(GameContextRecord gameContext, ConfigModelRecord configModel) {
    gameMap = gameContext.gameMap();
    gameState = gameContext.gameState();
    this.gameContext = gameContext;
    myConfigModel = configModel;
    winGameOutcomeStrategy = GameOutcomeFactory.createWinStrategy(configModel.winCondition());
    loseGameOutcomeStrategy = GameOutcomeFactory.createLoseStrategy(configModel.loseCondition());
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

    checkGameOutcome(winGameOutcomeStrategy);
    checkGameOutcome(loseGameOutcomeStrategy);
  }

  private void checkGameOutcome(GameOutcomeStrategyInterface gameOutcomeStrategy) {
    if (gameOutcomeStrategy.hasGameEnded(gameContext)) {
      gameState.setGameOver(true);
      String result = gameOutcomeStrategy.getGameOutcome(gameContext);
      GameEndStatus status = result.equals("Level Passed") ? GameEndStatus.WIN : GameEndStatus.LOSS;
      stopGameLoop(status);
    }
  }

  private void handleCollision(Entity e1, Entity e2) {
    applyCollisionStrategiesFromCollisionRules(e1, e2);
  }

  private void applyCollisionStrategiesFromCollisionRules(Entity e1, Entity e2) {
    for (CollisionRule collisionRule : myConfigModel.collisionRules()) {
      if (checkEntityTypesAndModesMatch(e1, e2, collisionRule)) {
        applyEntityACollisionStrategy(e1, e2, collisionRule);
        applyEntityBCollisionStrategy(e1, e2, collisionRule);
      }
    }
  }

  private void applyEntityBCollisionStrategy(Entity e1, Entity e2, CollisionRule collisionRule) {
    for (CollisionEventInterface eventB : collisionRule.getEventsB()) {
      createAndApplyCollisionStrategy(e1, e2, eventB);
    }
  }

  private void applyEntityACollisionStrategy(Entity e1, Entity e2, CollisionRule collisionRule) {
    for (CollisionEventInterface eventA : collisionRule.getEventsA()) {
      createAndApplyCollisionStrategy(e1, e2, eventA);
    }
  }

  private void createAndApplyCollisionStrategy(Entity e1, Entity e2,
      CollisionEventInterface collisionEvent) {
    CollisionStrategyInterface collisionStrategy = CollisionStrategyFactory.createCollisionStrategy(
        collisionEvent);
    try {
      collisionStrategy.handleCollision(new CollisionContextRecord(e1, e2, gameMap, gameState));
    } catch (EntityNotFoundException e) {
      LoggingManager.LOGGER.warn("Unable to handle collision event: {}", collisionEvent, e);
      throw new RuntimeException(e);
    }
  }

  private static boolean checkEntityTypesAndModesMatch(Entity e1, Entity e2,
      CollisionRule collisionRule) {
    return checkCollisionRuleEntityAMatches(e1, collisionRule) && checkCollisionRuleEntityBMatches(
        e2, collisionRule);
  }

  private static boolean checkCollisionRuleEntityBMatches(Entity e2, CollisionRule collisionRule) {
    return collisionRule.getEntityB().equals(e2.getEntityPlacement().getType().type())
        && collisionRule.getModeB().equals(e2.getEntityPlacement().getMode());
  }

  private static boolean checkCollisionRuleEntityAMatches(Entity e1, CollisionRule collisionRule) {
    return collisionRule.getEntityA().equals(e1.getEntityPlacement().getType().type())
        && collisionRule.getModeA().equals(e1.getEntityPlacement().getMode());
  }

  private List<List<Entity>> getAllCollisions() {
    List<List<Entity>> collisions = new ArrayList<>();
    for (Entity a : gameMap) {
      for (Entity b : gameMap) {
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
  public void setGameEndHandler(GameEndHandlerInterface handler) {
    this.gameEndHandler = handler;
  }

  private void stopGameLoop(GameEndStatus status) {
    if (gameEndHandler != null) {
      gameEndHandler.onGameEnd(status);
    }
  }
}
