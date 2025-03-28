package oogasalad;


import com.fasterxml.jackson.databind.ObjectMapper;
import oogasalad.engine.config.ConfigModel;
import java.io.File;
import java.io.IOException;

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
        ObjectMapper mapper = new ObjectMapper();
      ConfigModel config = null;
      try {
        config = mapper.readValue(new File("data/basic.json"), ConfigModel.class);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      System.out.println(config.entityConfigs.get(0).type);
    }
}
