package oogasalad.player.model.movement.targetcalculation;


import java.util.Iterator;
import java.util.Optional;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;

class TargetStrategyHelperMethods {

  static Optional<Entity> findFirstEntityOfType(GameMap gameMap, String targetType) {
    Iterator<Entity> iterator = gameMap.iterator();
    while (iterator.hasNext()) {
      Entity entity = iterator.next();
      if (entity.getEntityPlacement().getTypeString()
          .equalsIgnoreCase(targetType)) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }
}
