package oogasalad.player.model.movement.targetcalculation;

import java.lang.reflect.Constructor;
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

  private static final String STRATEGY_PACKAGE
      = "oogasalad.player.model.movement.targetcalculation.";

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
  public static TargetStrategy createTargetStrategy(EntityPlacement placement, GameMap gameMap)
      throws TargetStrategyException {
    String controlType = placement.getType().controlType();
    String className = STRATEGY_PACKAGE + capitalize(controlType) + "Strategy";

    try {
      Class<?> clazz = Class.forName(className);
      Constructor<?> constructor = clazz.getConstructor(GameMap.class, Map.class);
      return (TargetStrategy) constructor.newInstance(gameMap,
          placement.getType().strategyConfig());
    } catch (Exception e) {
      throw new TargetStrategyException(
          "Failed to create strategy for control type: " + controlType, e);
    }
  }

  // made by chatGPT
  private static String capitalize(String input) {
    String[] parts = input.split("(?=[A-Z])|_|\\s+"); // split camelCase, snake_case, or spaces
    StringBuilder sb = new StringBuilder();
    for (String part : parts) {
      if (!part.isEmpty()) {
        sb.append(part.substring(0, 1).toUpperCase());
        sb.append(part.substring(1).toLowerCase());
      }
    }
    return sb.toString();
  }
}
