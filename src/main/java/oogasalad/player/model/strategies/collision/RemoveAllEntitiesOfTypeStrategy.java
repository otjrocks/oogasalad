package oogasalad.player.model.strategies.collision;

import java.util.Iterator;
import oogasalad.engine.records.CollisionContextRecord;
import oogasalad.player.model.Entity;

/**
 * A collision strategy that removes all the entities of the provided type from the game map.
 *
 * @author Owen Jennings
 */
public class RemoveAllEntitiesOfTypeStrategy implements CollisionStrategyInterface {

  private final String myTargetType;

  /**
   * Initialize the strategy with your target type.
   *
   * @param targetType The type of entity you want to remove in this strategy.
   */
  public RemoveAllEntitiesOfTypeStrategy(String targetType) {
    myTargetType = targetType;
  }

  @Override
  public void handleCollision(CollisionContextRecord collisionContext) {
    Iterator<Entity> iterator = collisionContext.gameMap().iterator();
    while (iterator.hasNext()) {
      Entity entity = iterator.next();
      if (entity.getEntityPlacement().getType().type().equals(myTargetType)) {
        try {
          iterator.remove(); // remove entity concurrently from list safely.
        } catch (IllegalStateException e) {
          throw new RuntimeException("Failed to remove entity using iterator", e);
        }
      }
    }

  }
}
