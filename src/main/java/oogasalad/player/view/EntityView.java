package oogasalad.player.view;

import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import oogasalad.engine.model.EntityData;
import oogasalad.engine.model.GameMap;

/**
 * A view used to display a specific Entity from the {@code Entity} model API.
 *
 * @author Owen Jennings
 */
public class EntityView extends ImageView {

  private final EntityData myEntityData;

  /**
   * Create an Entity view using the entity data provided.
   *
   * @param gameMap    The game map being used for this entity view.
   * @param entityData The entity data used to initialize the view.
   */
  public EntityView(GameMap gameMap, EntityData entityData) {
    myEntityData = entityData;
    this.setFitWidth((double) GameView.WIDTH / gameMap.getWidth());
    this.setFitHeight((double) GameView.HEIGHT / gameMap.getHeight());
    initializeView();
  }

  private void initializeView() {
    this.setImage(new Image(
        Objects.requireNonNull(
            this.getClass().getClassLoader().getResourceAsStream(myEntityData.getImagePath()))));
  }
}
