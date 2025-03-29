package oogasalad.engine;

import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import oogasalad.engine.utility.FileUtility;

/**
 * A class to handle the selection and handling of language messages from a language properties
 * folder.
 *
 * @author Owen Jennings
 */
public class LanguageManager {

  private static final String LANGUAGE_FILE_PATH = "oogasalad.languages.";
  private static final String LANGUAGE_DIRECTORY_PATH = "src/main/resources/oogasalad/languages/";
  private static final String DEFAULT_LANGUAGE = "English";

  private static String myCurrentLanguage = DEFAULT_LANGUAGE;
  private static ResourceBundle myMessages = getLanguageResourceBundle();

  /**
   * Get the message string from the language resource config file for the provided key. This method
   * will use the current language for the requested message.
   *
   * @param key The key you are looking for
   * @return The message specified in the language file for the provided key if it exists or a
   * missing key message
   */
  public static String getMessage(String key) {
    try {
      if (myMessages != null) {
        return myMessages.getString(key);
      } else {
        logMissingMessage("The language file is missing.");
        return "ERROR: MISSING KEY";
      }
    } catch (MissingResourceException e) {
      // queries key does not exist in language file or trouble finding messages file
      // return a default string
      try {
        // try displaying to user that key is missing
        // in their preferred language, fallback to english
        return myMessages.getString("MISSING_KEY");
      } catch (MissingResourceException e1) {
        logMissingMessage(e.getMessage());
        return "ERROR: MISSING KEY";
      }
    }

  }

  /**
   * Get the display name of the current language.
   *
   * @return The display name of the current language
   */
  public static String getLanguage() {
    return myCurrentLanguage;
  }

  /**
   * Get a list of all available language files.
   *
   * @return The display name of all the possible languages
   */
  public static List<String> getAvailableLanguages() {
    return FileUtility.getFileNamesInDirectory(LANGUAGE_DIRECTORY_PATH, ".properties");
  }

  /**
   * Set the language for the program.
   *
   * @param language The string display name of the language you which to switch to
   */
  public static void setLanguage(String language) {
    myCurrentLanguage = language;
    myMessages = getLanguageResourceBundle();
  }

  private static ResourceBundle getLanguageResourceBundle() {
    return ResourceBundle.getBundle(
        LANGUAGE_FILE_PATH + myCurrentLanguage);
  }

  private static void logMissingMessage(String e) {
    if (LoggingManager.LOGGER != null) {
      LoggingManager.LOGGER.warn("Error loading a message: {}", e);
    }
  }

}
