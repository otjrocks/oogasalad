package oogasalad.player.controller;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.AnimationTimer;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.records.GameContext;
import oogasalad.engine.records.newconfig.model.ParsedLevel;
import oogasalad.engine.records.newconfig.model.SpawnEvent;
import oogasalad.player.view.GameMapView;

/**
 * A controller class that handles the game loop.
 */
public class GameLoopController {

  private AnimationTimer myGameLoop;
  private final GameContext myGameContext;
  private final GameMapView myGameMapView;
  private final ParsedLevel myLevel;
  private double myTotalElapsedTime = 0;
  private final Map<SpawnEvent, Entity> activeSpawnedEntities = new HashMap<>();


  /**
   * Initialize the game loop controller and start the animation. Calling the constructor will
   * automatically start the animation.
   *
   * @param gameContext The game context to use for updating each frame.
   * @param gameMapView The game map view used with this animation loop.
   * @param level       The parsed level information for this game loop.
   */
  public GameLoopController(GameContext gameContext, GameMapView gameMapView, ParsedLevel level) {
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
          myTotalElapsedTime += elapsedTime;
          lastUpdateTime = now;
        }
      }
    };
    myGameLoop.start(); // Start the game loop
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
      double spawnTime = parseTimeCondition(spawnEvent.spawnCondition().type());
      double despawnTime = parseTimeCondition(spawnEvent.despawnCondition().type());
      handleIndividualSpawnEvent(spawnEvent, spawnTime, despawnTime);
    }
  }

  private void handleIndividualSpawnEvent(SpawnEvent spawnEvent, double spawnTime,
      double despawnTime) {
    boolean shouldBeSpawned = myTotalElapsedTime >= spawnTime;
    boolean shouldBeDespawned = myTotalElapsedTime >= spawnTime + despawnTime;

    // Spawn if time has passed
    spawnEntityIfNecessary(spawnEvent, shouldBeSpawned);
    // Despawn if time has passed
    handleDespawnIfNecessary(spawnEvent, shouldBeDespawned);
  }

  private void spawnEntityIfNecessary(SpawnEvent spawnEvent, boolean shouldBeSpawned) {
    if (shouldBeSpawned && !activeSpawnedEntities.containsKey(spawnEvent)) {
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

  private void handleDespawnIfNecessary(SpawnEvent spawnEvent, boolean shouldBeDespawned) {
    if (shouldBeDespawned && activeSpawnedEntities.containsKey(spawnEvent)) {
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
  }


  private double parseTimeCondition(String condition) {
    if (condition != null && condition.startsWith("TimeElapsed(") && condition.endsWith(")")) {
      String timeStr = condition.substring("TimeElapsed(".length(), condition.length() - 1);
      return Double.parseDouble(timeStr);
    }
    return Double.MAX_VALUE; // Never triggers if invalid
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
