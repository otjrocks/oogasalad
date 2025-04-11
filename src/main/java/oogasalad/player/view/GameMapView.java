package oogasalad.player.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import java.util.function.Consumer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.model.GameEndStatus;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.records.GameContext;
import oogasalad.player.controller.GameLoopController;
import oogasalad.player.controller.GameMapController;

import static oogasalad.player.controller.GameMapController.PACMAN_INITIAL_X;
import static oogasalad.player.controller.GameMapController.PACMAN_INITIAL_Y;

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
  private boolean isDeathAnimationRunning = false;
  private Timeline deathAnimationTimeline;
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
    myGameMapController = new GameMapController(myGameContext, this, configModel);
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
    for (Iterator<Entity> it = myGameContext.gameMap().iterator(); it.hasNext(); ) {
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
   * Starts the death animation when Pac-Man dies.
   */
  public void triggerPacManDeathAnimation(Entity pacManEntity) {
    if (isDeathAnimationRunning) {
      return;
    }

    isDeathAnimationRunning = true;

    initializeOrReplaceEntityView(pacManEntity);

    deathAnimationTimeline = new Timeline(
        new KeyFrame(Duration.seconds(0.1), e -> updateDeathAnimationFrame(pacManEntity))
    );
    deathAnimationTimeline.setCycleCount(Timeline.INDEFINITE);
    deathAnimationTimeline.play();
  }

  private void initializeOrReplaceEntityView(Entity entity) {
    EntityView existingView = findEntityView(entity);

    if (existingView == null) {
      entityViews.add(new EntityView(entity, 11));
    }
  }

  private EntityView findEntityView(Entity entity) {
    for (EntityView view : entityViews) {
      if (view.getEntity().equals(entity)) {
        return view;
      }
    }
    return null;
  }

  private void updateDeathAnimationFrame(Entity pacManEntity) {
    pacManEntity.getEntityPlacement().incrementDeathFrame();

    drawAll();

    if (pacManEntity.getEntityPlacement().getDeathFrame() >= 11) {
      endDeathAnimation(pacManEntity);
    }
  }

  /**
   * Ends the death animation and resets Pac-Man's state.
   */
  public void endDeathAnimation(Entity pacManEntity) {
    isDeathAnimationRunning = false;
    pacManEntity.getEntityPlacement().setX(PACMAN_INITIAL_X);
    pacManEntity.getEntityPlacement().setY(PACMAN_INITIAL_Y);
    pacManEntity.getEntityPlacement().setDeathFrame(0);
    pacManEntity.getEntityPlacement().setInDeathAnimation(false);

    deathAnimationTimeline.stop();
    update();
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
    double tileHeight = getHeight() / myGameContext.gameMap().getHeight();

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
   * Checks if death animation is currently running
   */
  public boolean isDeathAnimationRunning() {
    return isDeathAnimationRunning;
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
