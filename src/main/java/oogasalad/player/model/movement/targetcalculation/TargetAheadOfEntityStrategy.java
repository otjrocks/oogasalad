package oogasalad.player.model.movement.targetcalculation;

import java.util.Map;
import java.util.Optional;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.entity.Entity;
import oogasalad.player.model.exceptions.TargetStrategyException;

public class TargetAheadOfEntityStrategy implements TargetStrategy{

  private final GameMap myGameMap;
  private final String myTargetType;
  private final int myTilesAhead;

  public TargetAheadOfEntityStrategy(GameMap gameMap, Map<String, Object> strategyConfig) {
    myGameMap = gameMap;
    myTargetType = TargetStrategyHelperMethods.validateAndGetTargetType(strategyConfig);
    myTilesAhead = validateAndGetTilesAhead(strategyConfig);
  }

  @Override
  public int[] getTargetPosition() {
    Optional<Entity> entity = TargetStrategyHelperMethods.findFirstEntityOfType(myGameMap,
        myTargetType);

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

      case 'R':
        targetX += myTilesAhead;
        break;
      case 'D':
        targetY += myTilesAhead;
        break;
      case 'L':
        targetX -= myTilesAhead;
        break;
      default: // default up
        targetY -= myTilesAhead;
        break;
    }
    return new int[]{targetX, targetY};
  }

  private static int validateAndGetTilesAhead(Map<String, Object> strategyConfig) {
    if (strategyConfig.containsKey("tilesAhead") && strategyConfig.get("tilesAhead") != null) {
      try {
        return Integer.parseInt(strategyConfig.get("tilesAhead").toString());
      } catch (NumberFormatException e) {
        throw new TargetStrategyException("tilesAhead must be an integer", e);
      }
    }

    // can log a warning, but may be annoying
    return 0;
  }
}
