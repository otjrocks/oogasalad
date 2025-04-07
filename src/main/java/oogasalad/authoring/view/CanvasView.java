package oogasalad.authoring.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.model.EntityPlacement;

import java.util.HashMap;
import java.util.Map;

/**
 * Visual canvas where entity instances are placed via drag and drop.
 *
 * @author Will He, Angela Predolac
 */
public class CanvasView extends Pane {

  private static final int TILE_SIZE = 40;
  private static final int ROWS = 15;
  private static final int COLS = 20;

  private final AuthoringController controller;
  private final Rectangle hoverHighlight = new Rectangle(TILE_SIZE, TILE_SIZE);
  private final Rectangle selectionHighlight = new Rectangle(TILE_SIZE, TILE_SIZE);
  private final EntityPlacement[][] gridEntities = new EntityPlacement[ROWS][COLS];
  private final Map<ImageView, EntityPlacement> entityViews = new HashMap<>();

  private boolean isDragging = false;
  private EntityPlacement selectedEntity = null;
  private ImageView selectedImageView = null;
  private int origRow, origCol;
  private double mouseOffsetX, mouseOffsetY;

  /**
   * Creates a canvasView object
   * @param controller wires up with model
   */
  public CanvasView(AuthoringController controller) {
    this.controller = controller;
    this.setPrefSize(800, 600);
    this.getStyleClass().add("canvas-view");

    initializeHoverHighlight();
    initializeSelectionHighlight();
    setupDragAndDropHandlers();
  }

  private void initializeHoverHighlight() {
    hoverHighlight.setFill(Color.TRANSPARENT);
    hoverHighlight.setStroke(Color.GRAY);
    hoverHighlight.setVisible(false);
    this.getChildren().add(hoverHighlight);
  }

  private void initializeSelectionHighlight() {
    selectionHighlight.setFill(Color.TRANSPARENT);
    selectionHighlight.setStroke(Color.BLUE);
    selectionHighlight.setStrokeWidth(2);
    selectionHighlight.setVisible(false);
    this.getChildren().add(selectionHighlight);
  }

  private void setupDragAndDropHandlers() {
    this.setOnDragOver(this::handleDragOver);
    this.setOnDragDropped(this::handleDragDropped);
    this.setOnDragEntered(this::handleDragEntered);
    this.setOnDragExited(this::handleDragExited);
  }

  private void handleDragOver(DragEvent e) {
    if (!e.getDragboard().hasString()) return;

    e.acceptTransferModes(TransferMode.COPY);

    if (isDragging) {
      int col = (int)(e.getX() / TILE_SIZE);
      int row = (int)(e.getY() / TILE_SIZE);

      if (isValidCell(row, col)) {
        hoverHighlight.setX(col * TILE_SIZE);
        hoverHighlight.setY(row * TILE_SIZE);
        hoverHighlight.setVisible(true);
      } else {
        hoverHighlight.setVisible(false);
      }
    }
    e.consume();
  }

  private void handleDragDropped(DragEvent e) {
    Dragboard db = e.getDragboard();
    if (!db.hasString()) return;

    int col = (int)(e.getX() / TILE_SIZE);
    int row = (int)(e.getY() / TILE_SIZE);

    if (isValidCell(row, col)) {
      if (gridEntities[row][col] == null) {
        double snappedX = col * TILE_SIZE;
        double snappedY = row * TILE_SIZE;
        controller.placeEntity(db.getString(), snappedX, snappedY);
        e.setDropCompleted(true);
      } else {
        e.setDropCompleted(false); // tile already used
      }
    }

    hoverHighlight.setVisible(false);
    e.consume();
  }

  private void handleDragEntered(DragEvent e) {
    isDragging = true;
    hoverHighlight.setVisible(true);
    e.consume();
  }

  private void handleDragExited(DragEvent e) {
    isDragging = false;
    hoverHighlight.setVisible(false);
    e.consume();
  }

  private boolean isValidCell(int row, int col) {
    return row >= 0 && row < ROWS && col >= 0 && col < COLS;
  }

  /**
   * Show a new image on canvas for an EntityPlacement.
   */
  public void addEntityVisual(EntityPlacement placement) {
    String imagePath = placement.getType()
        .getModes()
        .get(placement.getMode())
        .getImagePath();

    ImageView imageView = new ImageView(new Image(imagePath));
    imageView.setX(placement.getX());
    imageView.setY(placement.getY());
    imageView.setFitWidth(32);
    imageView.setFitHeight(32);
    this.getChildren().add(imageView);

    int row = (int)(placement.getY() / TILE_SIZE);
    int col = (int)(placement.getX() / TILE_SIZE);
    gridEntities[row][col] = placement;

    entityViews.put(imageView, placement);
    setupEntityMouseHandlers(imageView);
  }

