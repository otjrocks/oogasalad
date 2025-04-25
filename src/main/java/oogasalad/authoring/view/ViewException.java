package oogasalad.authoring.view;


/**
 * An exception thrown by the View Class when a user
 *
 * @author Jessica Chen
 */
public class ViewException extends RuntimeException {

  /**
   * Create this exception with an error message.
   *
   * @param message The message you wish to provide.
   */
  public ViewException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code ViewException} with the specified detail message and cause. This allows
   * the chaining of exceptions, which can help preserve the original stack trace of an underlying
   * exception.
   *
   * @param message the detail message providing additional context about the error.
   * @param cause   the underlying cause of the exception, which can be another exception that
   *                triggered this one.
   */
  public ViewException(String message, Throwable cause) {
    super(message, cause);
  }
}