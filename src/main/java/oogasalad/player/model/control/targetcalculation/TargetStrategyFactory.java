package oogasalad.player.model.control.targetcalculation;


import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.controlConfig.ConditionalControlConfig;
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.controlConfig.TargetControlConfig;
import oogasalad.engine.model.controlConfig.targetStrategy.TargetCalculationConfig;
import oogasalad.player.model.exceptions.TargetStrategyException;

/**
 * A factory design pattern used to create various target strategies.
 *
 * @author Jessica Chen
 */
public class TargetStrategyFactory {

  private static String STRATEGY_PACKAGE = "oogasalad.player.model.control.targetcalculation."; // Keep field as global and non-final for testing purposes.

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
  public static TargetStrategyInterface createTargetStrategy(EntityPlacement placement,
      GameMap gameMap)
      throws TargetStrategyException {

    ControlConfig config = placement.getType().controlConfig();
    TargetCalculationConfig targetCalculationConfig;

    if (config instanceof TargetControlConfig targetConfig) {
      targetCalculationConfig = targetConfig.targetCalculationConfig();
    } else if (config instanceof ConditionalControlConfig conditionalConfig) {
      targetCalculationConfig = conditionalConfig.targetCalculationConfig();
    } else {
      throw new TargetStrategyException(
          "No TargetStrategy available for control config: " + config.getClass());
    }

    String className =
        STRATEGY_PACKAGE + targetCalculationConfig.getClass().getSimpleName()
            .replace("Config", "Strategy");

    Map<String, Object> argsMap = recordToMap(targetCalculationConfig);

    try {
      Class<?> strategyClass = Class.forName(className);
      return instantiateStrategy(strategyClass, argsMap, placement, gameMap);
    } catch (Exception e) {
      throw new TargetStrategyException(
          "Failed to instantiate strategy for config: " + config.getClass(), e);
    }

  }

  private static TargetStrategyInterface instantiateStrategy(Class<?> strategyClass,
      Map<String, Object> targetCalculationConfig,
      EntityPlacement placement,
      GameMap gameMap)
      throws TargetStrategyException {
    try {
      for (Constructor<?> constructor : strategyClass.getConstructors()) {
        if (isPublicConstructor(constructor)) {
          TargetStrategyInterface strategy = tryInstantiateStrategy(constructor, gameMap,
              targetCalculationConfig, placement);
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

  private static Map<String, Object> recordToMap(TargetCalculationConfig record) {
    return Arrays.stream(record.getClass().getRecordComponents())
        .collect(Collectors.toMap(
            RecordComponent::getName,
            component -> extractFieldValue(component, record)
        ));
  }

  private static Object extractFieldValue(RecordComponent component,
      TargetCalculationConfig event) {
    try {
      return component.getAccessor().invoke(event);
    } catch (Exception e) {
      LoggingManager.LOGGER.warn("Failed to extract field: {}", component.getName(), e);
      return null;
    }
  }

  private static boolean isPublicConstructor(Constructor<?> constructor) {
    return Modifier.isPublic(constructor.getModifiers());
  }

  private static TargetStrategyInterface tryInstantiateStrategy(Constructor<?> constructor,
      GameMap gameMap, Map<String, Object> targetCalculationConfig,
      EntityPlacement placement)
      throws TargetStrategyException {
    try {
      if (matchesTwoArgConstructor(constructor)) {
        return (TargetStrategyInterface) constructor.newInstance(
            gameMap,
            targetCalculationConfig
        );
      }

      if (matchesThreeArgConstructor(constructor)) {
        return (TargetStrategyInterface) constructor.newInstance(
            gameMap,
            targetCalculationConfig,
            placement.getTypeString()
        );
      }

      return null;
    } catch (Exception e) {
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

}
