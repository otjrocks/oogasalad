package oogasalad.player.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.records.config.model.SaveConfigRecord;
import oogasalad.engine.records.model.GameSettingsRecord;
import oogasalad.player.model.exceptions.SaveFileException;

/**
 * Implementation of the GameState interface. This class manages the player's score, lives,
 * and HUD components. It also provides functionality for saving and loading game states.
 *
 * @author Troy Ludwig
 */
public class GameState implements GameStateInterface {

  private int score;
  private int lives;
  private final int startingLives;
  private final int initialScore;
  private double timeElapsed = 0;
  private int currentLevel;
  private List<Integer> levelOrder;
  private final static String SAVE_FOLDER = "data/saves/";

  /**
   * Loads in game settings given the game setting record.
   * @param gameSettings contains default settings for given game.
   */
  public GameState(GameSettingsRecord gameSettings) {
    this.startingLives = gameSettings.startingLives();
    this.initialScore = gameSettings.initialScore();
    this.score = initialScore;
    this.lives = startingLives;
    this.currentLevel = 0;
    this.levelOrder = generateDefaultLevelOrder(); // You may want to replace this
  }

  @Override
  public void updateScore(int delta) {
    this.score += delta;
  }

  @Override
  public int getScore() {
    return score;
  }

  @Override
  public void updateLives(int delta) {
    this.lives += delta;
  }

  @Override
  public int getLives() {
    return lives;
  }

  @Override
  public void resetState() {
    this.score = initialScore;
    this.lives = startingLives;
    this.timeElapsed = 0;
  }

  @Override
  public double getTimeElapsed() {
    return timeElapsed;
  }

  @Override
  public void setTimeElapsed(double timeElapsed) {
    this.timeElapsed = timeElapsed;
  }

  @Override
  public void resetTimeElapsed() {
    this.timeElapsed = 0;
  }

  @Override
  public void saveGameProgress(String saveName) throws SaveFileException {
    SaveConfigRecord saveConfig = new SaveConfigRecord(
        saveName,
        currentLevel,
        score,
        lives,
        score, // highScore = totalScore
        levelOrder
    );

    try {
      ObjectMapper mapper = new ObjectMapper();
      File saveFile = new File(SAVE_FOLDER + saveName + ".json");
      mapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, saveConfig);
    } catch (IOException e) {
      throw new SaveFileException(e.getMessage());
    }
  }

  @Override
  public void loadGameProgress(String saveName) throws SaveFileException {
    try {
      ObjectMapper mapper = new ObjectMapper();
      SaveConfigRecord saveConfig = mapper.readValue(
          new File(SAVE_FOLDER + saveName + ".json"),
          SaveConfigRecord.class
      );

      this.currentLevel = saveConfig.currentLevel();
      this.score = saveConfig.totalScore();
      this.lives = saveConfig.lives();
      this.levelOrder = saveConfig.levelOrder();

    } catch (IOException e) {
      throw new SaveFileException("Unable to load save file");
    }
  }

  @Override
  public void resetGameProgress() {
    this.currentLevel = 0;
    this.score = initialScore;
    this.lives = startingLives;
    this.timeElapsed = 0;
    this.levelOrder = generateDefaultLevelOrder();
  }

  private List<Integer> generateDefaultLevelOrder() {
    List<Integer> levels = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      levels.add(i);
    }
    return levels;
  }
}
