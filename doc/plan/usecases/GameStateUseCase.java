package oogasalad;
/**
 * Use Case: GAMESTATE-2: Save GameState.
 * This use case illustrates how the GameState API can be used to save the current game state
 * (score, lives, and HUD components) to a file.
 *
 * In a real implementation, the GameState would serialize itself into JSON or another format.
 * This example shows how the API is used in a high-level context, assuming the backend is implemented.
 * A working file save might use something like a FileWriter or object serialization.
 *
 * This example assumes that saveState writes the data to a specified path and throws an exception
 * if the operation fails.
 *
 * @Author: Luke Fu
 */
public class GameStateUseCase {

  public static void main(String[] args) {
    // A concrete/mock implementation of GameState
    GameState gameState = new ConcreteGameState(); // You would substitute this with your actual class

    // Update state values
    gameState.updateScore(500);
    gameState.updateLives(-1);

    // Optionally, register some HUD components
    gameState.addHUDComponent(() -> System.out.println("HUD: Score Display"));
    gameState.addHUDComponent(() -> System.out.println("HUD: Pause Button"));

    // Attempt to save the game state to a file
    String savePath = "example/path/saved_game.json";

    try {
      gameState.saveState(savePath);
      System.out.println("Game state saved successfully to " + savePath);
    } catch (SaveFailedException e) {
      System.err.println("Failed to save game state: " + e.getMessage());
    }
  }

  /**
   * Example mock implementation of GameState for illustration only.
   * In practice, your class would contain logic for serializing state, etc.
   */
  static class ConcreteGameState implements GameState {
    private int score = 0;
    private int lives = 3;
    private final List<HUDComponent> hudComponents = new java.util.ArrayList<>();

    public void updateScore(int delta) {
      score += delta;
    }

    public int getScore() {
      return score;
    }

    public void updateLives(int delta) {
      lives += delta;
    }

    public int getLives() {
      return lives;
    }

    public void saveState(String filePath) throws SaveFailedException {
      // Simulate a successful save
      if (filePath == null || filePath.isEmpty()) {
        throw new SaveFailedException("Invalid file path.");
      }
      System.out.println("Saving score: " + score + ", lives: " + lives + " to " + filePath);
      // In real code, write JSON or binary serialization here
    }

    public void loadState(String filePath) {
      throw new UnsupportedOperationException("Not implemented in this example.");
    }

    public void addHUDComponent(HUDComponent component) {
      hudComponents.add(component);
    }

    public List<HUDComponent> getHUDComponents() {
      return hudComponents;
    }

    public void resetState() {
      score = 0;
      lives = 3;
      hudComponents.clear();
    }
  }
}