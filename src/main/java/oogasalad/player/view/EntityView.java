package oogasalad.player.view;

import java.util.Objects;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;

/**
 * A view used to display a specific Entity from the {@code Entity} model API.
 *
 * @author Owen Jennings
 * @author Troy Ludwig
 */
public class EntityView extends ImageView {

  private final ResourceBundle SPRITE_DATA = ResourceBundle.getBundle("oogasalad.sprite_data.sprites");

  private final Entity myEntity;
  private final int myTotalFrames;
  private int myOffsetX;
  private int myOffsetY;

  private static final Image SPRITE_SHEET = new Image(
          Objects.requireNonNull(EntityView.class.getClassLoader().getResourceAsStream("sprites/Pacman.png"))
  );

  /**
   * Create an Entity view using the entity data provided.
   *
   * @param gameMap    The game map being used for this entity view.
   * @param entity  The entity used to initialize the view.
   */
  public EntityView(GameMap gameMap, Entity entity, int totalFrames) {
    super(SPRITE_SHEET);
    myEntity = entity;
    myTotalFrames = totalFrames;
    getOffsetX();
    getOffsetY();
    this.setFitWidth((double) GameView.WIDTH / gameMap.getWidth());
    this.setFitHeight((double) GameView.HEIGHT / gameMap.getHeight());
    setupAnimation();
  }

  private void setupAnimation() {
    int dimension = Integer.parseInt(SPRITE_DATA.getString((myEntity.getEntityPlacement().getTypeString() + "_DIM").toUpperCase()));
    int x = (myEntity.getEntityPlacement().getCurrentFrame() % myTotalFrames) * dimension + myOffsetX;
    this.setViewport(new Rectangle2D(x, myOffsetY, dimension, dimension));
  }

  /**
   * Ensures a valid X-Offset key is sent even when the entity doesn't have a current direction
   */
  public void getOffsetX() {
    if(myEntity.getEntityDirection() == ' ' || myEntity.getEntityDirection() == '\0') {
      myOffsetX = Integer.parseInt(SPRITE_DATA.getString((myEntity.getEntityPlacement().getTypeString() + "_R_X_OFFSET").toUpperCase()));
    }
    else{
      myOffsetX = Integer.parseInt(SPRITE_DATA.getString((myEntity.getEntityPlacement().getTypeString() + "_" + myEntity.getEntityDirection() + "_X_OFFSET").toUpperCase()));
    }
  }

  /**
   * Ensures a valid Y-Offset key is sent even when the entity doesn't have a current direction
   */
  public void getOffsetY() {
    if(myEntity.getEntityDirection() == ' ' || myEntity.getEntityDirection() == '\0') {
      myOffsetY = Integer.parseInt(SPRITE_DATA.getString((myEntity.getEntityPlacement().getTypeString() + "_R_Y_OFFSET").toUpperCase()));
    }
    else{
      myOffsetY = Integer.parseInt(SPRITE_DATA.getString((myEntity.getEntityPlacement().getTypeString() + "_" + myEntity.getEntityDirection() + "_Y_OFFSET").toUpperCase()));
    }
  }
}
