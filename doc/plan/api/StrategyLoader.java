package oogasalad;

import java.util.List;

/**
 * The {@code StrategyLoader} interface provides methods for retrieving available strategies
 * used in the game. Strategies define various behaviors such as movement, collision handling,
 * edge interactions, effects, and game outcomes.
 *
 * <p>Implementations of this interface are responsible for dynamically loading the available
 * strategy classes that adhere to the respective strategy interfaces.
 *
 * @author Austin Huang
 * @see MovementStrategy
 * @see CollisionStrategy
 * @see EdgeStrategy
 * @see EffectStrategy
 * @see GameOutcomeStrategy
 */
public interface StrategyLoader {
  // ChatGPT assisted in writing the methods of this interface

  /**
   * Retrieves a list of available movement strategy classes.
   *
   * @return a list of classes implementing the {@code MovementStrategy} interface.
   * @throws LoadStrategyException if movement strategies cannot be loaded.
   */
  List<Class<? extends MovementStrategy>> getAvailableMovementStrategies() throws LoadStrategyException;

  /**
   * Retrieves a list of available collision strategy classes.
   *
   * @return a list of classes implementing the {@code CollisionStrategy} interface.
   * @throws LoadStrategyException if collision strategies cannot be loaded.
   */
  List<Class<? extends CollisionStrategy>> getAvailableCollisionStrategies() throws LoadStrategyException;

  /**
   * Retrieves a list of available edge strategy classes.
   *
   * @return a list of classes implementing the {@code EdgeStrategy} interface.
   * @throws LoadStrategyException if edge strategies cannot be loaded.
   */
  List<Class<? extends EdgeStrategy>> getAvailableEdgeStrategies() throws LoadStrategyException;

  /**
   * Retrieves a list of available effect strategy classes.
   *
   * @return a list of classes implementing the {@code EffectStrategy} interface.
   * @throws LoadStrategyException if effect strategies cannot be loaded.
   */
  List<Class<? extends EffectStrategy>> getAvailableEffectStrategies() throws LoadStrategyException;

  /**
   * Retrieves a list of available game outcome strategy classes.
   *
   * @return a list of classes implementing the {@code GameOutcomeStrategy} interface.
   * @throws LoadStrategyException if game outcome strategies cannot be loaded.
   */
  List<Class<? extends GameOutcomeStrategy>> getAvailableGameOutcomeStrategies() throws LoadStrategyException;

  /*
   * Custom exception class to handle strategy loading errors.
   */
  class LoadStrategyException extends Exception {
    /**
     * Constructs a new {@code LoadStrategyException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception.
     */
    public LoadStrategyException(String message) {
      super(message);
    }

    /**
     * Constructs a new {@code LoadStrategyException} with the specified detail message
     * and cause.
     *
     * @param message the detail message explaining the reason for the exception.
     * @param cause the cause of the exception.
     */
    public LoadStrategyException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
