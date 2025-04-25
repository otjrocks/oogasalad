package oogasalad.player.model.exceptions;

/**
 * An exception thrown by the TargetStrategy or TargetStrategyFactory when a user an invalid
 * TargetStrategy
 *
 * @author Jessica Chen
 */
public class TargetStrategyException extends RuntimeException {


  /**
   * Create this exception with an error message.
   *
   * @param message The message you wish to provide.
   */
  public TargetStrategyException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code TargetStrategyException} with the specified detail message and cause.
   * This allows the chaining of exceptions, which can help preserve the original stack trace of an
   * underlying exception.
   *
   * @param message the detail message providing additional context about the error.
   * @param cause   the underlying cause of the exception, which can be another exception that
   *                triggered this one.
   */
  public TargetStrategyException(String message, Throwable cause) {
    super(message, cause);
  }
}
