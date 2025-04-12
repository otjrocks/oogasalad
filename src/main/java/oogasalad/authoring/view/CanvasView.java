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
 * @author Will He, Angela Predolac, Ishan Madan
 */
public class CanvasView {

  private static final int TILE_SIZE = 40;
  private static final int ROWS = 15;
  private static final int COLS = 20;
  private static final double DRAG_THRESHOLD = 5.0;

  private final Pane root;
  private final AuthoringController controller;
  private final Rectangle hoverHighlight = new Rectangle(TILE_SIZE, TILE_SIZE);
  private final Rectangle selectionHighlight = new Rectangle(TILE_SIZE, TILE_SIZE);
  private final EntityPlacement[][] gridEntities = new EntityPlacement[ROWS][COLS];
  private final Map<ImageView, EntityPlacement> entityViews = new HashMap<>();

  private boolean isDragging = false;
  private EntityPlacement selectedEntity = null;
  private ImageView selectedImageView = null;
  private int origRow, origCol;
  private double startDragX, startDragY;
  private double startImageX, startImageY;
  private boolean hasMoved = false;


  /**
   * Creates a canvasView object
   * @param controller wires up with model
   */
  public CanvasView(AuthoringController controller) {
    this.root = new Pane();
    this.controller = controller;
    root.setPrefSize(800, 600);
    root.getStyleClass().add("canvas-view");

    initializeHoverHighlight();
    initializeSelectionHighlight();
    setupDragAndDropHandlers();
  }
  
  /**
   * Returns the root JavaFX node for this view.
   *
   * @return the root node that can be added to a scene
   */
  public Pane getNode() {
    return root;
  }

  private void initializeHoverHighlight() {
    hoverHighlight.setFill(Color.TRANSPARENT);
    hoverHighlight.setStroke(Color.GRAY);
    hoverHighlight.setVisible(false);
    root.getChildren().add(hoverHighlight);
  }

  private void initializeSelectionHighlight() {
    selectionHighlight.setFill(Color.TRANSPARENT);
    selectionHighlight.setStroke(Color.BLUE);
    selectionHighlight.setStrokeWidth(2);
    selectionHighlight.setVisible(false);
    root.getChildren().add(selectionHighlight);
  }

  private void setupDragAndDropHandlers() {
    root.setOnDragOver(this::handleDragOver);
    root.setOnDragDropped(this::handleDragDropped);
    root.setOnDragEntered(this::handleDragEntered);
    root.setOnDragExited(this::handleDragExited);
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
    startDragX = e.getSceneX();
    startDragY = e.getSceneY();
    startImageX = clickedImageView.getX();
    startImageY = clickedImageView.getY();

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
    double deltaX = e.getSceneX() - startDragX;
    double deltaY = e.getSceneY() - startDragY;

    // Only initiate a drag if the movement exceeds our threshold
    if (!hasMoved && (Math.abs(deltaX) > DRAG_THRESHOLD || Math.abs(deltaY) > DRAG_THRESHOLD)) {
      hasMoved = true;
    }

    if (!hasMoved) {
      return; // Not yet considered a drag
    }

    // Calculate new position directly from the start position plus delta
    double newX = startImageX + deltaX;
    double newY = startImageY + deltaY;

    // Get grid cell
    int col = (int)Math.floor(newX / TILE_SIZE);
    int row = (int)Math.floor(newY / TILE_SIZE);

    // Ensure we stay within grid bounds
    col = Math.max(0, Math.min(COLS - 1, col));
    row = Math.max(0, Math.min(ROWS - 1, row));

    double snappedX = col * TILE_SIZE;
    double snappedY = row * TILE_SIZE;

    boolean isCellAvailable = gridEntities[row][col] == null ||
            gridEntities[row][col] == selectedEntity;

    if (isCellAvailable) {
      // Update visual position with snapped coordinates
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
    selectionHighlight.setVisible(false);
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
    root.getChildren().clear();

    for (int i = 0; i < ROWS; i++) {
      for (int j = 0; j < COLS; j++) {
        gridEntities[i][j] = null;
      }
    }
    entityViews.clear();

    // Add visuals back
    for (EntityPlacement placement : placements) {
      addEntityVisual(placement);
    }

    // Restore hover highlight after clearing
    root.getChildren().add(hoverHighlight);
    root.getChildren().add(selectionHighlight);
    selectionHighlight.setVisible(false);
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
    root.getChildren().add(imageView);

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
