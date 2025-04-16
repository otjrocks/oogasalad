package oogasalad.player.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
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

  private static final Map<String, Image> SPRITE_CACHE = new HashMap<>();
  private final Entity entity;
  private final int totalFrames;
  private final int dimension;
  private final Image sprite;

  /**
   * Initialize an Entity view.
   *
   * @param entity      The Entity model used to represent this view.
   */
  public EntityView(Entity entity) {
    this.entity = entity;
    this.totalFrames = entity.getEntityPlacement().getEntityFrameNumber();
    this.dimension = 28;
    this.sprite = SPRITE_CACHE.computeIfAbsent(entity.getEntityPlacement().getEntityImagePath(), name -> {
      try (InputStream stream = EntityView.class.getClassLoader().getResourceAsStream("sprites/" + name)) {
        if (stream == null) throw new IllegalArgumentException("Image not found: " + name);
        return new Image(stream);
      } catch (IOException e) {
        throw new RuntimeException("Failed to load image: " + name, e);
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

    if (entity.getEntityDirection() == Direction.L){
      dirOffset = 28;
    }
    if (entity.getEntityDirection() == Direction.U){
      dirOffset = 56;
    }
    if(entity.getEntityDirection() == Direction.D){
      dirOffset = 84;
    }

    double destX = entity.getEntityPlacement().getX() * tileWidth;
    double destY = entity.getEntityPlacement().getY() * tileHeight;

    gc.drawImage(
        sprite,
            frameIndex * dimension,
        dirOffset,
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