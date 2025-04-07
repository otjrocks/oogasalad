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
  private double dragStartX, dragStartY;
  private int origRow, origCol;

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
  }

  private void handleEntityMousePressed(MouseEvent e) {
    if (e == null || !(e.getSource() instanceof ImageView clickedImageView)) {
      return;
    }

    EntityPlacement clickedEntity = entityViews.get(clickedImageView);

    if (clickedEntity == null) {
      return;
    }

    if (e.getSource() instanceof ImageView) {

        // Select this entity
        selectedEntity = clickedEntity;
        selectedImageView = clickedImageView;
        dragStartX = e.getSceneX();
        dragStartY = e.getSceneY();

        // Store original grid position
        origRow = (int)(clickedEntity.getY() / TILE_SIZE);
        origCol = (int)(clickedEntity.getX() / TILE_SIZE);

        // Show selection highlight
        selectionHighlight.setX(clickedEntity.getX());
        selectionHighlight.setY(clickedEntity.getY());
        selectionHighlight.setVisible(true);

        e.consume();
    }
  }

  private void handleEntityMouseDragged(MouseEvent e) {
    if (selectedEntity == null || selectedImageView == null) {
      return;
    }

    double offsetX = e.getSceneX() - dragStartX;
    double offsetY = e.getSceneY() - dragStartY;
    double newX = selectedEntity.getX() + offsetX;
    double newY = selectedEntity.getY() + offsetY;

    int col = (int)(newX / TILE_SIZE);
    int row = (int)(newY / TILE_SIZE);

    updateEntityPositionDuringDrag(row, col);

    dragStartX = e.getSceneX();
    dragStartY = e.getSceneY();

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

    int newCol = (int)(selectedImageView.getX() / TILE_SIZE);
    int newRow = (int)(selectedImageView.getY() / TILE_SIZE);

    finalizeEntityMovement(newRow, newCol);

    selectionHighlight.setVisible(false);
    selectedEntity = null;
    selectedImageView = null;

    e.consume();
  }

  /**
   * Finalizes the movement of an entity to its new position or reverts it to the original position
   * if the new position is invalid.
   *
   * @param newRow target grid row
   * @param newCol target grid column
   */
  private void finalizeEntityMovement(int newRow, int newCol) {
    // Check if movement is valid and actually changed position
    boolean isValidMove = isValidPosition(newRow, newCol) && hasPositionChanged(newRow, newCol);

    if (isValidMove) {
      applyValidMove(newRow, newCol);
      return;
    }

    // If position is invalid, revert to original position
    if (!isValidPosition(newRow, newCol)) {
      revertToOriginalPosition();
    }
  }

  /**
   * Checks if the given position is valid for entity placement.
   */
  private boolean isValidPosition(int row, int col) {
    if (!isValidCell(row, col)) {
      return false;
    }

    return gridEntities[row][col] == null || (row == origRow && col == origCol);
  }

  /**
   * Checks if the position has changed from the original.
   */
  private boolean hasPositionChanged(int row, int col) {
    return row != origRow || col != origCol;
  }

  /**
   * Applies a valid entity move to the new position.
   */
  private void applyValidMove(int newRow, int newCol) {
    // Update grid tracking
    gridEntities[origRow][origCol] = null;
    gridEntities[newRow][newCol] = selectedEntity;

    // Get the snapped coordinates
    double snappedX = newCol * TILE_SIZE;
    double snappedY = newRow * TILE_SIZE;

    // Update the model through controller
    controller.moveEntity(selectedEntity, snappedX, snappedY);
  }

  /**
   * Reverts entity position to its original location.
   */
  private void revertToOriginalPosition() {
    selectedImageView.setX(origCol * TILE_SIZE);
    selectedImageView.setY(origRow * TILE_SIZE);
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
