package oogasalad.player.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import java.util.function.Consumer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.model.GameEndStatus;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.records.GameContext;
import oogasalad.player.controller.GameLoopController;
import oogasalad.player.controller.GameMapController;

/**
 * A Canvas-based view for rendering the entire GameMap and all its corresponding entities.
 * <p>
 * This class was refactored to use a canvas using ChatGPT.
 *
 * @author Owen Jennings
 */
public class GameMapView extends Canvas {

  private final GameContext myGameContext;
  private final GameMapController myGameMapController;
  private final List<EntityView> entityViews = new ArrayList<>();
  private final ResourceBundle SPRITE_DATA =
      ResourceBundle.getBundle("oogasalad.sprite_data.sprites");
  private GameLoopController myGameLoopController;
  private Consumer<Boolean> endGameCallback;

  /**
   * Initialize a game map view.
   *
   * @param gameContext The game context object for this view.
   * @param configModel The config model for this view.
   */
  public GameMapView(GameContext gameContext, ConfigModel configModel) {
    super(GameView.GAME_VIEW_WIDTH, GameView.GAME_VIEW_HEIGHT);
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

  private void initializeEntityViews() {
    entityViews.clear();
    for (Iterator<Entity> it = myGameContext.gameMap().iterator();
        it.hasNext(); ) {
      Entity entity = it.next();
      int frames = Integer.parseInt(
          SPRITE_DATA.getString(
              (entity.getEntityPlacement().getTypeString() + "_FRAMES").toUpperCase()
          )
      );
      entityViews.add(new EntityView(entity, frames));
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
    GraphicsContext gc = getGraphicsContext2D();
    gc.clearRect(0, 0, getWidth(), getHeight());

    double tileWidth = getWidth() / myGameContext.gameMap().getWidth();
    double tileHeight =
        getHeight() / myGameContext.gameMap().getHeight();

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
