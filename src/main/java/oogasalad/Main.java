package oogasalad;


import com.fasterxml.jackson.databind.ObjectMapper;
import oogasalad.engine.config.ConfigModel;
import java.io.File;
import java.io.IOException;
import oogasalad.engine.config.ConfigParseException;
import oogasalad.engine.config.ConfigParser;
import oogasalad.engine.config.JsonConfigParser;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main {
    /**
     * A method to test (and a joke :).
     */
    public double getVersion () {
        return 0.001;
    }

    /**
     * Start of the program.
     */
    public static void main (String[] args) {
      ConfigParser parser = new JsonConfigParser();
      ConfigModel config = null;
      try {
        config = parser.loadFromFile("data/basic.json");
      } catch (ConfigParseException e) {
        System.out.println(e.getMessage());
      }

      System.out.println(config.getEntityConfigs().getFirst().getType());
    }
}
