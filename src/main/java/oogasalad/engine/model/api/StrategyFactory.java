package oogasalad.engine.model.api;

import oogasalad.engine.LoggingManager;
import oogasalad.engine.model.strategies.collision.CollisionStrategy;
import oogasalad.engine.records.newconfig.model.collisionevent.CollisionEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;

/**
 * A factory design pattern to create strategies based on a provided event.
 */
public class StrategyFactory {

  private static final String STRATEGY_PACKAGE = "oogasalad.engine.model.strategies.collision";

  public static CollisionStrategy createCollisionStrategy(CollisionEvent collisionEvent) {
    try {
      String eventClassName = collisionEvent.getClass().getSimpleName(); // e.g., UpdateScoreEvent
      String strategyClassName = eventClassName.replace("Event", "Strategy"); // UpdateScoreStrategy
      Class<?> strategyClass = Class.forName(STRATEGY_PACKAGE + "." + strategyClassName);

      // Get parameters from event record (supports multiple parameters if needed)
      Object[] args = Arrays.stream(collisionEvent.getClass().getRecordComponents())
          .map(component -> extractFieldValue(component, collisionEvent))
          .toArray();

      Constructor<?> constructor = Arrays.stream(strategyClass.getConstructors())
          .filter(c -> c.getParameterCount() == args.length)
          .findFirst()
          .orElseThrow();

      return (CollisionStrategy) constructor.newInstance(args);
    } catch (Exception e) {
      LoggingManager.LOGGER.warn("Failed to create strategy for: {}",
          collisionEvent.getClass().getSimpleName(), e);
      return defaultStrategy();
    }
  }

  private static Object extractFieldValue(RecordComponent component, CollisionEvent event) {
    try {
      return component.getAccessor().invoke(event);
    } catch (Exception e) {
      LoggingManager.LOGGER.warn("Failed to extract field: {}", component.getName(), e);
      return null;
    }
  }

  private static CollisionStrategy defaultStrategy() {
    try {
      Class<?> defaultClass = Class.forName(STRATEGY_PACKAGE + ".ConsumeStrategy");
      return (CollisionStrategy) defaultClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException("Failed to create default strategy", e);
    }
  }
}
