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
      System.out.println(e.getMessage());
    }
    System.out.println(config.getEntityConfigs().getFirst().getType());
  }
}
