package oogasalad.engine.model;

/**
 * Represents a tile-based layout for the game map.
 */
public class Tiles {

  private String[] layout;

  /**
   * Empty constructor for JSON deserialization.
   */
  public Tiles() {
    // Required for JSON
  }

  /**
   * Creates a Tiles object with a given layout.
   * @param layout array of strings representing tile rows
   */
  public Tiles(String[] layout) {
    this.layout = layout.clone(); // Defensive copy
  }

  /**
   * Gets a copy of the layout.
   * @return the tile layout
   */
  public String[] getLayout() {
    return layout.clone(); // Prevent exposing internal array
  }

  /**
   * Sets the layout from a given array.
   * @param layout new tile layout
   */
  public void setLayout(String[] layout) {
    this.layout = layout.clone(); // Defensive copy
  }
}
