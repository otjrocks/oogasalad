package oogasalad.player.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.function.Consumer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.model.GameEndStatus;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.player.controller.GameLoopController;
import oogasalad.player.controller.GameMapController;

/**
 * A Canvas-based view for rendering the entire GameMap and all its corresponding entities.
 * <p>
 * This class was refactored to use a canvas using ChatGPT.
 *
 * @author Owen Jennings
 */
public class GameMapView {

  private final GameContextRecord myGameContext;
  private final GameMapController myGameMapController;
  private final List<EntityView> entityViews = new ArrayList<>();
  private GameLoopController myGameLoopController;
  private Consumer<Boolean> endGameCallback;
  private final Canvas myCanvas;

  /**
   * Initialize a game map view.
   *
   * @param gameContext The game context object for this view.
   * @param configModel The config model for this view.
   */
  public GameMapView(GameContextRecord gameContext, ConfigModel configModel) {
    myCanvas = new Canvas(GameView.GAME_VIEW_WIDTH, GameView.GAME_VIEW_HEIGHT);
    myGameContext = gameContext;
    myGameMapController = new GameMapController(myGameContext, configModel);
    myGameMapController.setGameEndHandler(status -> {
      pauseGame();
      if (endGameCallback != null && status != GameEndStatus.PAUSE_ONLY) {
        endGameCallback.accept(status == GameEndStatus.WIN);
      }
    });
    initializeEntityViews();
  }

  /**
   * Get the canvas element which is modified by this view.
   * @return A JavaFX Canvas which is drawn on by this view.
   */
  public Canvas getCanvas() {
    return myCanvas;
  }

  private void initializeEntityViews() {
    entityViews.clear();
    for (Entity entity : myGameContext.gameMap()) {
      entityViews.add(new EntityView(entity));
    }
  }

  /**
   * Call on each game tick to update models, handle removals, and redraw the canvas.
   */
  public void update() {
    try {
      myGameMapController.updateEntityModels();
    } catch (InvalidPositionException e) {
      throw new RuntimeException(e);
    }
    initializeEntityViews();  // rebuild views to reflect current entities (removals/additions)
    drawAll();
  }

  private void drawAll() {
    GraphicsContext gc = myCanvas.getGraphicsContext2D();
    gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());

    double tileWidth = myCanvas.getWidth() / myGameContext.gameMap().getWidth();
    double tileHeight =
        myCanvas.getHeight() / myGameContext.gameMap().getHeight();

    for (EntityView ev : entityViews) {
      ev.draw(gc, tileWidth, tileHeight);
    }
  }

  /**
   * Sets the {@link GameLoopController} associated with this {@code GameMapView}.
   * <p>
   * This method is used to link the game loop controller so that the view can trigger game loop
   * operations, such as pausing the game when a victory condition is met.
   *
   * @param controller the {@code GameLoopController} that manages the game's animation loop
   */
  public void setGameLoopController(GameLoopController controller) {
    this.myGameLoopController = controller;
  }

  private void pauseGame() {
    if (myGameLoopController != null) {
      myGameLoopController.pauseGame();
    }
  }

  /**
   * Sets the callback to be executed when the game ends.
   *
   * <p>This method allows external components (e.g., {@code GameView}) to register a handler
   * that responds to the end of gameplay, such as displaying a message or transitioning to another
   * screen.</p>
   *
   * @param callback trigger for callback.
   */
  public void setEndGameCallback(Consumer<Boolean> callback) {
    this.endGameCallback = callback;
  }
}
