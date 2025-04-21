package oogasalad.player.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.animation.AnimationTimer;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.exceptions.EntityNotFoundException;
import oogasalad.engine.exceptions.InvalidPositionException;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.model.ParsedLevelRecord;
import oogasalad.engine.records.config.model.SpawnEventRecord;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.model.Entity;
import oogasalad.player.model.api.SpawnEventStrategyFactory;
import oogasalad.player.model.strategies.collision.TemporaryModeChangeStrategy;
import oogasalad.player.model.strategies.spawnevent.SpawnEventStrategyInterface;
import oogasalad.player.view.GameMapView;

/**
 * A controller class that handles the game loop.
 */
public class GameLoopController {

  private AnimationTimer myGameLoop;
  private final GameContextRecord myGameContext;
  private final GameMapView myGameMapView;
  private final ParsedLevelRecord myLevel;
  private final Map<SpawnEventRecord, Entity> activeSpawnedEntities = new HashMap<>();
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
      ParsedLevelRecord level) {
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
        attemptRemovingEntity(entity);
      }
      activeSpawnedEntities.clear();
      myTotalElapsedTime = 0;
    }
  }

  private void attemptRemovingEntity(Entity entity) {
    if (myGameContext.gameMap().contains(entity)) {
      try {
        myGameContext.gameMap().removeEntity(entity);
      } catch (EntityNotFoundException e) {
        LoggingManager.LOGGER.warn("Unable to remove entity {}", entity, e);
      }
    }
  }

  /**
   * Updates the game state and refreshes the entity positions.
   */
  private void updateGame() {
    //Updates the game map and entity positions
    myGameContext.gameMap().update();
    myGameMapView.update();
    handleModeChanges();
    handleSpawnEvents();
  }

  private void handleModeChanges() {

    double currentTime = myGameContext.gameState().getTimeElapsed();
    Iterator<Map.Entry<Entity, TemporaryModeChangeStrategy.ModeChangeInfo>> iterator =
            myGameContext.gameMap().getActiveModeChanges().entrySet().iterator();

    while (iterator.hasNext()) {
      Map.Entry<Entity, TemporaryModeChangeStrategy.ModeChangeInfo> entry = iterator.next();
      Entity entity = entry.getKey();
      TemporaryModeChangeStrategy.ModeChangeInfo info = entry.getValue();
      if (currentTime >= info.revertTime) {
        entity.getEntityPlacement().setMode(info.originalMode);
        iterator.remove();
      }
    }
  }

  private void handleSpawnEvents() {
    for (SpawnEventRecord spawnEvent : myLevel.spawnEvents()) {
      handleSpawnEvent(spawnEvent);
    }
  }

  private void handleSpawnEvent(SpawnEventRecord spawnEvent) {
    checkAndHandleSpawn(spawnEvent);
    checkAndHandleDespawn(spawnEvent);
  }

  private void checkAndHandleSpawn(SpawnEventRecord spawnEvent) {
    SpawnEventStrategyInterface spawnEventStrategy = SpawnEventStrategyFactory.createSpawnEventStrategy(
        spawnEvent.spawnCondition().type());
    if (spawnEventStrategy.shouldSpawn(spawnEvent, myGameContext
    )) {
      spawnEntity(spawnEvent);
    }
  }

  private void checkAndHandleDespawn(SpawnEventRecord spawnEvent) {
    SpawnEventStrategyInterface despawnEventStrategy = SpawnEventStrategyFactory.createSpawnEventStrategy(
        spawnEvent.despawnCondition().type());
    if (despawnEventStrategy.shouldDespawn(spawnEvent, myGameContext
    )) {
      despawnEntity(spawnEvent);
    }
  }


  private void spawnEntity(SpawnEventRecord spawnEvent) {
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

  private void despawnEntity(SpawnEventRecord spawnEvent) {
    Entity entityToRemove = activeSpawnedEntities.get(spawnEvent);
    attemptRemovingEntity(entityToRemove);
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
