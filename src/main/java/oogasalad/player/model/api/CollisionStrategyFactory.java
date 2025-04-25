package oogasalad.player.model.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import oogasalad.engine.records.config.model.CollisionEventInterface;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.model.strategies.collision.CollisionStrategyInterface;

/**
 * A factory design pattern to create collision strategies based on a provided event.
 *
 * @author Owen Jennings
 */
public class CollisionStrategyFactory {

  private static final String STRATEGY_PACKAGE = "oogasalad.player.model.strategies.collision";

  /**
   * Create a collision strategy based on the collision event record derived from the configuration
   * files.
   *
   * @param collisionEvent A collision event data storage object.
   * @return The collision strategy created from the collision event object.
   */
  // I used ChatGPT to assist in generating this method.
  public static CollisionStrategyInterface createCollisionStrategy(
      CollisionEventInterface collisionEvent) {
    try {
      String eventClassName = collisionEvent.getClass().getSimpleName();
      String strategyClassName = eventClassName.replace("CollisionEventRecord",
          "Strategy");
      Class<?> strategyClass = Class.forName(STRATEGY_PACKAGE + "." + strategyClassName);

      // Get parameters from event record (supports multiple parameters if needed)
      Object[] args = Arrays.stream(collisionEvent.getClass().getRecordComponents())
          .map(component -> extractFieldValue(component, collisionEvent))
          .toArray();

      Constructor<?> constructor = Arrays.stream(strategyClass.getConstructors())
          .filter(c -> c.getParameterCount() == args.length)
          .findFirst()
          .orElseThrow();

      return (CollisionStrategyInterface) constructor.newInstance(args);
    } catch (Exception e) {
      LoggingManager.LOGGER.warn("Failed to create strategy for: {}",
          collisionEvent.getClass().getSimpleName(), e);
      return defaultStrategy();
    }
  }

  private static Object extractFieldValue(RecordComponent component,
      CollisionEventInterface event) {
    try {
      return component.getAccessor().invoke(event);
    } catch (Exception e) {
      LoggingManager.LOGGER.warn("Failed to extract field: {}", component.getName(), e);
      return null;
    }
  }

  private static CollisionStrategyInterface defaultStrategy() {
    try {
      Class<?> defaultClass = Class.forName(STRATEGY_PACKAGE + ".ConsumeStrategy");
      return (CollisionStrategyInterface) defaultClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Failed to create default strategy", e);
    }
  }
}
