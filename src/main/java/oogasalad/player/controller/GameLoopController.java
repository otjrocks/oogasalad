package oogasalad.player.controller;

import javafx.animation.AnimationTimer;
import oogasalad.engine.records.GameContext;
import oogasalad.player.view.GameMapView;

/**
 * A controller class that handles the game loop.
 */
public class GameLoopController {

  private AnimationTimer myGameLoop;
  private final GameContext myGameContext;
  private final GameMapView myGameMapView;

  /**
   * Initialize the game loop controller and start the animation. Calling the constructor will
   * automatically start the animation.
   *
   * @param gameContext The game context to use for updating each frame.
   * @param gameMapView The game map view used with this animation loop.
   */
  public GameLoopController(GameContext gameContext, GameMapView gameMapView) {
    myGameContext = gameContext;
    myGameMapView = gameMapView;
    initializeGameLoop();
  }
  // this and following methods are written by ChatGPT

  /**
   * Initializes and starts the game loop using AnimationTimer.
   */
  private void initializeGameLoop() {
    myGameLoop = new AnimationTimer() {
      private long lastUpdateTime = 0;

      @Override
      public void handle(long now) {
        double elapsedTime = (now - lastUpdateTime) / 1_000_000_000.0;
        if (checkEnoughTimeHasPassed(elapsedTime)) {
          updateGame();
          lastUpdateTime = now;
        }
      }

      private boolean checkEnoughTimeHasPassed(double elapsedTime) {
        return lastUpdateTime == 0 || elapsedTime > 1.0 / 60.0;
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
  }

  /**
   * Stops the loop when called
   */
  public void pauseGame() {
    if (myGameLoop != null || myGameMapView.isDeathAnimationRunning()){
      myGameLoop.stop();
    }
  }

  /**
   * Starts the loop again
   */
  public void resumeGame() {
    if (myGameLoop != null && !myGameMapView.isDeathAnimationRunning()) {
      myGameLoop.start();
    }
  }

}
