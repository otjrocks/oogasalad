package oogasalad.player.view;

import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
  private static final Image SPRITE_SHEET = new Image(
      Objects.requireNonNull(
          EntityView.class.getClassLoader()
              .getResourceAsStream("sprites/Pacman.png"))
  );

  private final Entity entity;
  private final int totalFrames;
  private final int dimension;

  /**
   * Initialize an Entity view.
   *
   * @param entity      The Entity model used to represent this view.
   * @param totalFrames The total number of frames in the sprite file for this entity view.
   */
  public EntityView(Entity entity, int totalFrames) {
    this.entity = entity;
    this.totalFrames = totalFrames;
    // Dimension of each frame in the sprite sheet
    this.dimension = Integer.parseInt(
        SPRITE_DATA.getString(
            (entity.getEntityPlacement().getTypeString() + "_DIM").toUpperCase()
        )
    );
  }

  /**
   * Draws this entity onto the provided GraphicsContext.
   *
   * @param gc         the canvas GraphicsContext
   * @param tileWidth  width of one map tile in pixels
   * @param tileHeight height of one map tile in pixels
   */
  public void draw(GraphicsContext gc, double tileWidth, double tileHeight) {
    int frameIndex, offsetX, offsetY;
    if (entity.getEntityPlacement().isInDeathAnimation()) {
      frameIndex = entity.getEntityPlacement().getDeathFrame();
      offsetX = Integer.parseInt(SPRITE_DATA.getString("PACMAN_DEATH_X_OFFSET"));
      offsetY = Integer.parseInt(SPRITE_DATA.getString("PACMAN_DEATH_Y_OFFSET"));
    } else {
      frameIndex = entity.getEntityPlacement().getCurrentFrame() % totalFrames;
      char dir = entity.getEntityDirection();
      String prefix = (entity.getEntityPlacement().getTypeString()
              + (dir == ' ' || dir == '\0' ? "_R" : "_" + dir))
              .toUpperCase();

      offsetX = Integer.parseInt(SPRITE_DATA.getString(prefix + "_X_OFFSET"));
      offsetY = Integer.parseInt(SPRITE_DATA.getString(prefix + "_Y_OFFSET"));
    }

    double destX = entity.getEntityPlacement().getX() * tileWidth;
    double destY = entity.getEntityPlacement().getY() * tileHeight;

    gc.drawImage(
        SPRITE_SHEET,
        frameIndex * dimension + offsetX,
        offsetY,
        dimension,
        dimension,
        destX,
        destY,
        tileWidth,
        tileHeight
    );
  }

  public Entity getEntity() {
    return entity;
  }
}