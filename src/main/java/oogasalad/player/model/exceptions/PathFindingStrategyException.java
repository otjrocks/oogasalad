package oogasalad.player.model.exceptions;

/**
 * An exception thrown by the PathFindingStrategy or PathFindingStrategyFactory when a user an
 * invalid PathFindingStrategy
 *
 * @author Jessica Chen
 */
public class PathFindingStrategyException extends RuntimeException {

  /**
   * Create this exception with an error message.
   *
   * @param message The message you wish to provide.
   */
  public PathFindingStrategyException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code PathFindingStrategyException} with the specified detail message and
   * cause. This allows the chaining of exceptions, which can help preserve the original stack trace
   * of an underlying exception.
   *
   * @param message the detail message providing additional context about the error.
   * @param cause   the underlying cause of the exception, which can be another exception that
   *                triggered this one.
   */
  public PathFindingStrategyException(String message, Throwable cause) {
    super(message, cause);
  }
}
