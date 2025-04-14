package oogasalad.player.model.control.pathfinding;


import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import oogasalad.player.model.exceptions.PathFindingStrategyException;

public class PathFindingStrategyFactory {

  private static final String STRATEGY_PACKAGE
      = "oogasalad.player.model.control.pathfinding.";

  public static PathFindingStrategy createPathFindingStrategy(String pathFindingStrategy)
      throws PathFindingStrategyException {

    String className =
        STRATEGY_PACKAGE + pathFindingStrategy + "PathFindingStrategy";

    try {
      Class<?> strategyClass = Class.forName(className);
      return instantiateStrategy(strategyClass);
    } catch (Exception e) {
      throw new PathFindingStrategyException(
          "Failed to instantiate strategy for: " + pathFindingStrategy, e);
    }

  }

  private static PathFindingStrategy instantiateStrategy(Class<?> strategyClass)
      throws PathFindingStrategyException {
    try {
      for (Constructor<?> constructor : strategyClass.getConstructors()) {
        if (isPublicConstructor(constructor)) {
          return (PathFindingStrategy) constructor.newInstance();
        }
      }
    } catch (Exception e) {
      throw new PathFindingStrategyException("Failed to instantiate strategy", e);
    }

    throw new PathFindingStrategyException("No valid constructor found for: " + strategyClass.getName());
  }

  private static boolean isPublicConstructor(Constructor<?> constructor) {
    return Modifier.isPublic(constructor.getModifiers());
  }

}
