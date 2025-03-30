package oogasalad.player.view;

import java.util.Objects;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import oogasalad.engine.model.EntityData;

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
   * @param entityData The entity data used to initialize the view.
   */
  public EntityView(EntityData entityData) {
    myEntityData = entityData;
    initializeView();
  }

  private void initializeView() {
    this.setX(myEntityData.getInitialX());
    this.setY(myEntityData.getInitialY());
    this.setImage(new Image(
        Objects.requireNonNull(
            this.getClass().getClassLoader().getResourceAsStream(myEntityData.getImagePath()))));
  }
}
