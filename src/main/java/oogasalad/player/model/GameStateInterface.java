package oogasalad.player.model;

/**
 * The {@code GameState} interface defines methods to manage HUD elements and game metadata like
 * score, lives, and serialization functionality for pause/save/resume capabilities. It is used to
 * keep track of dynamic values and HUD components throughout the game.
 *
 * @author Luke Fu
 */
public interface GameStateInterface {

  /**
   * Updates the player's score by the specified amount.
   *
   * @param delta the number of points to add (can be negative to subtract points).
   */
  void updateScore(int delta);

  /**
   * Retrieves the current score of the player.
   *
   * @return the current score.
   */
  int getScore();

  /**
   * Updates the number of remaining lives for the player.
   *
   * @param delta the number of lives to add (can be negative to subtract lives).
   */
  void updateLives(int delta);

  /**
   * Retrieves the current number of lives.
   *
   * @return the number of remaining lives.
   */
  int getLives();

  /**
   * Get the time elapsed in the game.
   *
   * @return A double representing time elapsed since the last reset.
   */
  double getTimeElapsed();

  /**
   * Set the amount of time that has elapsed so far in the game.
   *
   * @param timeElapsed The amount of time that has elapsed.
   */
  void setTimeElapsed(double timeElapsed);

  /**
   * Reset the time elapsed counter.
   */
  void resetTimeElapsed();

  /**
   * Resets the game state to its initial configuration, including score, lives, and registered
   * HUD.
   */
  void resetState();

  /**
   * Sets the lives to a specific value
   */
  void setLives(int lives);

  /**
   * Saves the current game progress to a file.
   *
   * @param saveName the name of the save file.
   */
  void saveGameProgress(String saveName);

  /**
   * Loads game progress from a save file.
   *
   * @param saveName the name of the save file.
   */
  void loadGameProgress(String saveName);

  /**
   * Resets game progress to the default initial state.
   */
  void resetGameProgress();

  /**
   * Get the current game's high score.
   *
   * @return An int representing the game's high score.
   */
  int getHighScore();
}
