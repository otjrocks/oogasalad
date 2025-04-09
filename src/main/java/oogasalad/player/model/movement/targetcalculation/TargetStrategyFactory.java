package oogasalad.player.model.movement.targetcalculation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
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
      Class<?> strategyClass = Class.forName(className);
      return instantiateStrategy(strategyClass, placement, gameMap);
    } catch (Exception e) {
      throw new TargetStrategyException(
          "Failed to create strategy for control type: " + controlType, e);
    }
  }

  private static TargetStrategy instantiateStrategy(Class<?> strategyClass,
      EntityPlacement placement,
      GameMap gameMap)
      throws TargetStrategyException {
    try {
      for (Constructor<?> constructor : strategyClass.getConstructors()) {
        if (isPublicConstructor(constructor)) {
          TargetStrategy strategy = tryInstantiateStrategy(constructor, gameMap, placement);
          if (strategy != null) {
            return strategy;
          }
        }
      }
    } catch (Exception e) {
      throw new TargetStrategyException("Failed to instantiate strategy", e);
    }

    throw new TargetStrategyException("No valid constructor found for: " + strategyClass.getName());
  }

  private static boolean isPublicConstructor(Constructor<?> constructor) {
    return Modifier.isPublic(constructor.getModifiers());
  }

  private static TargetStrategy tryInstantiateStrategy(Constructor<?> constructor,
      GameMap gameMap,
      EntityPlacement placement)
      throws TargetStrategyException {
    try {
      if (matchesTwoArgConstructor(constructor)) {
        return (TargetStrategy) constructor.newInstance(
            gameMap,
            placement.getType().strategyConfig()
        );
      }

      if (matchesThreeArgConstructor(constructor)) {
        return (TargetStrategy) constructor.newInstance(
            gameMap,
            placement.getType().strategyConfig(),
            placement.getTypeString()
        );
      }

      return null;
    }
    catch (Exception e) {
      throw new TargetStrategyException("Failed to instantiate strategy", e);
    }
  }

  private static boolean matchesTwoArgConstructor(Constructor<?> constructor) {
    Class<?>[] paramTypes = constructor.getParameterTypes();
    return paramTypes.length == 2 &&
        paramTypes[0].equals(GameMap.class) &&
        paramTypes[1].equals(Map.class);
  }

  private static boolean matchesThreeArgConstructor(Constructor<?> constructor) {
    Class<?>[] paramTypes = constructor.getParameterTypes();
    return paramTypes.length == 3 &&
        paramTypes[0].equals(GameMap.class) &&
        paramTypes[1].equals(Map.class) &&
        paramTypes[2].equals(String.class);
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
