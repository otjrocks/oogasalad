package oogasalad.engine.model.exceptions;

/**
 * An exception thrown when no strategies are found or some other error occurs when loading
 * strategies.
 *
 * @author Austin Huang
 */
public class LoadStrategyException extends RuntimeException {

  /**
   * Create this exception with an error message.
   *
   * @param message The message to provide.
   */
  public LoadStrategyException(String message) {
    super(message);
  }
}
