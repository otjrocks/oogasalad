package oogasalad.player.model.api;


import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import oogasalad.player.model.exceptions.PathFindingStrategyException;
import oogasalad.player.model.strategies.control.pathfinding.PathFindingStrategyInterface;

/**
 * Factory class for creating instances of {@link PathFindingStrategyInterface}. This class dynamically loads
 * and instantiates pathfinding strategy classes based on their names. The strategy classes must be
 * located in the package {@code oogasalad.player.model.control.pathfinding} and follow the naming
 * convention of appending "PathFindingStrategy" to the strategy name.
 */
public class PathFindingStrategyFactory {

  private static final String STRATEGY_PACKAGE
      = "oogasalad.player.model.strategies.control.pathfinding.";

  /**
   * Creates an instance of a PathFindingStrategy based on the provided strategy name.
   *
   * @param pathFindingStrategy the name of the pathfinding strategy to create
   * @return an instance of the specified PathFindingStrategy
   * @throws PathFindingStrategyException if the strategy class cannot be found or instantiated
   */
  public static PathFindingStrategyInterface createPathFindingStrategy(String pathFindingStrategy)
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

  private static PathFindingStrategyInterface instantiateStrategy(Class<?> strategyClass)
      throws PathFindingStrategyException {
    try {
      for (Constructor<?> constructor : strategyClass.getConstructors()) {
        if (isPublicConstructor(constructor)) {
          return (PathFindingStrategyInterface) constructor.newInstance();
        }
      }
    } catch (Exception e) {
      throw new PathFindingStrategyException("Failed to instantiate strategy", e);
    }

    throw new PathFindingStrategyException(
        "No valid constructor found for: " + strategyClass.getName());
  }

  private static boolean isPublicConstructor(Constructor<?> constructor) {
    return Modifier.isPublic(constructor.getModifiers());
  }

}
