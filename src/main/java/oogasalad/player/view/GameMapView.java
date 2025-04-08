package oogasalad.player.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.records.GameContext;
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

  /**
   * Initialize a game map view.
   *
   * @param gameContext The game context object for this view.
   */
  public GameMapView(GameContext gameContext) {
    super(GameView.WIDTH, GameView.HEIGHT);
    myGameContext = gameContext;
    myGameMapController = new GameMapController(myGameContext);
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
}
