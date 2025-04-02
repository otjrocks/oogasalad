package oogasalad.player.view;

import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameMap;

/**
 * A view used to display a specific Entity from the {@code Entity} model API.
 *
 * @author Owen Jennings
 */
public class EntityView extends ImageView {

  private final EntityPlacement myPlacement;

  /**
   * Create an Entity view using the entity data provided.
   *
   * @param gameMap    The game map being used for this entity view.
   * @param entityPlacement The entity data used to initialize the view.
   */
  public EntityView(GameMap gameMap, EntityPlacement entityPlacement) {
    super();
    myPlacement = entityPlacement;
    this.setFitWidth((double) GameView.WIDTH / gameMap.getWidth() - 5);
    this.setFitHeight((double) GameView.HEIGHT / gameMap.getHeight() - 5);
    initializeView();
  }

  private void initializeView() {
    this.setImage(new Image(
        Objects.requireNonNull(
            this.getClass().getClassLoader().getResourceAsStream(myPlacement.getType().getModes().get("Default").getImagePath()))));
  }
}
