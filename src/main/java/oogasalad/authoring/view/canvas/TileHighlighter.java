package oogasalad.authoring.view.canvas;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;

/**
 * Handles the hover and selection highlighting of tiles on the canvas grid.
 * Provides visual feedback during entity placement and movement.
 *
 * @author Will He
 */
public class TileHighlighter {

  private final Rectangle hover;
  private final Rectangle selection;

  /**
   * Constructs a TileHighlighter and adds hover and selection rectangles to the root pane.
   *
   * @param root the parent Pane where highlights should be displayed
   */
  public TileHighlighter(Pane root) {
    hover = new Rectangle();
    hover.setFill(Color.TRANSPARENT);
    hover.setStroke(Color.GRAY);
    hover.setVisible(false);
    hover.getStyleClass().add("tile-hover");

    selection = new Rectangle();
    selection.setFill(Color.TRANSPARENT);
    selection.setStroke(Color.BLUE);
    selection.setStrokeWidth(2);
    selection.setVisible(false);
    selection.getStyleClass().add("tile-selection");

    root.getChildren().addAll(hover, selection);
  }

  /**
   * Resizes both hover and selection rectangles to match the given tile dimensions.
   *
   * @param tileWidth  the width of a tile
   * @param tileHeight the height of a tile
   */
  public void resizeTo(double tileWidth, double tileHeight) {
    hover.setWidth(tileWidth);
    hover.setHeight(tileHeight);
    selection.setWidth(tileWidth);
    selection.setHeight(tileHeight);
  }

  /**
   * Moves the hover rectangle to the specified (x, y) position.
   *
   * @param x the new X coordinate
   * @param y the new Y coordinate
   */
  public void moveHoverTo(double x, double y) {
    hover.setX(x);
    hover.setY(y);
  }

  /**
   * Moves the selection rectangle to the specified (x, y) position.
   *
   * @param x the new X coordinate
   * @param y the new Y coordinate
   */
  public void moveSelectionTo(double x, double y) {
    selection.setX(x);
    selection.setY(y);
  }

  /**
   * Makes the hover rectangle visible.
   */
  public void showHover() {
    hover.setVisible(true);
  }

  /**
   * Hides the hover rectangle.
   */
  public void hideHover() {
    hover.setVisible(false);
  }

  /**
   * Makes the selection rectangle visible.
   */
  public void showSelection() {
    selection.setVisible(true);
  }

  /**
   * Hides the selection rectangle.
   */
  public void hideSelection() {
    selection.setVisible(false);
  }

  /**
   * Returns the hover rectangle for direct access or styling.
   *
   * @return the hover rectangle
   */
  public Rectangle getHoverRectangle() {
    return hover;
  }

  /**
   * Returns the selection rectangle for direct access or styling.
   *
   * @return the selection rectangle
   */
  public Rectangle getSelectionRectangle() {
    return selection;
  }

  /**
   * Checks whether the selection rectangle is currently visible.
   *
   * @return true if the selection is visible, false otherwise
   */
  public boolean isSelectionVisible() {
    return selection.isVisible();
  }

  /**
   * Returns the current X position of the selection rectangle.
   *
   * @return the X coordinate
   */
  public double getSelectionX() {
    return selection.getX();
  }

  /**
   * Returns the current Y position of the selection rectangle.
   *
   * @return the Y coordinate
   */
  public double getSelectionY() {
    return selection.getY();
  }
}
