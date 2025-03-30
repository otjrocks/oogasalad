package oogasalad.engine.model.exceptions;

/**
 * An exception thrown when a user attempts to add an Entity to the Game map in an invalid
 * position.
 *
 * @author Owen Jennings
 */
public class InvalidPositionException extends Exception {

  /**
   * Create an exception with an error message.
   *
   * @param message The message you wish to provide.
   */
  public InvalidPositionException(String message) {
    super(message);
  }
}
