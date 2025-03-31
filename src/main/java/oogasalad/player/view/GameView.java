package oogasalad.player.view;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.StackPane;
import oogasalad.engine.model.GameMap;

/**
 * The main game view of the player. Primarily encapsulates the game map view.
 *
 * @author Owen Jennings
 */
public class GameView extends StackPane {

  public static final int WIDTH = 500;
  public static final int HEIGHT = 500;
  public static int TILE_WIDTH = 0;
  public static int TILE_HEIGHT = 0;

  private GameMapView myGameMapView;
  private GameMap myGameMap;
  private AnimationTimer gameLoop;

  /**
   * Create the game view.
   *
   * @param gameMap The game map model you wish to use.
   */
  public GameView(GameMap gameMap) {
    TILE_WIDTH = WIDTH / gameMap.getWidth();
    TILE_HEIGHT = HEIGHT / gameMap.getHeight();
    myGameMapView = new GameMapView(gameMap);
    myGameMap = gameMap;
    this.setPrefSize(WIDTH, HEIGHT);
    this.setMinSize(WIDTH, HEIGHT);
    this.setMaxSize(WIDTH, HEIGHT);
    this.getChildren().add(myGameMapView);
    this.getStyleClass().add("game-view");

    initializeGameLoop();
  }


  // this and following methods are written by chat gpt

  /**
   * Initializes and starts the game loop using AnimationTimer.
   */
  private void initializeGameLoop() {
    gameLoop = new AnimationTimer() {
      private long lastUpdateTime = 0;

      @Override
      public void handle(long now) {
        // Calculate elapsed time in seconds (optional, for frame-dependent logic)
        double elapsedTime = (now - lastUpdateTime) / 1_000_000_000.0;

        // Only update if enough time has passed (e.g., 60 FPS)
        if (lastUpdateTime == 0 || elapsedTime > 1.0 / 5.0) {
          updateGame(elapsedTime);
          lastUpdateTime = now;
        }
      }
    };
    gameLoop.start(); // Start the game loop
  }

  /**
   * Updates the game state and refreshes the entity positions.
   *
   * @param elapsedTime Time passed since the last frame, useful for animations.
   */
  private void updateGame(double elapsedTime) {
    // Update the game map and entity positions
    myGameMap.update(); // Update game state (e.g., entity movements)
    myGameMapView.updateEntityPositions(); // Update entity views to reflect changes
  }

  /**
   * Stop the game loop if needed (optional).
   */
  public void stopGameLoop() {
    if (gameLoop != null) {
      gameLoop.stop();
    }
  }

}
