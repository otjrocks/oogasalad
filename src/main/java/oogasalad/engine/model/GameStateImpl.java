package oogasalad.engine.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the GameState interface. This class manages the player's score, lives, and HUD
 * components. It also provides functionality for saving and loading game states.
 *
 * @author Troy Ludwig
 */
public class GameStateImpl implements GameState {

  private int score;
  private int lives;
  private boolean gameOver;
  private final List<HudComponent> hudComponents;

  /**
   * Creates game state representation (for HUD elements) based on a number of initial lives
   *
   * @param initialLives: Number of lives we want the player to start with
   */
  public GameStateImpl(int initialLives) {
    this.score = 0;
    this.lives = initialLives;
    this.gameOver = false;
    this.hudComponents = new ArrayList<>();
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

  // Asked ChatGPT to help with this method
  @Override
  public void saveState(String filePath) throws SaveFailedException {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
      out.writeObject(this);
    } catch (IOException e) {
      throw new SaveFailedException("Failed to save game state: " + e.getMessage());
    }
  }

  // Asked ChatGPT to help with this method
  @Override
  public void loadState(String filePath) throws LoadFailedException {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
      GameStateImpl loadedState = (GameStateImpl) in.readObject();
      this.score = loadedState.score;
      this.lives = loadedState.lives;
      this.hudComponents.clear();
      this.hudComponents.addAll(loadedState.hudComponents);
    } catch (IOException | ClassNotFoundException e) {
      throw new LoadFailedException("Failed to load game state: " + e.getMessage());
    }
  }

  @Override
  public void addHudComponent(HudComponent component) {
    hudComponents.add(component);
  }

  @Override
  public List<HudComponent> getHudComponents() {
    return new ArrayList<>(hudComponents);
  }

  @Override
  public void resetState() {
    this.score = 0;
    this.lives = 0;
    this.hudComponents.clear();
  }

  @Override
  public void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }

  @Override
  public boolean isGameOver() {
    return gameOver;
  }
}

