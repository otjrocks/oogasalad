package oogasalad.player.model.save;

import oogasalad.engine.records.config.ConfigModelRecord;
import oogasalad.engine.records.config.model.SaveConfigRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import oogasalad.player.model.exceptions.SaveFileException;

/**
 * Handles saving and loading game progress within a session.
 */
public class GameSessionManager {

  private final String gameFolderName;
  private SaveConfigRecord saveConfig;
  private final String saveName;

  /**
   * Creates a new Game Session Manager with a save config record.
   *
   * @param gameFolderName The game of the folder to save the game session to.
   * @param saveName       The save name of this session.
   */
  public GameSessionManager(String gameFolderName, String saveName) {
    this.saveName = saveName;
    this.gameFolderName = gameFolderName;
  }

  /**
   * Initializes a new game session using default values from the given config.
   *
   * @param configModel the game configuration containing default settings
   */
  public void startNewSession(ConfigModelRecord configModel) {
    List<Integer> defaultOrder = new ArrayList<>();
    for (int i = 0; i < configModel.levels().size(); i++) {
      defaultOrder.add(i);
    }

    saveConfig = new SaveConfigRecord(
        saveName,
        0,
        configModel.settings().initialScore(),
        configModel.settings().startingLives(),
        configModel.settings().initialScore(),
        defaultOrder
    );
    save();
  }

  /**
   * Loads an existing session from file.
   */
  public void loadExistingSession() throws IOException {
    saveConfig = SaveManager.loadGame(gameFolderName, saveName);
  }


  /**
   * Saves the current session state to a file.
   */
  public void save() throws SaveFileException {
    try {
      SaveManager.saveGame(saveConfig, gameFolderName);
    } catch (IOException e) {
      throw new SaveFileException(e.getMessage());
    }
  }

  /**
   * Resets the session to a new one using the default configuration.
   */
  public void resetSession(ConfigModelRecord configModel) {
    startNewSession(configModel);
  }

  /**
   * Returns the current level in the save config.
   */
  public int getCurrentLevel() {
    return saveConfig.currentLevel();
  }

  /**
   * Returns the current lives of the save config.
   */
  public int getLives() {
    return saveConfig.lives();
  }

  /**
   * Returns the high score in the saveConfig
   */
  public int getHighScore() {
    return saveConfig.highScore();
  }

  /**
   * Set a new high score to the game save file, if the new high score is greater than the current
   * high score.
   *
   * @param newHighScore The new potential high score.
   */
  public void setHighScore(int newHighScore) {
    if (newHighScore > saveConfig.highScore()) {
      saveConfig = new SaveConfigRecord(
          saveConfig.saveName(),
          saveConfig.currentLevel(),
          saveConfig.totalScore(),
          saveConfig.lives(),
          newHighScore,
          saveConfig.levelOrder()
      );
      save();
    }
  }

  /**
   * Returns the saved level order in the save config
   */
  public List<Integer> getLevelOrder() {
    return saveConfig.levelOrder();
  }

  /**
   * Sets lives in safe config to one less.
   */
  public void loseLife() {
    saveConfig = new SaveConfigRecord(
        saveConfig.saveName(),
        saveConfig.currentLevel(),
        saveConfig.totalScore(),
        saveConfig.lives() - 1,
        saveConfig.highScore(),
        saveConfig.levelOrder()
    );
    save();
  }

  /**
   * Returns the current score in the save config.
   */
  public int getCurrentScore() {
    return saveConfig.totalScore();
  }

  /**
   * Moves to the next level.
   */
  public void advanceLevel(int levelScore) {
    int nextLevel = saveConfig.currentLevel() + 1;
    if (nextLevel >= saveConfig.levelOrder().size()) {
      return;
    }

    saveConfig = new SaveConfigRecord(
        saveConfig.saveName(),
        nextLevel,
        levelScore,
        saveConfig.lives(),
        Math.max(levelScore, saveConfig.highScore()),
        saveConfig.levelOrder()
    );
    save();
  }

  /**
   * Sets lives to an int in save config.
   */
  public void setLives(int newLives) {
    saveConfig = new SaveConfigRecord(
        saveConfig.saveName(),
        saveConfig.currentLevel(),
        saveConfig.totalScore(),
        newLives,
        saveConfig.highScore(),
        saveConfig.levelOrder()
    );
    save();
  }
}
