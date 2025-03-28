package oogasalad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import oogasalad.GameMap.Entity;
import oogasalad.GameMap.InvalidPositionException;

/**
 * Use Case: MAPGENERATION-1: Load Map. This use case illustrates how the GameMap will be used to
 * load the map from a configuration file.
 * <p>
 * The file format is expected to contain entity information in the format: x,y where x and y
 * represent coordinates. This is an example using a text file, but something similar can be created
 * using our json file handler and FileHandler classes.
 *
 * @author Owen Jennings
 */
public class GameMapUseCase {

  public static void main(String[] args) {
    GameMap gameMap = new GameMap(200, 200); // A concrete/mock implementation of a GameMap
    // Specific concrete implementation of the GameMap Object
    // The map is of width 200, height 200
    String filePath = "map_config.txt";

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        Entity entity = new Entity(x, y);  // Create specific entity
        gameMap.addEntity(entity); // Add entity to map
      }
      System.out.println("Map loaded successfully.");
    } catch (IOException | InvalidPositionException e) {
      System.err.println("Error loading map: " + e.getMessage());
    }
  }
}
