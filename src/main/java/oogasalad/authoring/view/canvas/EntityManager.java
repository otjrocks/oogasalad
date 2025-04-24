package oogasalad.authoring.view.canvas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import oogasalad.authoring.view.util.SpriteSheetUtil;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.ModeConfigRecord;

/**
 * Manages visual representation and placement of entities on the canvas grid. Responsible for
 * adding, updating, and removing ImageViews tied to entity data. Also connects drag interaction
 * events to controller logic.
 *
 * @author Will He
 */
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
   * Constructs an EntityManager for a canvas grid.
   *
   * @param root             the JavaFX Pane to attach visuals to
   * @param grid             the CanvasGrid providing layout information
   * @param placementHandler callback to notify controller when an entity is moved
   * @param onPressed        mouse press handler for entity interaction
   * @param onDragged        mouse drag handler for entity interaction
   * @param onReleased       mouse release handler for entity interaction
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

  /**
   * Clears all visual entities and resets the backing grid.
   */
  public void clear() {
    root.getChildren().removeIf(n -> n instanceof ImageView && entityViews.containsKey(n));

    entityViews.clear();
    gridEntities = new EntityPlacement[grid.getRows()][grid.getCols()];
  }

  /**
   * Reloads all entities from a list of placements. Clears and repopulates visuals.
   *
   * @param placements the list of entity placements to render
   */
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

  /**
   * Adds a new entity visual to the canvas and tracks it in the entity grid.
   *
   * @param placement the entity to visualize and track
   */
  public void addEntityVisual(EntityPlacement placement) {
    int row = grid.getRowFromY(placement.getY());
    int col = grid.getColFromX(placement.getX());

    placement.setInitialTileX(col);
    placement.setInitialTileY(row);

    double x = grid.getXFromCol(col);
    double y = grid.getYFromRow(row);

    // Get full spritesheet image and extract first tile
    ModeConfigRecord modeConfig = placement.getType().modes().get(placement.getMode());
    WritableImage previewTile = SpriteSheetUtil.getPreviewTile(modeConfig);
    ImageView imageView = new ImageView(previewTile);

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


  /**
   * Updates an entity's position in the grid and notifies the controller.
   *
   * @param placement the entity to move
   * @param newRow    new row position
   * @param newCol    new column position
   */
  public void updateEntityPosition(EntityPlacement placement, int newRow, int newCol) {
    int oldRow = grid.getRowFromY(placement.getY());
    int oldCol = grid.getColFromX(placement.getX());

    gridEntities[oldRow][oldCol] = null;
    gridEntities[newRow][newCol] = placement;

    placement.setInitialTileX(newCol);
    placement.setInitialTileY(newRow);

    double newX = grid.getXFromCol(newCol);
    double newY = grid.getYFromRow(newRow);
    placementHandler.moveEntity(placement, newX, newY);
  }

  /**
   * Checks if a cell is valid and unoccupied, optionally ignoring a specific placement.
   *
   * @param row    the row to check
   * @param col    the column to check
   * @param ignore an entity placement to ignore (for dragging/moving)
   * @return true if the cell is available, false otherwise
   */
  public boolean isCellAvailable(int row, int col, EntityPlacement ignore) {
    return grid.isValidCell(row, col) &&
        (gridEntities[row][col] == null || gridEntities[row][col] == ignore);
  }

  /**
   * Returns a mapping from ImageView to their corresponding entity placements.
   *
   * @return a map of visual nodes and their linked data
   */
  public Map<ImageView, EntityPlacement> getEntityViews() {
    return entityViews;
  }


  /**
   * Removes a visual entity from the canvas and the grid.
   *
   * @param placement the entity to remove
   */
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

  /**
   * Retrieves a list of {@link EntityPlacement}s that belong to the specified entity type. This is
   * used to identify all placements of a particular EntityType currently visible on the canvas.
   *
   * @param entityTypeName the name of the entity type to filter by
   * @return a list of matching {@link EntityPlacement} instances
   */
  public List<EntityPlacement> getPlacementsForEntityType(String entityTypeName) {
    return entityViews.values().stream()
        .filter(p -> p.getType().type().equals(entityTypeName))
        .toList();
  }


  /**
   * Callback interface for updating entity positions.
   */
  @FunctionalInterface
  public interface PlacementHandler {

    /**
     * Called when an entity is moved to a new position.
     *
     * @param placement the moved entity
     * @param newX      new x-coordinate
     * @param newY      new y-coordinate
     */
    void moveEntity(EntityPlacement placement, double newX, double newY);
  }
}
