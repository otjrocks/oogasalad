package oogasalad.player.view;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.StackPane;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameState;

public class GameView extends StackPane {

  public static final int WIDTH = GameConfig.WIDTH - 2 * GameConfig.MARGIN;
  public static final int HEIGHT = GameConfig.HEIGHT - 2 * GameConfig.MARGIN;

  private final GameMapView myGameMapView;
  private final GameMap myGameMap;
  private AnimationTimer gameLoop; // ✅ store as field

  public GameView(GameMap gameMap, GameState gameState) {
    super();
    myGameMapView = new GameMapView(gameMap, gameState);
    myGameMap = gameMap;
    this.setPrefSize(WIDTH, HEIGHT);
    this.setMinSize(WIDTH, HEIGHT);
    this.setMaxSize(WIDTH, HEIGHT);
    this.getChildren().add(myGameMapView);
    this.getStyleClass().add("game-view");

    this.setFocusTraversable(true); // 🔑 ensure key input
    initializeGameLoop();
  }

  private void initializeGameLoop() {
    gameLoop = new AnimationTimer() {
      private long lastUpdateTime = 0;

      @Override
      public void handle(long now) {
        double elapsedTime = (now - lastUpdateTime) / 1_000_000_000.0;
        if (lastUpdateTime == 0 || elapsedTime > 1.0 / 60.0) {
          updateGame();
          lastUpdateTime = now;
        }
      }
    };
    gameLoop.start();
  }

  private void updateGame() {
    myGameMap.update();
    myGameMapView.updateEntityPositions();
  }

  public void pauseGame() {
    if (gameLoop != null) gameLoop.stop();
  }

  public void resumeGame() {
    if (gameLoop != null) gameLoop.start();
  }
}
