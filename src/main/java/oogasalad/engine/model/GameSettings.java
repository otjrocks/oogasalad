package oogasalad.engine.model;

/**
 * A class to store settings from the game.
 *
 * @author Will He
 */
public class GameSettings {

  private double gameSpeed;
  private int startingLives;
  private int initialScore;
  private String edgePolicy;
  private int width;
  private int height;

  /**
   * Get the int representing the initial score for the game.
   *
   * @return The initial score.
   */
  public int getInitialScore() {
    return initialScore;
  }

  /**
   * Set the initial score for this settings object.
   *
   * @param initialScore The initial score.
   */
  public void setInitialScore(int initialScore) {
    this.initialScore = initialScore;
  }

  /**
   * Get the game speed.
   *
   * @return A double representing the speed.
   */
  public double getGameSpeed() {
    return gameSpeed;
  }

  /**
   * Set the game speed.
   *
   * @param gameSpeed A double representing the game speed.
   */
  public void setGameSpeed(double gameSpeed) {
    this.gameSpeed = gameSpeed;
  }

  /**
   * Get the number of starting lives.
   *
   * @return Integer representing the starting lives.
   */
  public int getStartingLives() {
    return startingLives;
  }

  /**
   * Set the starting number of lives.
   *
   * @param startingLives An int representing the starting number of lives.
   */
  public void setStartingLives(int startingLives) {
    this.startingLives = startingLives;
  }

  /**
   * Get the edge policy as a string.
   *
   * @return The string representing the edge policy.
   */
  public String getEdgePolicy() {
    return edgePolicy;
  }

  /**
   * Set the edge policy.
   *
   * @param edgePolicy The string representing a valid edge policy.
   */
  public void setEdgePolicy(String edgePolicy) {
    this.edgePolicy = edgePolicy;
  }

  /**
   * Get the width of this game map.
   *
   * @return The int representing the game map width.
   */
  public int getWidth() {
    return width;
  }

  /**
   * Set the width of this game map.
   *
   * @param width The int representing the width you want.
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Get the height of the game map.
   *
   * @return The current game map height.
   */
  public int getHeight() {
    return height;
  }

  /**
   * Set the height of the game map.
   *
   * @param height The height of the game map.
   */
  public void setHeight(int height) {
    this.height = height;
  }
}
