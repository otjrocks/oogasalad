package oogasalad.player.model.api;

import java.io.IOException;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.records.config.ConfigModelRecord;
import oogasalad.player.model.GameState;

/**
 * Loads in a new game state given a config file
 *
 * @author Luke Fu
 */
public class GameStateFactory {

  /**
   * Creates new game state from a path to the game folder.
   */
  public static GameState createFromConfig(String gameFolderPath) {
    JsonConfigParser parser = new JsonConfigParser();
    try {
      ConfigModelRecord configModel = parser.loadFromFile(gameFolderPath + "/gameConfig.json");
      return new GameState(configModel.settings());
    } catch (ConfigException e) {
      throw new RuntimeException("Unable to load game config from " + gameFolderPath, e);
    }
  }
}
