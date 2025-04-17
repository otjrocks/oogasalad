package oogasalad.player.controller;

import java.nio.file.Paths;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.ConfigModelRecord;
import oogasalad.engine.config.JsonConfigSaver;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.model.GameMapInterface;
import oogasalad.engine.model.api.GameMapFactory;
import oogasalad.engine.model.exceptions.InvalidPositionException;

/**
 * A controller that is used to progress through levels of the game.
 *
 * @author Owen Jennings
 */
public class LevelController {

  private int myLevelIndex;
  private final ConfigModelRecord myConfigModel;
  private final MainController myMainController;

  /**
   * Create a level controller with the current config model.
   *
   * @param mainController The main controller for this program.
   * @param configModel    The config model for this level controller.
   */
  public LevelController(MainController mainController, ConfigModelRecord configModel) {
    myMainController = mainController;
    myConfigModel = configModel;
    myLevelIndex = configModel.currentLevelIndex();
  }

  /**
   * Get the current level map. Returns null if the level does not exist
   *
   * @return The current level map or null if the level does not exist
   */
  public GameMapInterface getCurrentLevelMap() {
    if (myLevelIndex > myConfigModel.levels().size() - 1) { // you have completed last level.
      LoggingManager.LOGGER.info(
          "You have requested to get the level map for a nonexistent level.");
      return null;
    }
    GameMapInterface gameMap = null;
    try {
      gameMap = GameMapFactory.createGameMap(myMainController.getInputManager(),
          myConfigModel,
          myLevelIndex);
    } catch (InvalidPositionException e) {
      LoggingManager.LOGGER.warn("Failed to create or populate GameMap: ", e);
    }
    return gameMap;
  }

  /**
   * Increment the current level.
   */
  public void incrementAndUpdateConfig() {
    myLevelIndex++;
    try {
      JsonConfigSaver saver = new JsonConfigSaver();
      saver.saveUpdatedLevelIndex(myLevelIndex, Paths.get("data/games/BasicPacMan"));
      LoggingManager.LOGGER.info("Level index updated and saved to gameConfig.json");
    } catch (ConfigException e) {
      LoggingManager.LOGGER.warn("Failed to save updated level index", e);
    }
  }




  /**
   * Get the current level index.
   *
   * @return The int representing the current level 0-indexed.
   */
  public int getCurrentLevelIndex() {
    return myLevelIndex;
  }

  /**
   * Gets if there are any levels remaining
   * @return Whether a next level exists
   */
  public boolean hasNextLevel() {
    return myLevelIndex + 1 < myConfigModel.levels().size();
  }

}
