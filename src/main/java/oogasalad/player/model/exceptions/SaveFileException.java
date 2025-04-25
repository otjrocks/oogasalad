package oogasalad.player.model.exceptions;

/**
 * An exception thrown by the any function that involves saving information to a save
 * config file.
 *
 * @author Jessica Chen
 */
public class SaveFileException extends RuntimeException {

  /**
   * Create this exception with an error message.
   *
   * @param message The message you wish to provide.
   */
  public SaveFileException(String message) {
    super(message);
  }
}
