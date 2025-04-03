package oogasalad.player.view;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.StackPane;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;

/**
 * The main game view of the player. Primarily encapsulates the game map view.
 *
 * @author Owen Jennings
 */
public class GameView extends StackPane {

  public static final int WIDTH = GameConfig.WIDTH - 2 * GameConfig.MARGIN;
  public static final int HEIGHT = GameConfig.HEIGHT - 2 * GameConfig.MARGIN;

  private final GameMapView myGameMapView;
  private final GameMap myGameMap;

  /**
   * Create the game view.
   *
   * @param gameMap The game map model you wish to use.
   */
  public GameView(GameMap gameMap, GameState gameState) {
    super();
    myGameMapView = new GameMapView(gameMap, gameState);
    myGameMap = gameMap;
    this.setPrefSize(WIDTH, HEIGHT);
    this.setMinSize(WIDTH, HEIGHT);
    this.setMaxSize(WIDTH, HEIGHT);
    this.getChildren().add(myGameMapView);
    this.getStyleClass().add("game-view");

    initializeGameLoop();
  }

  // this and following methods are written by ChatGPT

  /**
   * Initializes and starts the game loop using AnimationTimer.
   */
  private void initializeGameLoop() {
    // Calculate elapsed time in seconds (optional, for frame-dependent logic)
    // Only update if enough time has passed (e.g., 60 FPS)
    AnimationTimer gameLoop = new AnimationTimer() {
      private long lastUpdateTime = 0;

      @Override
      public void handle(long now) {
        // Calculate elapsed time in seconds (optional, for frame-dependent logic)
        double elapsedTime = (now - lastUpdateTime) / 1_000_000_000.0;

        // Only update if enough time has passed (e.g., 60 FPS)
        if (checkEnoughTimeHasPassed(elapsedTime)) {
          updateGame();
          lastUpdateTime = now;
        }
      }

      private boolean checkEnoughTimeHasPassed(double elapsedTime) {
        return lastUpdateTime == 0 || elapsedTime > 1.0 / 60.0;
      }
    };
    gameLoop.start(); // Start the game loop
  }

  /**
   * Updates the game state and refreshes the entity positions.
   */
  private void updateGame() {
    // Update the game map and entity positions

    myGameMap.update(); // Update game state (e.g., entity movements)
    myGameMapView.updateEntityPositions(); // Update entity views to reflect changes
  }

}
