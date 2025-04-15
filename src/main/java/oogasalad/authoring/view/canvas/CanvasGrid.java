package oogasalad.authoring.view.canvas;

/**
 * Represents a grid layout for a canvas, handling conversion between pixel coordinates
 * and grid positions. Responsible for tracking the number of rows and columns and calculating
 * tile dimensions to fit the canvas size.
 *
 * @author Will He
 */
public class CanvasGrid {

  private int rows;
  private int cols;
  private double tileWidth;
  private double tileHeight;

  /**
   * Constructs a CanvasGrid with the given number of initial rows and columns.
   *
   * @param initialRows the initial number of rows
   * @param initialCols the initial number of columns
   */
  public CanvasGrid(int initialRows, int initialCols) {
    this.rows = initialRows;
    this.cols = initialCols;
  }

  /**
   * Resizes the grid to fit new canvas dimensions and updates the number of rows and columns.
   *
   * @param canvasWidth the new canvas width in pixels
   * @param canvasHeight the new canvas height in pixels
   * @param newCols the new number of columns
   * @param newRows the new number of rows
   */
  public void resizeGrid(double canvasWidth, double canvasHeight, int newCols, int newRows) {
    this.cols = newCols;
    this.rows = newRows;
    this.tileWidth = canvasWidth / cols;
    this.tileHeight = canvasHeight / rows;
  }

  /**
   * Returns the number of columns in the grid.
   *
   * @return the number of columns
   */
  public int getCols() {
    return cols;
  }

  /**
   * Returns the number of rows in the grid.
   *
   * @return the number of rows
   */
  public int getRows() {
    return rows;
  }

  /**
   * Returns the width of a single tile in pixels.
   *
   * @return the tile width
   */
  public double getTileWidth() {
    return tileWidth;
  }

  /**
   * Returns the height of a single tile in pixels.
   *
   * @return the tile height
   */
  public double getTileHeight() {
    return tileHeight;
  }

  /**
   * Converts a column index to an x-coordinate on the canvas.
   *
   * @param col the column index
   * @return the x-coordinate in pixels
   */
  public double getXFromCol(int col) {
    return col * tileWidth;
  }

  /**
   * Converts a row index to a y-coordinate on the canvas.
   *
   * @param row the row index
   * @return the y-coordinate in pixels
   */
  public double getYFromRow(int row) {
    return row * tileHeight;
  }

  /**
   * Converts an x-coordinate to a column index.
   *
   * @param x the x-coordinate in pixels
   * @return the column index
   */
  public int getColFromX(double x) {
    return (int) Math.floor(x / tileWidth);
  }

  /**
   * Converts a y-coordinate to a row index.
   *
   * @param y the y-coordinate in pixels
   * @return the row index
   */
  public int getRowFromY(double y) {
    return (int) Math.floor(y / tileHeight);
  }

  /**
   * Checks whether the given row and column indices represent a valid cell in the grid.
   *
   * @param row the row index
   * @param col the column index
   * @return true if the cell is within grid bounds, false otherwise
   */
  public boolean isValidCell(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }
}
