package oogasalad.authoring.view.canvas;

import java.util.function.Consumer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import oogasalad.engine.model.EntityPlacement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager {

  private final Pane root;
  private final CanvasGrid grid;
  private final Map<ImageView, EntityPlacement> entityViews = new HashMap<>();
  private EntityPlacement[][] gridEntities;
  private final PlacementHandler placementHandler;
  private final Consumer<MouseEvent> onPressed;
  private final Consumer<MouseEvent> onDragged;
  private final Consumer<MouseEvent> onReleased;

  /**
   * @param root Pane to attach visuals to
   * @param grid grid metadata (rows, cols, tile size)
   * @param placementHandler callback to notify model on visual move
   */

  public EntityManager(Pane root, CanvasGrid grid, PlacementHandler placementHandler,
      Consumer<MouseEvent> onPressed,
      Consumer<MouseEvent> onDragged,
      Consumer<MouseEvent> onReleased) {
    this.root = root;
    this.grid = grid;
    this.placementHandler = placementHandler;
    this.onPressed = onPressed;
    this.onDragged = onDragged;
    this.onReleased = onReleased;
  }


  public void clear() {
    entityViews.clear();
    gridEntities = new EntityPlacement[grid.getRows()][grid.getCols()];
  }

  public void reloadEntities(List<EntityPlacement> placements) {
    clear();

    for (EntityPlacement placement : placements) {
      int row = grid.getRowFromY(placement.getY());
      int col = grid.getColFromX(placement.getX());

      if (grid.isValidCell(row, col)) {
        addEntityVisual(placement);
      }
    }
  }

  public void addEntityVisual(EntityPlacement placement) {
    int row = grid.getRowFromY(placement.getY());
    int col = grid.getColFromX(placement.getX());

    double x = grid.getXFromCol(col);
    double y = grid.getYFromRow(row);

    ImageView imageView = new ImageView(new Image(placement.getEntityImagePath()));
    imageView.setX(x);
    imageView.setY(y);
    imageView.setFitWidth(grid.getTileWidth());
    imageView.setFitHeight(grid.getTileHeight());

    imageView.setOnMousePressed(onPressed::accept);
    imageView.setOnMouseDragged(onDragged::accept);
    imageView.setOnMouseReleased(onReleased::accept);


    root.getChildren().add(imageView);
    entityViews.put(imageView, placement);
    gridEntities[row][col] = placement;
  }

  public void updateEntityPosition(EntityPlacement placement, int newRow, int newCol) {
    int oldRow = grid.getRowFromY(placement.getY());
    int oldCol = grid.getColFromX(placement.getX());

    gridEntities[oldRow][oldCol] = null;
    gridEntities[newRow][newCol] = placement;

    double newX = grid.getXFromCol(newCol);
    double newY = grid.getYFromRow(newRow);
    placementHandler.moveEntity(placement, newX, newY); // callback to controller
  }

  public boolean isCellAvailable(int row, int col, EntityPlacement ignore) {
    return grid.isValidCell(row, col) &&
        (gridEntities[row][col] == null || gridEntities[row][col] == ignore);
  }

  public Map<ImageView, EntityPlacement> getEntityViews() {
    return entityViews;
  }

  public EntityPlacement[][] getGridEntities() {
    return gridEntities;
  }

  public void removeEntity(EntityPlacement placement) {
    ImageView toRemove = entityViews.entrySet().stream()
        .filter(e -> e.getValue() == placement)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);

    if (toRemove != null) {
      root.getChildren().remove(toRemove);
      entityViews.remove(toRemove);

      int row = grid.getRowFromY(placement.getY());
      int col = grid.getColFromX(placement.getX());
      if (grid.isValidCell(row, col)) {
        gridEntities[row][col] = null;
      }
    }
  }


  @FunctionalInterface
  public interface PlacementHandler {
    void moveEntity(EntityPlacement placement, double newX, double newY);
  }
}