  private void setupEntityMouseHandlers(ImageView imageView) {
    imageView.setOnMousePressed(this::handleEntityMousePressed);
    imageView.setOnMouseDragged(this::handleEntityMouseDragged);
    imageView.setOnMouseReleased(this::handleEntityMouseReleased);

    imageView.setPickOnBounds(true);
    imageView.setMouseTransparent(false);
  }

  private void handleEntityMousePressed(MouseEvent e) {
    if (e == null || !(e.getSource() instanceof ImageView clickedImageView)) {
      return;
    }

    EntityPlacement clickedEntity = entityViews.get(clickedImageView);

    selectedEntity = clickedEntity;
    selectedImageView = clickedImageView;

    // Store original grid position
    origRow = (int)(clickedEntity.getY() / TILE_SIZE);
    origCol = (int)(clickedEntity.getX() / TILE_SIZE);

    // Calculate mouse offset within the entity for smooth dragging
    mouseOffsetX = e.getX();
    mouseOffsetY = e.getY();

    // Show selection highlight
    selectionHighlight.setX(clickedEntity.getX());
    selectionHighlight.setY(clickedEntity.getY());
    selectionHighlight.setVisible(true);

    // Bring the selected entity to front
    selectedImageView.toFront();
    selectionHighlight.toFront();

    e.consume();
  }

  private void handleEntityMouseDragged(MouseEvent e) {
    if (selectedEntity == null || selectedImageView == null) {
      return;
    }

    // Get the mouse position on the canvas
    double mouseX = e.getSceneX() - this.localToScene(0, 0).getX();
    double mouseY = e.getSceneY() - this.localToScene(0, 0).getY();

    // Calculate the entity position accounting for the initial click offset
    double entityX = mouseX - mouseOffsetX;
    double entityY = mouseY - mouseOffsetY;

    // Snap to grid
    int col = (int)(entityX / TILE_SIZE);
    int row = (int)(entityY / TILE_SIZE);

    // Ensure we're within grid bounds
    if (isValidCell(row, col)) {
      // Check if target cell is available or it's the original cell
      boolean isCellAvailable = gridEntities[row][col] == null ||
              (gridEntities[row][col] == selectedEntity);

      if (isCellAvailable) {
        double snappedX = col * TILE_SIZE;
        double snappedY = row * TILE_SIZE;

        selectedImageView.setX(snappedX);
        selectedImageView.setY(snappedY);
        selectionHighlight.setX(snappedX);
        selectionHighlight.setY(snappedY);
      }
    }

    e.consume();
  }

  /**
   * Updates the visual position of the selected entity during drag if the target cell is valid.
   *
   * @param row target grid row
   * @param col target grid column
   */
  private void updateEntityPositionDuringDrag(int row, int col) {
    if (!isValidCell(row, col)) {
      return;
    }

    boolean isCellAvailable = gridEntities[row][col] == null ||
            (row == origRow && col == origCol);

    if (isCellAvailable) {
      double snappedX = col * TILE_SIZE;
      double snappedY = row * TILE_SIZE;

      selectedImageView.setX(snappedX);
      selectedImageView.setY(snappedY);
      selectionHighlight.setX(snappedX);
      selectionHighlight.setY(snappedY);
    }
  }

  private void handleEntityMouseReleased(MouseEvent e) {
    if (selectedEntity == null || selectedImageView == null) {
      return;
    }

    // Get the final grid position
    int newCol = (int)(selectedImageView.getX() / TILE_SIZE);
    int newRow = (int)(selectedImageView.getY() / TILE_SIZE);

    // Check if position is valid
    boolean validNewPosition = isValidCell(newRow, newCol) &&
            (gridEntities[newRow][newCol] == null || gridEntities[newRow][newCol] == selectedEntity);

    // If the new position is valid and different from the original
    if (validNewPosition && (newRow != origRow || newCol != origCol)) {
      // Update the grid references
      gridEntities[origRow][origCol] = null;
      gridEntities[newRow][newCol] = selectedEntity;

      // Calculate pixel coordinates
      double newX = newCol * TILE_SIZE;
      double newY = newRow * TILE_SIZE;

      // Update the model
      controller.moveEntity(selectedEntity, newX, newY);
    } else if (!validNewPosition) {
      // If invalid position, revert to original
      selectedImageView.setX(origCol * TILE_SIZE);
      selectedImageView.setY(origRow * TILE_SIZE);
      selectionHighlight.setX(origCol * TILE_SIZE);
      selectionHighlight.setY(origRow * TILE_SIZE);
    }

    // Reset selected state but keep highlight visible to show current selection
    selectedEntity = null;
    selectedImageView = null;

    e.consume();
  }


  /**
   * Reload all visuals (e.g., after editing types or modes).
   */
  public void reloadFromPlacements(java.util.List<EntityPlacement> placements) {
    this.getChildren().clear();

    // Add visuals back
    for (EntityPlacement placement : placements) {
      addEntityVisual(placement);
    }

    // Restore hover highlight after clearing
    this.getChildren().add(hoverHighlight);
  }


}
