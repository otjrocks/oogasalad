package oogasalad.player.model.api;


import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.ModeConfigRecord;
import oogasalad.engine.records.config.model.controlConfig.ConditionalControlConfigRecord;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.records.config.model.controlConfig.TargetControlConfigRecord;
import oogasalad.engine.records.config.model.controlConfig.targetStrategy.TargetCalculationConfigInterface;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.exceptions.TargetStrategyException;
import oogasalad.player.model.strategies.control.targetcalculation.TargetStrategyInterface;

/**
 * A factory design pattern used to create various target strategies.
 *
 * @author Jessica Chen
 */
public class TargetStrategyFactory {

  private static String STRATEGY_PACKAGE = "oogasalad.player.model.strategies.control.targetcalculation."; // Keep field as global and non-final for testing purposes.

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
      GameMapInterface gameMap)
      throws TargetStrategyException {

    String mode = placement.getMode();
    ModeConfigRecord modeConfig = placement.getType().modes().get(mode);
    ControlConfigInterface config = modeConfig.controlConfig();
    TargetCalculationConfigInterface targetCalculationConfig = getTargetCalculationConfigInterface(
        config);

    String className =
        STRATEGY_PACKAGE + targetCalculationConfig.getClass().getSimpleName()
            .replace("ConfigRecord", "Strategy");

    Map<String, Object> argsMap = recordToMap(targetCalculationConfig);

    try {
      Class<?> strategyClass = Class.forName(className);
      return instantiateStrategy(strategyClass, argsMap, placement, gameMap);
    } catch (Exception e) {
      throw new TargetStrategyException(
          "Failed to instantiate strategy for config: " + config.getClass(), e);
    }

  }

  private static TargetCalculationConfigInterface getTargetCalculationConfigInterface(
      ControlConfigInterface config) {

    for (RecordComponent component : config.getClass().getRecordComponents()) {
      if (TargetCalculationConfigInterface.class.isAssignableFrom(component.getType())) {
        try {
          Object value = component.getAccessor().invoke(config);
          if (value != null) {
            return (TargetCalculationConfigInterface) value;
          }
        } catch (Exception e) {
          throw new TargetStrategyException(
              "Failed to access field " + component.getName() + " in " + config.getClass()
                  .getSimpleName(), e);
        }
      }
    }

    throw new TargetStrategyException(
        "No field of type TargetCalculationConfigInterface found in config: "
            + config.getClass().getSimpleName());
  }


  private static TargetStrategyInterface instantiateStrategy(Class<?> strategyClass,
      Map<String, Object> targetCalculationConfig,
      EntityPlacement placement,
      GameMapInterface gameMap)
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

  private static Map<String, Object> recordToMap(TargetCalculationConfigInterface record) {
    return Arrays.stream(record.getClass().getRecordComponents())
        .collect(Collectors.toMap(
            RecordComponent::getName,
            component -> extractFieldValue(component, record)
        ));
  }

  private static Object extractFieldValue(RecordComponent component,
      TargetCalculationConfigInterface event) {
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
      GameMapInterface gameMap, Map<String, Object> targetCalculationConfig,
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
        paramTypes[0].equals(GameMapInterface.class) &&
        paramTypes[1].equals(Map.class);
  }

  private static boolean matchesThreeArgConstructor(Constructor<?> constructor) {
    Class<?>[] paramTypes = constructor.getParameterTypes();
    return paramTypes.length == 3 &&
        paramTypes[0].equals(GameMapInterface.class) &&
        paramTypes[1].equals(Map.class) &&
        paramTypes[2].equals(String.class);
  }

}
