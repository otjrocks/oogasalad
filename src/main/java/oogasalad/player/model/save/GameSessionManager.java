package oogasalad.player.model.save;

import oogasalad.engine.records.config.ConfigModelRecord;
import oogasalad.engine.records.config.model.SaveConfigRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading game progress within a session.
 */
public class GameSessionManager {

  private final String gameFolderName;
  private SaveConfigRecord saveConfig;
  private final String saveName;

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
    System.out.println(saveConfig.currentLevel());
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
  public void save() {
    try {
      SaveManager.saveGame(saveConfig, gameFolderName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Resets the session to a new one using the default configuration.
   */
  public void resetSession(ConfigModelRecord configModel) {
    startNewSession(configModel);
  }

  public int getCurrentLevel() {
    return saveConfig.currentLevel();
  }

  public int getLives() {
    return saveConfig.lives();
  }

  public int getHighScore() {
    return saveConfig.highScore();
  }

  public List<Integer> getLevelOrder() {
    return saveConfig.levelOrder();
  }

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

  public int getCurrentScore() {
    return saveConfig.totalScore();
  }

  public void advanceLevel(int levelScore) {
    int nextLevel = saveConfig.currentLevel() + 1;
    if (nextLevel >= saveConfig.levelOrder().size()) {
      System.out.println("All levels complete — cannot advance further.");
      return;  // Prevent saving invalid level
    }

    saveConfig = new SaveConfigRecord(
        saveConfig.saveName(),
        nextLevel,
        levelScore,
        saveConfig.lives(),
        Math.max(levelScore, saveConfig.highScore()),
        saveConfig.levelOrder()
    );
    System.out.println("Advancing to Level " + saveConfig.currentLevel());
    save();
  }



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
