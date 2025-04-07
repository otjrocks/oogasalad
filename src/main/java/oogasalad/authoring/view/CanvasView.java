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
    if (selectedEntity != null && selectedImageView != null) {
      double offsetX = e.getSceneX() - dragStartX;
      double offsetY = e.getSceneY() - dragStartY;

      // Update the visual position during drag
      double newX = selectedEntity.getX() + offsetX;
      double newY = selectedEntity.getY() + offsetY;

      // Snap to grid during drag
      int col = (int)(newX / TILE_SIZE);
      int row = (int)(newY / TILE_SIZE);

      if (isValidCell(row, col)) {
        double snappedX = col * TILE_SIZE;
        double snappedY = row * TILE_SIZE;

        // Only update visuals if the cell is empty or it's the original cell
        if (gridEntities[row][col] == null || (row == origRow && col == origCol)) {
          selectedImageView.setX(snappedX);
          selectedImageView.setY(snappedY);
          selectionHighlight.setX(snappedX);
          selectionHighlight.setY(snappedY);
        }
      }

      // Update drag start for next event
      dragStartX = e.getSceneX();
      dragStartY = e.getSceneY();

      e.consume();
    }
  }

  private void handleEntityMouseReleased(MouseEvent e) {
    if (selectedEntity != null && selectedImageView != null) {
      // Get the final grid position
      int newCol = (int)(selectedImageView.getX() / TILE_SIZE);
      int newRow = (int)(selectedImageView.getY() / TILE_SIZE);

      // Only move if the position is valid and either empty or the original position
      if (isValidCell(newRow, newCol) &&
              (gridEntities[newRow][newCol] == null || (newRow == origRow && newCol == origCol))) {

        // If actually moved to a new position
        if (newRow != origRow || newCol != origCol) {
          // Update grid tracking
          gridEntities[origRow][origCol] = null;
          gridEntities[newRow][newCol] = selectedEntity;

          // Get the snapped coordinates
          double snappedX = newCol * TILE_SIZE;
          double snappedY = newRow * TILE_SIZE;

          // Update the model through controller
          controller.moveEntity(selectedEntity, snappedX, snappedY);
        }
      } else {
        // Invalid move, return to original position
        selectedImageView.setX(origCol * TILE_SIZE);
        selectedImageView.setY(origRow * TILE_SIZE);
      }

      // Reset selection
      selectionHighlight.setVisible(false);
      selectedEntity = null;
      selectedImageView = null;

      e.consume();
    }
  }

  /**
   * Reload all visuals (e.g., after editing types or modes).
   */
  public void reloadFromPlacements(java.util.List<EntityPlacement> placements) {
    this.getChildren().clear();
    for (int r = 0; r < ROWS; r++) {
      for (int c = 0; c < COLS; c++) {
        gridEntities[r][c] = null;
      }
    }
    entityViews.clear();
    initializeHoverHighlight();
    initializeSelectionHighlight();
    for (EntityPlacement placement : placements) {
      addEntityVisual(placement);
    }
  }
}
