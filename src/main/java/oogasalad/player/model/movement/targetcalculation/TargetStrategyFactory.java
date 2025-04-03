package oogasalad.player.model.movement.targetcalculation;

import java.util.Map;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.player.model.exceptions.TargetStrategyException;

/**
 * A factory design pattern used to create various target strategies.
 *
 * @author Jessica Chen
 */
public class TargetStrategyFactory {

  public static TargetStrategy createTargetStrategy(EntityPlacement placement,
      GameMap gameMap)
      throws TargetStrategyException {
    String strategy = placement.getType().getControlType().toLowerCase();
    return switch (strategy) {
      case "targetentity" -> createTargetTypeStrategy(placement, gameMap);
      case "targetaheadofentity" -> createTargetTypeStrategy(placement, gameMap);
      default -> throw new TargetStrategyException("Unknown target strategy");
    };
  }

  private static TargetStrategy createTargetTypeStrategy(EntityPlacement placement,
      GameMap gameMap) {
    Map<String, Object> strategyConfig = placement.getType().getStrategyConfig();
    String targetType = validateAndGetTargetType(strategyConfig);
    int tilesAhead = validateAndGetTilesAhead(strategyConfig);
    return new TargetTypeStrategy(gameMap, targetType, tilesAhead);
  }

  private static String validateAndGetTargetType(Map<String, Object> strategyConfig) {
    if (!strategyConfig.containsKey("targetType")) {
      throw new TargetStrategyException("Target type is required");
    }

    return strategyConfig.get("targetType").toString();
  }

  private static int validateAndGetTilesAhead(Map<String, Object> strategyConfig) {
    if (strategyConfig.containsKey("tilesAhead")) {
      try {
        return Integer.parseInt(strategyConfig.get("tilesAhead").toString());
      } catch (NumberFormatException e) {
        throw new TargetStrategyException("tilesAhead must be an integer", e);
      }
    }

    // we can log this, but may be annoying
    return 0;
  }

}
