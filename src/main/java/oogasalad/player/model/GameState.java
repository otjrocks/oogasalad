package oogasalad.player.model;

/**
 * Implementation of the GameState interface. This class manages the player's score, lives, and HUD
 * components. It also provides functionality for saving and loading game states.
 *
 * @author Troy Ludwig
 */
public class GameState implements GameStateInterface {

  private int score;
  private int lives;
  private boolean gameOver;
  private double timeElapsed = 0;

  /**
   * Creates game state representation (for HUD elements) based on a number of initial lives
   *
   * @param initialLives: Number of lives we want the player to start with
   */
  public GameState(int initialLives) {
    this.score = 0;
    this.lives = initialLives;
    this.gameOver = false;
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
  public void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
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

}

