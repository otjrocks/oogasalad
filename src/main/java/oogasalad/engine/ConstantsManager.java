package oogasalad.engine;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A class to handle the using of constants from properties file
 *
 * @author Owen Jennings Language Manager, modified by Jessica Chen
 */
public class ConstantsManager {

  private static final String CONSTANTS_FILE_PATH = "oogasalad.constants.";

  /**
   * Get the message string from the constants resource config file for the provided key and the
   * provided constants folder
   *
   * @param filename The file name of the key you looking for
   * @param key      The key you are looking for
   * @return The message specified in the file for the provided key if it exists or a missing key
   * message
   * @throws ConstantsManagerException if either filename or key is missing
   */
  public static String getMessage(String filename, String key) throws ConstantsManagerException {
    try {
      ResourceBundle myConstants = getConstantsResourceBundle(filename);

      if (myConstants != null) {
        return myConstants.getString(key);
      } else {
        throw new ConstantsManagerException("The key is missing");
      }
    } catch (MissingResourceException e) {
      logMissingMessage("The constants file is missing.");

      throw new ConstantsManagerException("The constants file is missing", e);
    }
  }

  private static ResourceBundle getConstantsResourceBundle(String filename) {
    return ResourceBundle.getBundle(
        CONSTANTS_FILE_PATH + filename);
  }

  private static void logMissingMessage(String e) {
    if (LoggingManager.LOGGER != null) {
      LoggingManager.LOGGER.warn("Error loading a message: {}", e);
    }
  }

}
