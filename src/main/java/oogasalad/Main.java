package oogasalad;


import oogasalad.engine.LoggingManager;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.api.ConfigParser;
import oogasalad.engine.config.JsonConfigParser;

/**
 * Feel free to completely change this code or delete it entirely.
 */
public class Main {

  /**
   * A method to test (and a joke :).
   */
  public double getVersion() {
    return 0.001;
  }

  /**
   * Start of the program.
   */
  public static void main(String[] args) {
    LoggingManager.printStartInfo();
    ConfigParser parser = new JsonConfigParser();
    ConfigModel config = null;
    try {
      config = parser.loadFromFile("data/basic.json");
    } catch (ConfigException e) {
      LoggingManager.LOGGER.error("Error loading the basic config file, {}", e.getMessage());
    }
    LoggingManager.LOGGER.info("Config file read first entity as: {}",
        config.getEntityConfigs().getFirst().getType());
  }
}
