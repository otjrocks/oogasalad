package oogasalad.engine.config;

/**
 * Exception thrown to indicate an issue with configuration processing within {@code ConfigParser}
 * or {@code ConfigSaver}.
 *
 * @author Will He
 */
public class ConfigException extends Exception {

  /**
   * Constructs a new ConfigException with the specified detail message.
   *
   * @param message the detail message explaining the reason for the exception
   */
  public ConfigException(String message) {
    super(message);
  }

  /**
   * Constructs a new ConfigException with the specified detail message and cause.
   *
   * @param message the detail message explaining the reason for the exception
   * @param cause   the cause of the exception (a throwable that caused this exception to be
   *                thrown)
   */
  public ConfigException(String message, Throwable cause) {
    super(message, cause);
  }
}
