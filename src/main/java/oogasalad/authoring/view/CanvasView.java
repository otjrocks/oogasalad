package oogasalad.authoring.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.model.EntityPlacement;

/**
 * Visual canvas where entity instances are placed via drag and drop.
 *
 * @author Will He
 */
public class CanvasView extends Pane {

  private static final int TILE_SIZE = 40;
  private static final int ROWS = 15;
  private static final int COLS = 20;

  private final AuthoringController controller;
  private final Rectangle hoverHighlight = new Rectangle(TILE_SIZE, TILE_SIZE);
  private final EntityPlacement[][] gridEntities = new EntityPlacement[ROWS][COLS];
  private boolean isDragging = false;

  /**
   * Creates a canvasView object
   * @param controller wires up with model
   */
  public CanvasView(AuthoringController controller) {
    this.controller = controller;
    this.setPrefSize(800, 600);
    this.getStyleClass().add("canvas-view");

    initializeHoverHighlight();
    setupDragAndDropHandlers();
  }

  private void initializeHoverHighlight() {
    hoverHighlight.setFill(Color.TRANSPARENT);
    hoverHighlight.setStroke(Color.GRAY);
    hoverHighlight.setVisible(false);
    this.getChildren().add(hoverHighlight);
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

  }

  /**
   * Reload all visuals (e.g., after editing types or modes).
   */
  public void reloadFromPlacements(java.util.List<EntityPlacement> placements) {
    this.getChildren().clear();
    for (EntityPlacement placement : placements) {
      addEntityVisual(placement);
    }
  }
}
