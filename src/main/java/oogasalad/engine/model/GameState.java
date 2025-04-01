package oogasalad.engine.model;

import java.util.List;

/**
 * The {@code GameState} interface defines methods to manage HUD elements and game metadata
 * like score, lives, and serialization functionality for pause/save/resume capabilities.
 * It is used to keep track of dynamic values and HUD components throughout the game.
 *
 * @author Luke Fu
 */
public interface GameState {

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
   * Saves the current game state to a file.
   *
   * @param filePath the path to the file where the game state should be saved.
   * @throws SaveFailedException if the game state could not be saved.
   */
  void saveState(String filePath) throws SaveFailedException;

  /**
   * Loads a saved game state from a file.
   *
   * @param filePath the path to the file to load the game state from.
   * @throws LoadFailedException if the game state could not be loaded.
   */
  void loadState(String filePath) throws LoadFailedException;

  /**
   * Registers a new HUD component (e.g., score box, pause button, level display).
   *
   * @param component the HUDComponent to be registered.
   */
  void addHUDComponent(HUDComponent component);

  /**
   * Returns all currently registered HUD components that should be displayed.
   *
   * @return list of HUD components active in the HUD.
   */
  List<HUDComponent> getHUDComponents();

  /**
   * Resets the game state to its initial configuration, including score, lives, and registered HUD.
   */
  void resetState();

  /*
   * Supporting types for GameState API
   */

  /**
   * Marker or functional interface representing a HUD element.
   */
  interface HUDComponent {
    // Extendable interface for things like ScoreBox, PauseButton, etc.
  }

  /**
   * Exception thrown when saving the game state fails.
   */
  class SaveFailedException extends Exception {
    public SaveFailedException(String message) {
      super(message);
    }
  }

  /**
   * Exception thrown when loading the game state fails.
   */
  class LoadFailedException extends Exception {
    public LoadFailedException(String message) {
      super(message);
    }
  }
}
