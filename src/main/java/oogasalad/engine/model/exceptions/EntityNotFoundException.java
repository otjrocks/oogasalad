package oogasalad.engine.model.exceptions;

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
}
