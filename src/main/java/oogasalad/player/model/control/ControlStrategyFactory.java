package oogasalad.player.model.control;


import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import oogasalad.engine.input.GameInputManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.player.model.exceptions.ControlStrategyException;

public class ControlStrategyFactory {

  private static final String STRATEGY_PACKAGE
      = "oogasalad.player.model.control.";

  public static ControlStrategy createControlStrategy(
      GameInputManager input, EntityPlacement entityPlacement,
      GameMap gameMap)
      throws ControlStrategyException {
    String controlType = entityPlacement.getType().controlType();
    String className = STRATEGY_PACKAGE + truncate(controlType) + "ControlStrategy";

    try {
      Class<?> strategyClass = Class.forName(className);
      return instantiateStrategy(strategyClass, input, entityPlacement, gameMap);
    } catch (Exception e) {
      throw new ControlStrategyException(
          "Failed to create strategy for control type: " + controlType + " " + className, e);
    }
  }

  private static ControlStrategy instantiateStrategy(Class<?> strategyClass, GameInputManager input,
      EntityPlacement placement,
      GameMap gameMap)
      throws ControlStrategyException {
    try {
      for (Constructor<?> constructor : strategyClass.getConstructors()) {
        if (isPublicConstructor(constructor)) {
          ControlStrategy strategy = tryInstantiateStrategy(constructor, input, gameMap, placement);
          if (strategy != null) {
            return strategy;
          }
        }
      }
    } catch (Exception e) {
      throw new ControlStrategyException("Failed to instantiate strategy", e);
    }

    throw new ControlStrategyException(
        "No valid constructor found for: " + strategyClass.getName());
  }

  private static ControlStrategy tryInstantiateStrategy(Constructor<?> constructor,
      GameInputManager input,
      GameMap gameMap,
      EntityPlacement placement)
      throws ControlStrategyException {
    try {
      // bfs uses 2, keyboard uses 3, basic uses 0
      if (matchesZeroArgConstructor(constructor)) {
        return (ControlStrategy) constructor.newInstance(
        );
      }

      if (matchesTwoArgConstructor(constructor)) {
        return (ControlStrategy) constructor.newInstance(
            gameMap,
            placement
        );
      }

      if (matchesThreeArgConstructor(constructor)) {
        return (ControlStrategy) constructor.newInstance(
            input,
            gameMap,
            placement
        );
      }

      return null;
    } catch (Exception e) {
      throw new ControlStrategyException("Failed to instantiate strategy", e);
    }
  }


  private static boolean matchesZeroArgConstructor(Constructor<?> constructor) {
    Class<?>[] paramTypes = constructor.getParameterTypes();
    return paramTypes.length == 0;
  }

  private static boolean matchesTwoArgConstructor(Constructor<?> constructor) {
    Class<?>[] paramTypes = constructor.getParameterTypes();
    return paramTypes.length == 2 &&
        paramTypes[0].equals(GameMap.class) &&
        paramTypes[1].equals(EntityPlacement.class);
  }

  private static boolean matchesThreeArgConstructor(Constructor<?> constructor) {
    Class<?>[] paramTypes = constructor.getParameterTypes();
    return paramTypes.length == 3 &&
        paramTypes[0].equals(GameInputManager.class) &&
        paramTypes[1].equals(GameMap.class) &&
        paramTypes[2].equals(EntityPlacement.class);
  }

  private static boolean isPublicConstructor(Constructor<?> constructor) {
    return Modifier.isPublic(constructor.getModifiers());
  }

  private static String truncate(String input) {
    // split by camelCase, snake_case, or spaces
    String[] parts = input.split("(?=[A-Z])|_|\\s+");
    for (String part : parts) {
      if (!part.isEmpty()) {
        // get only the first part and capitalize it correctly
        return part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
      }
    }
    return ""; // return empty string if no valid parts found
  }


}
