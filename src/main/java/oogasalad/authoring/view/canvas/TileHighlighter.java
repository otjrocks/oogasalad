package oogasalad.authoring.view.canvas;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;

public class TileHighlighter {

  private final Rectangle hover;
  private final Rectangle selection;

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

  public void resizeTo(double tileWidth, double tileHeight) {
    hover.setWidth(tileWidth);
    hover.setHeight(tileHeight);
    selection.setWidth(tileWidth);
    selection.setHeight(tileHeight);
  }

  public void moveHoverTo(double x, double y) {
    hover.setX(x);
    hover.setY(y);
  }

  public void moveSelectionTo(double x, double y) {
    selection.setX(x);
    selection.setY(y);
  }

  public void showHover() {
    hover.setVisible(true);
  }

  public void hideHover() {
    hover.setVisible(false);
  }

  public void showSelection() {
    selection.setVisible(true);
  }

  public void hideSelection() {
    selection.setVisible(false);
  }

  public Rectangle getHoverRectangle() {
    return hover;
  }

  public Rectangle getSelectionRectangle() {
    return selection;
  }

}
