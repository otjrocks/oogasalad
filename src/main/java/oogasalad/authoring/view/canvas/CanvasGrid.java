package oogasalad.authoring.view.canvas;

public class CanvasGrid {

  private int rows;
  private int cols;
  private double tileWidth;
  private double tileHeight;

  public CanvasGrid(int initialRows, int initialCols) {
    this.rows = initialRows;
    this.cols = initialCols;
  }

  /**
   * Recalculates tile width and height to fill the given canvas dimensions.
   */
  public void resizeGrid(double canvasWidth, double canvasHeight, int newCols, int newRows) {
    this.cols = newCols;
    this.rows = newRows;
    this.tileWidth = canvasWidth / cols;
    this.tileHeight = canvasHeight / rows;
  }

  public int getCols() {
    return cols;
  }

  public int getRows() {
    return rows;
  }

  public double getTileWidth() {
    return tileWidth;
  }

  public double getTileHeight() {
    return tileHeight;
  }

  public double getXFromCol(int col) {
    return col * tileWidth;
  }

  public double getYFromRow(int row) {
    return row * tileHeight;
  }

  public int getColFromX(double x) {
    return (int) Math.floor(x / tileWidth);
  }

  public int getRowFromY(double y) {
    return (int) Math.floor(y / tileHeight);
  }

  public boolean isValidCell(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }
}
