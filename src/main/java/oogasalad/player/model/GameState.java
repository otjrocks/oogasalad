package oogasalad.player.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import oogasalad.engine.records.config.model.SaveConfigRecord;

/**
 * Implementation of the GameState interface. This class manages the player's score, lives, and HUD
 * components. It also provides functionality for saving and loading game states.
 *
 * @author Troy Ludwig
 */
public class GameState implements GameStateInterface {

  private int score;
  private int lives;
  private double timeElapsed = 0;
  private int currentLevel;
  private List<Double> scoresPerLevel;
  private final static String SAVE_FOLDER = "data/saves/";

  /**
   * Creates game state representation (for HUD elements) based on a number of initial lives
   *
   * @param initialLives: Number of lives we want the player to start with
   */
  public GameState(int initialLives) {
    this.score = 0;
    this.lives = initialLives;
    this.currentLevel = 0;
    this.scoresPerLevel = new ArrayList<>();
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
    this.score = 0;
    this.lives = 0;
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
  public void saveGameProgress(String saveName) {
    SaveConfigRecord saveConfig = new SaveConfigRecord(
        saveName,
        currentLevel,
        scoresPerLevel,
        lives,
        score,
        generateDefaultLevelOrder() // if you want
    );

    try {
      ObjectMapper mapper = new ObjectMapper();
      File saveFile = new File(SAVE_FOLDER + saveName + ".json");
      mapper.writerWithDefaultPrettyPrinter().writeValue(saveFile, saveConfig);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void loadGameProgress(String saveName) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      SaveConfigRecord saveConfig = mapper.readValue(
          new File(SAVE_FOLDER + saveName + ".json"),
          SaveConfigRecord.class
      );

      this.currentLevel = saveConfig.currentLevel();
      this.scoresPerLevel = saveConfig.scores();
      this.lives = saveConfig.lives();
      this.score = saveConfig.highScore();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void resetGameProgress() {
    this.currentLevel = 0;
    this.score = 0;
    this.lives = 3;
    this.scoresPerLevel = new ArrayList<>();
  }

  private List<Integer> generateDefaultLevelOrder() {
    // placeholder, you can improve this
    List<Integer> levels = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      levels.add(i);
    }
    return levels;
  }
}

