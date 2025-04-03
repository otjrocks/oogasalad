package oogasalad.player.model.movement.targetcalculation;


import java.util.Iterator;
import java.util.Optional;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;

/**
 * @author Jessica Chen
 */
public class TargetTypeStrategy implements TargetStrategy {

  private final GameMap myGameMap;
  private final String myTargetType;
  private int myTilesAhead = 0;

  public TargetTypeStrategy(GameMap gameMap, String targetType, int tilesAhead) {
    myGameMap = gameMap;
    myTargetType = targetType;
    myTilesAhead = tilesAhead;
  }


  @Override
  public int[] getTargetPosition() {
    Optional<Entity> entity = returnFirstEntityOfType();

    // if no entity, no target so stay where you are
    // or random movement (design choice for later)
    return entity.map(value -> calcTargetPosition(value.getEntityDirection(),
        (int) value.getEntityPlacement().getX(),
        (int) value.getEntityPlacement().getY())).orElseGet(() -> new int[]{0, 0});

  }

  private int[] calcTargetPosition(char dir, int x, int y) {
    int targetX = x;
    int targetY = y;

    switch (dir) {
      case 'U':
        targetY -= myTilesAhead;
        break;
      case 'R':
        targetX += myTilesAhead;
        break;
      case 'D':
        targetY += myTilesAhead;
        break;
      case 'L':
        targetX -= myTilesAhead;
        break;
      default:
        break;
    }
    return new int[]{targetX, targetY};
  }

  private Optional<Entity> returnFirstEntityOfType() {
    Iterator<Entity> iterator = myGameMap.iterator();
    while (iterator.hasNext()) {
      Entity entity = iterator.next();
      if (entity.getEntityPlacement().getTypeString()
          .equalsIgnoreCase(myTargetType)) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }
}
