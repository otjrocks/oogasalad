package oogasalad;
/**
 * The {@code GameState} interface provides methods to manage the states of HUD elements and other
 * graphical interfaces while the game is running.
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
   * Retrieves a list of all registered HUD components.
   *
   * @return list of HUD components currently displayed in the game.
   */
  List<HUDComponent> getHUDComponents();

  /**
   * Resets the game state to its initial configuration (e.g., on reset or level restart).
   */
  void resetState();

  /*
   Skeleton classes to ensure interface compiles correctly.
   */

  interface HUDComponent {
    // Marker or functional interface representing a HUD element
  }

  class SaveFailedException extends Exception {
    public SaveFailedException(String message) {
      super(message);
    }
  }

  class LoadFailedException extends Exception {
    public LoadFailedException(String message) {
      super(message);
    }
  }
}
