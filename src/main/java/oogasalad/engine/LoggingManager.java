package oogasalad.engine;

import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class to handle all the Log4j2 logging for this program.
 *
 * @author Owen Jennings
 */
public class LoggingManager {

  private static final int LENGTH_OF_DIVIDER = 50;
  public static final Logger LOGGER = LogManager.getLogger(); // The Logger for this program

  /**
   * Print the starting information of the program. This should be called whenever the program is
   * restarted.
   */
  public static void printStartInfo() {
    if (LOGGER != null) {
      LOGGER.info("\n");
      logDividerLine();
      LOGGER.info("Program started at {}", new Date());
      logDividerLine();
    }
  }

  private static void logDividerLine() {
    LOGGER.info("-".repeat(LENGTH_OF_DIVIDER));
  }
}
