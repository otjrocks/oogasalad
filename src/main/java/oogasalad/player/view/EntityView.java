package oogasalad.player.view;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.engine.utility.constants.Directions.Direction;
import oogasalad.player.model.Entity;

/**
 * A class used to display a specific Entity from the {@code Entity} model API. This view has a
 * {@code draw()} method which is called by the {@code GameMapView} class which extends a javafx
 * canvas.
 * <p>
 * This class was refactored from original extension of an ImageView to a draw method for a javafx
 * canvas using assistance from ChatGPT
 *
 * @author Owen Jennings
 * @author Troy Ludwig
 */
public class EntityView {

  private static final Map<String, Image> SPRITE_CACHE = new HashMap<>();
  private final Entity entity;
  private final int totalFrames;
  private final int height;
  private final int width;
  private final Image sprite;

  /**
   * Initialize an Entity view.
   *
   * @param entity     The Entity model used to represent this view.
   * @param gameFolder Full path to the game folder
   */
  public EntityView(Entity entity, String gameFolder) {
    this.entity = entity;
    this.totalFrames = entity.getEntityPlacement().getEntityFrameNumber();
    this.height = entity.getEntityPlacement().getEntityImageHeight();
    this.width = entity.getEntityPlacement().getEntityImageWidth();
    String imagePath = gameFolder + entity.getEntityPlacement().getEntityImagePath();
    this.sprite = SPRITE_CACHE.computeIfAbsent(imagePath, path -> {
      try {
        return new Image(new FileInputStream(imagePath)); // Load from file path
      } catch (FileNotFoundException e) {
        LoggingManager.LOGGER.warn("Unable to load entity image {}", imagePath);
        throw new RuntimeException("Failed to load image from path: " + imagePath, e);
      }
    });
  }


  /**
   * Draws this entity onto the provided GraphicsContext.
   *
   * @param gc         the canvas GraphicsContext
   * @param tileWidth  width of one map tile in pixels
   * @param tileHeight height of one map tile in pixels
   */
  public void draw(GraphicsContext gc, double tileWidth, double tileHeight) {
    int frameIndex = entity.getEntityPlacement().getCurrentFrame() % totalFrames;
    int dirOffset = 0;

    if (entity.getEntityDirection() == Direction.L) {
      dirOffset = height;
    }
    if (entity.getEntityDirection() == Direction.U) {
      dirOffset = 2 * height;
    }
    if (entity.getEntityDirection() == Direction.D) {
      dirOffset = 3 * height;
    }
    double destX = entity.getEntityPlacement().getX() * tileWidth;
    double destY = entity.getEntityPlacement().getY() * tileHeight;

    gc.drawImage(
        sprite,
        frameIndex * width,
        dirOffset,
        width,
        height,
        destX,
        destY,
        tileWidth,
        tileHeight
    );
  }
}