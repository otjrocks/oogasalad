package oogasalad.engine.exceptions;

/**
 * An exception thrown by the Game Map when a user requests an entity that is not found on the map.
 *
 * @author Owen Jennings
 */
public class EntityNotFoundException extends Exception {

  /**
   * Create this exception with an error message.
   *
   * @param message The message you wish to provide.
   */
  public EntityNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code EntityNotFoundException} with the specified detail message and cause.
   * This allows the chaining of exceptions, which can help preserve the original stack trace of an
   * underlying exception.
   *
   * @param message the detail message providing additional context about the error.
   * @param cause   the underlying cause of the exception, which can be another exception that
   *                triggered this one.
   */
  public EntityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
