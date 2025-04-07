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

/**
 * A view used to display a specific Entity from the {@code Entity} model API.
 *
 * @author Owen Jennings
 */
public class EntityView extends ImageView {

  private ResourceBundle SPRITE_DATA = ResourceBundle.getBundle("oogasalad.sprite_data.sprites");

  private SpriteAnimationView mySprite;
  private EntityPlacement myEntityPlacement;
  private final int myTotalFrames;
  private final int myOffsetX;
  private final int myOffsetY;

  private static final Image SPRITE_SHEET = new Image(
          Objects.requireNonNull(EntityView.class.getClassLoader().getResourceAsStream("sprites/Pacman.png"))
  );

  /**
   * Create an Entity view using the entity data provided.
   *
   * @param gameMap    The game map being used for this entity view.
   * @param entityPlacement The entity data used to initialize the view.
   */
  public EntityView(GameMap gameMap, EntityPlacement entityPlacement, int totalFrames) {
    super(SPRITE_SHEET);
    myEntityPlacement = entityPlacement;
    myTotalFrames = totalFrames;
    myOffsetX = Integer.parseInt(SPRITE_DATA.getString((myEntityPlacement.getTypeString() + "_X_OFFSET").toUpperCase()));
    myOffsetY = Integer.parseInt(SPRITE_DATA.getString((myEntityPlacement.getTypeString() + "_Y_OFFSET").toUpperCase()));
    this.setFitWidth((double) GameView.WIDTH / gameMap.getWidth());
    this.setFitHeight((double) GameView.HEIGHT / gameMap.getHeight());
    setupAnimation();
  }

  private void setupAnimation() {
    int frameDimension = 32;
    int x = (myEntityPlacement.getCurrentFrame() % myTotalFrames) * frameDimension + myOffsetX;
    this.setViewport(new Rectangle2D(x, myOffsetY, frameDimension, frameDimension));
  }
}
