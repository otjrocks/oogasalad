package oogasalad.engine.model.strategies;

import java.util.List;
import oogasalad.engine.model.exceptions.LoadStrategyException;

/**
 * A class to handle the loading of strategy patterns for entities.
 *
 * @author Austin Huang
 */
public class StrategyLoader {

  /**
   * Retrieves a list of available movement strategy classes.
   *
   * @return a list of classes implementing the {@code MovementStrategy} interface.
   * @throws LoadStrategyException if movement strategies cannot be loaded.
   */
  List<Class<? extends MovementStrategy>> getAvailableMovementStrategies()
      throws LoadStrategyException {
    return List.of();
  }

  /**
   * Retrieves a list of available collision strategy classes.
   *
   * @return a list of classes implementing the {@code CollisionStrategy} interface.
   * @throws LoadStrategyException if collision strategies cannot be loaded.
   */
  List<Class<? extends CollisionStrategy>> getAvailableCollisionStrategies()
      throws LoadStrategyException {
    return List.of();
  }

  /**
   * Retrieves a list of available edge strategy classes.
   *
   * @return a list of classes implementing the {@code EdgeStrategy} interface.
   * @throws LoadStrategyException if edge strategies cannot be loaded.
   */
  List<Class<? extends EdgeStrategy>> getAvailableEdgeStrategies() throws LoadStrategyException {
    return List.of();
  }

  /**
   * Retrieves a list of available effect strategy classes.
   *
   * @return a list of classes implementing the {@code EffectStrategy} interface.
   * @throws LoadStrategyException if effect strategies cannot be loaded.
   */
  List<Class<? extends EffectStrategy>> getAvailableEffectStrategies()
      throws LoadStrategyException {
    return List.of();
  }

  /**
   * Retrieves a list of available game outcome strategy classes.
   *
   * @return a list of classes implementing the {@code GameOutcomeStrategy} interface.
   * @throws LoadStrategyException if game outcome strategies cannot be loaded.
   */
  List<Class<? extends GameOutcomeStrategy>> getAvailableGameOutcomeStrategies()
      throws LoadStrategyException {
    return List.of();
  }
}



