package oogasalad.authoring.view;

import java.util.List;
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
  private double lastMouseSceneX, lastMouseSceneY;
  private boolean hasMoved = false;


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

  void handleDragDropped(DragEvent e) {
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

  boolean isValidCell(int row, int col) {
    return row >= 0 && row < ROWS && col >= 0 && col < COLS;
  }

  private void setupEntityMouseHandlers(ImageView imageView) {
    imageView.setOnMousePressed(this::handleEntityMousePressed);
    imageView.setOnMouseDragged(this::handleEntityMouseDragged);
    imageView.setOnMouseReleased(this::handleEntityMouseReleased);

    imageView.setPickOnBounds(true);
    imageView.setMouseTransparent(false);
  }

  void handleEntityMousePressed(MouseEvent e) {
    if (e == null || !(e.getSource() instanceof ImageView clickedImageView)) {
      return;
    }

    EntityPlacement clickedEntity = entityViews.get(clickedImageView);

    selectedEntity = clickedEntity;
    selectedImageView = clickedImageView;

    // Store original grid position
    origRow = (int)(clickedEntity.getY() / TILE_SIZE);
    origCol = (int)(clickedEntity.getX() / TILE_SIZE);

    // Store the initial mouse position in scene coordinates
    lastMouseSceneX = e.getSceneX();
    lastMouseSceneY = e.getSceneY();

    // Reset the movement flag
    hasMoved = false;

    // Show selection highlight at current position
    selectionHighlight.setX(clickedImageView.getX());
    selectionHighlight.setY(clickedImageView.getY());
    selectionHighlight.setVisible(true);

    // Bring the selected entity and highlight to front
    selectedImageView.toFront();
    selectionHighlight.toFront();

    controller.selectEntityPlacement(clickedEntity);
    e.consume();

  }

  void handleEntityMouseDragged(MouseEvent e) {
    if (selectedEntity == null || selectedImageView == null) {
      return;
    }

    // Calculate delta movement from last mouse position
    double deltaX = e.getSceneX() - lastMouseSceneX;
    double deltaY = e.getSceneY() - lastMouseSceneY;

    // Update the last known mouse position
    lastMouseSceneX = e.getSceneX();
    lastMouseSceneY = e.getSceneY();

    // Only move if there's significant delta to avoid unintended movement
    if (Math.abs(deltaX) < 1 && Math.abs(deltaY) < 1 && !hasMoved) {
      return;
    }

    hasMoved = true;

    // Get current position
    double currentX = selectedImageView.getX();
    double currentY = selectedImageView.getY();

    // Calculate new position by adding delta
    double newX = currentX + deltaX;
    double newY = currentY + deltaY;

    // Snap to grid
    int col = Math.max(0, Math.min(COLS - 1, (int)Math.round(newX / TILE_SIZE)));
    int row = Math.max(0, Math.min(ROWS - 1, (int)Math.round(newY / TILE_SIZE)));

    double snappedX = col * TILE_SIZE;
    double snappedY = row * TILE_SIZE;

    // Check if the target cell is available or it's the original cell
    boolean isCellAvailable = !isValidCell(row, col) ||
            gridEntities[row][col] == null ||
            gridEntities[row][col] == selectedEntity;

    if (isCellAvailable) {
      // Update visual position
      selectedImageView.setX(snappedX);
      selectedImageView.setY(snappedY);
      selectionHighlight.setX(snappedX);
      selectionHighlight.setY(snappedY);
    }

    e.consume();
  }

  /**
   * Handles the mouse release event for dragged entities.
   * Simply processes event and delegates to appropriate helper methods.
   */
  void handleEntityMouseReleased(MouseEvent e) {
    // Return early if no entity is selected
    if (selectedEntity == null) {
      e.consume();
      return;
    }

    // Process drag if movement occurred
    if (hasMoved) {
      finalizeEntityPosition();
    }

    // Reset tracking variables
    resetEntityTracking();
    e.consume();
  }

  /**
   * Reset all tracking variables related to entity dragging.
   */
  void resetEntityTracking() {
    selectedEntity = null;
    selectedImageView = null;
    hasMoved = false;
  }

  void finalizeEntityPosition() {
    int newCol = (int)(selectedImageView.getX() / TILE_SIZE);
    int newRow = (int)(selectedImageView.getY() / TILE_SIZE);

    boolean validNewPosition = isValidCell(newRow, newCol) &&
            (gridEntities[newRow][newCol] == null || gridEntities[newRow][newCol] == selectedEntity);
    boolean positionChanged = (newRow != origRow || newCol != origCol);

    if (validNewPosition && positionChanged) {
      updateEntityPosition(newRow, newCol);
    } else if (!validNewPosition) {
      revertToOriginalPosition();
    }
  }

  private void updateEntityPosition(int newRow, int newCol) {
    // Update the grid references
    gridEntities[origRow][origCol] = null;
    gridEntities[newRow][newCol] = selectedEntity;

    // Calculate pixel coordinates
    double newX = newCol * TILE_SIZE;
    double newY = newRow * TILE_SIZE;

    // Update the model
    controller.moveEntity(selectedEntity, newX, newY);
  }

  private void revertToOriginalPosition() {
    // If invalid position, revert to original
    selectedImageView.setX(origCol * TILE_SIZE);
    selectedImageView.setY(origRow * TILE_SIZE);
    selectionHighlight.setX(origCol * TILE_SIZE);
    selectionHighlight.setY(origRow * TILE_SIZE);
  }

  /**
   * Reload all visuals (e.g., after editing types or modes).
   */
  public void reloadFromPlacements(List<EntityPlacement> placements) {
    this.getChildren().clear();

    // Add visuals back
    for (EntityPlacement placement : placements) {
      addEntityVisual(placement);
    }

    // Restore hover highlight after clearing
    this.getChildren().add(hoverHighlight);
  }

  /**
   * Gets the image path for this entity.
   * This is a temporary method until the Mode class is fully implemented.
   *
   * @return the path to the entity's image
   */
  public String getEntityImagePath() {
    return "default-entity.png";
  }

  /**
   * Show a new image on canvas for an EntityPlacement.
   */
  public void addEntityVisual(EntityPlacement placement) {
    String imagePath = placement.getEntityImagePath();

    ImageView imageView = createImageViewForEntity(imagePath, placement);
    this.getChildren().add(imageView);

    int row = (int)(placement.getY() / TILE_SIZE);
    int col = (int)(placement.getX() / TILE_SIZE);
    gridEntities[row][col] = placement;

    entityViews.put(imageView, placement);
    setupEntityMouseHandlers(imageView);
  }

  /**
   * Creates an ImageView for the entity. Extracted to a method to allow for mocking in tests.
   *
   * @param imagePath the path to the image
   * @param placement the entity placement
   * @return the created ImageView
   */
  protected ImageView createImageViewForEntity(String imagePath, EntityPlacement placement) {
    ImageView imageView = new ImageView(new Image(imagePath));
    imageView.setX(placement.getX());
    imageView.setY(placement.getY());
    imageView.setFitWidth(32);
    imageView.setFitHeight(32);
    return imageView;
  }

  /**
   * Gets whether the selection highlight is currently visible.
   * For testing purposes only.
   *
   * @return true if the selection highlight is visible
   */
  public boolean getSelectionHighlightVisible() {
    return selectionHighlight.isVisible();
  }

  /**
   * Gets the X position of the selection highlight.
   * For testing purposes only.
   *
   * @return the X position of the selection highlight
   */
  public double getSelectionHighlightX() {
    return selectionHighlight.getX();
  }

  /**
   * Gets the Y position of the selection highlight.
   * For testing purposes only.
   *
   * @return the Y position of the selection highlight
   */
  public double getSelectionHighlightY() {
    return selectionHighlight.getY();
  }

  /**
   * Gets whether any entity is currently selected.
   * For testing purposes only.
   *
   * @return true if an entity is selected
   */
  public boolean hasSelectedEntity() {
    return selectedEntity != null;
  }



}
