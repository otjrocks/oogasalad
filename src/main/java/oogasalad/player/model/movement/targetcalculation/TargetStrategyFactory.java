package oogasalad.player.model.movement.targetcalculation;

import java.util.Map;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.exceptions.TargetStrategyException;

/**
 * A factory design pattern used to create various target strategies.
 *
 * @author Jessica Chen
 */
public class TargetStrategyFactory {

  /**
   * Factory method to create a TargetStrategy based on the control type of the given
   * EntityPlacement.
   *
   * @param placement the EntityPlacement object containing information about the entity and its
   *                  control type
   * @param gameMap   the GameMap object representing the current state of the game
   * @return a TargetStrategy instance corresponding to the control type of the entity
   * @throws TargetStrategyException if the control type of the entity is unknown or unsupported
   */
  public static TargetStrategy createTargetStrategy(EntityPlacement placement,
      GameMap gameMap)
      throws TargetStrategyException {
    String strategy = placement.getType().getControlType().toLowerCase();
    return switch (strategy) {
      case "targetentity" -> new TargetEntityStrategy(gameMap, placement.getType().getStrategyConfig());
      case "targetaheadofentity" -> new TargetAheadOfEntityStrategy(gameMap, placement.getType().getStrategyConfig());
      default -> throw new TargetStrategyException("Unknown target strategy");
    };
  }


}
