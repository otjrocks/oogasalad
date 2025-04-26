package oogasalad.player.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.exceptions.InvalidPositionException;
import oogasalad.engine.records.config.ConfigModelRecord;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.api.GameMapFactory;
import oogasalad.player.model.save.GameSessionManager;

/**
 * A controller that is used to progress through levels of the game.
 * Handles level order and state independently after loading.
 *
 * @author Luke Fu
 */
public class LevelController {

  private int myLevelIndex;
  private final ConfigModelRecord myConfigModel;
  private final MainController myMainController;
  private final List<Integer> myLevelOrder;
  private final GameSessionManager sessionManager;

  public LevelController(MainController mainController, ConfigModelRecord configModel,
      boolean randomized, GameSessionManager sessionManager) {
    myMainController = mainController;
    myConfigModel = configModel;
    this.sessionManager = sessionManager;
    this.myLevelIndex = sessionManager.getCurrentLevel(); // Start from saved level, not configModel directly

    myLevelOrder = new ArrayList<>();
    for (int i = 0; i < myConfigModel.levels().size(); i++) {
      myLevelOrder.add(i);
    }

    if (randomized) {
      Collections.shuffle(myLevelOrder);
    }
  }
  /**
   * Creates current game map and checks if map is out of bounds.
   */
  public GameMapInterface getCurrentLevelMap() {
    if (myLevelIndex >= myConfigModel.levels().size()) {
      LoggingManager.LOGGER.info("Requested non-existent level.");
      return null;
    }

    try {
      int mappedIndex = myLevelOrder.get(myLevelIndex);
      return GameMapFactory.createGameMap(myMainController.getInputManager(), myConfigModel, mappedIndex);
    } catch (InvalidPositionException e) {
      LoggingManager.LOGGER.warn("Failed to create GameMap: ", e);
      return null;
    }
  }

  /**
   * Moves the level up by one
   */
  public void incrementLevel() {
    myLevelIndex++;
  }

  /**
   * Resets level count to 0
   */
  public void resetLevels() {
    myLevelIndex = 0;
  }

  /**
   * Returns current level index
   */
  public int getCurrentLevelIndex() {
    return myLevelIndex;
  }

  /**
   * Boolean to determine if there is a next level.
   */
  public boolean hasNextLevel() {
    return myLevelIndex + 1 < myLevelOrder.size();
  }

  /**
   * Returns the current mapped level index.
   */
  public int getMappedLevelIndex() {
    int mappedIndex = myLevelOrder.get(myLevelIndex);
    LoggingManager.LOGGER.info("Logical level {} mapped to actual level {}", myLevelIndex, mappedIndex);
    return mappedIndex;
  }
}
