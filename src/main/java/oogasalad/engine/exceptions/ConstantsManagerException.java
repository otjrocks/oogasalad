package oogasalad.engine.exceptions;

/**
 * Exception thrown to indicate an issue with configuration processing within {@code ConfigParser}
 * or {@code ConfigSaver}.
 *
 * @author Will He
 */
public class ConstantsManagerException extends Exception {

  /**
   * Constructs a new ConstantsManagerException with the specified detail message.
   *
   * @param message the detail message explaining the reason for the exception
   */
  public ConstantsManagerException(String message) {
    super(message);
  }

  /**
   * Constructs a new ConstantsManagerException with the specified detail message and cause.
   *
   * @param message the detail message explaining the reason for the exception
   * @param cause   the cause of the exception (a throwable that caused this exception to be
   *                thrown)
   */
  public ConstantsManagerException(String message, Throwable cause) {
    super(message, cause);
  }
}
