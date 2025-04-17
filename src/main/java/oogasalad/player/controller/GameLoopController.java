package oogasalad.player.controller;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.AnimationTimer;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.model.ParsedLevel;
import oogasalad.engine.records.config.model.SpawnEvent;
import oogasalad.player.model.spawnevent.SpawnEventStrategy;
import oogasalad.player.model.spawnevent.SpawnEventStrategyFactory;
import oogasalad.player.view.GameMapView;

/**
 * A controller class that handles the game loop.
 */
public class GameLoopController {

  private AnimationTimer myGameLoop;
  private final GameContextRecord myGameContext;
  private final GameMapView myGameMapView;
  private final ParsedLevel myLevel;
  private final Map<SpawnEvent, Entity> activeSpawnedEntities = new HashMap<>();
  private double myTotalElapsedTime = 0;


  /**
   * Initialize the game loop controller and start the animation. Calling the constructor will
   * automatically start the animation.
   *
   * @param gameContext The game context to use for updating each frame.
   * @param gameMapView The game map view used with this animation loop.
   * @param level       The parsed level information for this game loop.
   */
  public GameLoopController(GameContextRecord gameContext, GameMapView gameMapView,
      ParsedLevel level) {
    myGameContext = gameContext;
    myGameMapView = gameMapView;
    myLevel = level;
    initializeGameLoop();
  }
  // this and following methods are written by ChatGPT

  /**
   * Initializes and starts the game loop using AnimationTimer.
   */
  private void initializeGameLoop() {
    myGameLoop = new AnimationTimer() {
      private long lastUpdateTime = -1;

      @Override
      public void handle(long now) {
        if (lastUpdateTime < 0) {
          lastUpdateTime = now;
          return;
        }

        double elapsedTime = (now - lastUpdateTime) / 1_000_000_000.0;

        if (elapsedTime > 1.0 / 60.0) {
          updateGame();
          clearActiveSpawnedEntitiesIfTimeElapsedWasReset();
          myTotalElapsedTime += elapsedTime;
          myGameContext.gameState()
              .setTimeElapsed(myGameContext.gameState().getTimeElapsed() + elapsedTime);
          lastUpdateTime = now;
        }
      }
    };
    myGameLoop.start(); // Start the game loop
  }

  private void clearActiveSpawnedEntitiesIfTimeElapsedWasReset() {
    if (myGameContext.gameState().getTimeElapsed() < myTotalElapsedTime) {
      for (Entity entity : activeSpawnedEntities.values()) {
        try {
          myGameContext.gameMap().removeEntity(entity);
        } catch (EntityNotFoundException e) {
          LoggingManager.LOGGER.warn("Unable to remove entity " + entity, e);
        }
      }
      activeSpawnedEntities.clear();
      myTotalElapsedTime = 0;
    }
  }

  /**
   * Updates the game state and refreshes the entity positions.
   */
  private void updateGame() {
    //Updates the game map and entity positions
    myGameContext.gameMap().update();
    myGameMapView.update();
    handleSpawnEvents();
  }

  private void handleSpawnEvents() {
    for (SpawnEvent spawnEvent : myLevel.spawnEvents()) {
      handleSpawnEvent(spawnEvent);
    }
  }

  private void handleSpawnEvent(SpawnEvent spawnEvent) {
    checkAndHandleSpawn(spawnEvent);
    checkAndHandleDespawn(spawnEvent);
  }

  private void checkAndHandleSpawn(SpawnEvent spawnEvent) {
    SpawnEventStrategy spawnEventStrategy = SpawnEventStrategyFactory.createSpawnEventStrategy(
        spawnEvent.spawnCondition().type());
    if (spawnEventStrategy.shouldSpawn(spawnEvent, myGameContext
    )) {
      spawnEntity(spawnEvent);
    }
  }

  private void checkAndHandleDespawn(SpawnEvent spawnEvent) {
    SpawnEventStrategy despawnEventStrategy = SpawnEventStrategyFactory.createSpawnEventStrategy(
        spawnEvent.despawnCondition().type());
    if (despawnEventStrategy.shouldDespawn(spawnEvent, myGameContext
    )) {
      despawnEntity(spawnEvent);
    }
  }


  private void spawnEntity(SpawnEvent spawnEvent) {
    // only spawn if it has not already been spawned
    if (!activeSpawnedEntities.containsKey(spawnEvent)) {
      // Create and spawn entity
      Entity newEntity = new Entity(null,
          new EntityPlacement(spawnEvent.entityType(), spawnEvent.x(), spawnEvent.y(),
              spawnEvent.mode()),
          myGameContext.gameMap());
      try {
        myGameContext.gameMap().addEntity(newEntity);
        activeSpawnedEntities.put(spawnEvent, newEntity);
      } catch (InvalidPositionException e) {
        LoggingManager.LOGGER.warn("Could not add spawn entity {}", spawnEvent);
        throw new RuntimeException(e);
      }
    }
  }

  private void despawnEntity(SpawnEvent spawnEvent) {
    Entity entityToRemove = activeSpawnedEntities.get(spawnEvent);
    try {
      myGameContext.gameMap().removeEntity(entityToRemove);
    } catch (EntityNotFoundException e) {
      LoggingManager.LOGGER.info(
          "Entity not despawned following configuration rules, since it no longer exists on the game map {}",
          spawnEvent);
    }
    activeSpawnedEntities.remove(spawnEvent);

  }

  /**
   * Stops the loop when called
   */
  public void pauseGame() {
    if (myGameLoop != null) {
      myGameLoop.stop();
    }
  }

  /**
   * Starts the loop again
   */
  public void resumeGame() {
    if (myGameLoop != null) {
      myGameLoop.start();
    }
  }

}
