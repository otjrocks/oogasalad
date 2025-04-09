package oogasalad.player.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.records.GameContext;
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

  private boolean isDeathAnimationRunning = false;
  private Timeline deathAnimationTimeline;
  /**
   * Initialize a game map view.
   *
   * @param gameContext The game context object for this view.
   */
  public GameMapView(GameContext gameContext) {
    super(GameView.GAME_VIEW_WIDTH, GameView.GAME_VIEW_HEIGHT);
    myGameContext = gameContext;
    myGameMapController = new GameMapController(myGameContext, this);
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
    if (isDeathAnimationRunning) return;

    isDeathAnimationRunning = true;

    deathAnimationTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0.1), e -> {
              pacManEntity.getEntityPlacement().incrementDeathFrame();

              EntityView view = null;
              for (EntityView entityView : entityViews) {
                if(entityView.getEntity().equals(pacManEntity)) {
                  view = entityView;
                }
              }

              if(view == null) {
                entityViews.remove(view);
                entityViews.add(new EntityView(pacManEntity, 11));
              }

              drawAll();

              if (pacManEntity.getEntityPlacement().getDeathFrame() >= 11) {
                endDeathAnimation(pacManEntity);
              }
            })
    );
    deathAnimationTimeline.setCycleCount(Timeline.INDEFINITE);
    deathAnimationTimeline.play();
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
    myGameMapController.updateEntityModels();
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

  public boolean isDeathAnimationRunning() {
    return isDeathAnimationRunning;
  }
}
