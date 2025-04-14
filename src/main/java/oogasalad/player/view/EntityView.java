package oogasalad.player.view;

import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import oogasalad.engine.enums.Directions.Direction;
import oogasalad.engine.model.entity.Entity;

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

  private static final ResourceBundle SPRITE_DATA =
      ResourceBundle.getBundle("oogasalad.sprite_data.sprites");

  private final Entity entity;
  private final int totalFrames;
  private final int dimension;
  private final Image sprite;

  /**
   * Initialize an Entity view.
   *
   * @param entity      The Entity model used to represent this view.
   * @param totalFrames The total number of frames in the sprite file for this entity view.
   */
  public EntityView(Entity entity, int totalFrames) {
    this.entity = entity;
    this.totalFrames = totalFrames;
    this.dimension = 64;
    Direction dir = entity.getEntityDirection() != null ? entity.getEntityDirection() : Direction.NONE;
    String suffix = dir == Direction.NONE ? "_R" : "_" + dir.name();
    String imageName = (entity.getEntityPlacement().getTypeString() +
            "_" + entity.getEntityPlacement().getMode() +
            suffix).toUpperCase();
    System.out.println("Attempting to load image: " + imageName);
    this.sprite = new Image(
            Objects.requireNonNull(
                    EntityView.class.getClassLoader()
                            .getResourceAsStream(
                                    "sprites/" + imageName + ".png"
                            )));
  }

  /**
   * Draws this entity onto the provided GraphicsContext.
   *
   * @param gc         the canvas GraphicsContext
   * @param tileWidth  width of one map tile in pixels
   * @param tileHeight height of one map tile in pixels
   */
  public void draw(GraphicsContext gc, double tileWidth, double tileHeight) {
    int frameIndex;
    if (entity.getEntityPlacement().isInDeathAnimation()) {
      frameIndex = entity.getEntityPlacement().getDeathFrame();
    } else {
      frameIndex = entity.getEntityPlacement().getCurrentFrame() % totalFrames;
    }

    double destX = entity.getEntityPlacement().getX() * tileWidth;
    double destY = entity.getEntityPlacement().getY() * tileHeight;

    gc.drawImage(
        sprite,
        0,
        frameIndex * dimension,
        dimension,
        dimension,
        destX,
        destY,
        tileWidth,
        tileHeight
    );
  }

  /**
   * returns the entity object associated with the current EntityView
   */
  public Entity getEntity() {
    return entity;
  }
}