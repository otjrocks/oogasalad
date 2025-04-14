package oogasalad.authoring.view.canvas;

import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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

  private static final double DRAG_THRESHOLD = 5.0;
  private static final int DEFAULT_ROWS = 15;
  private static final int DEFAULT_COLS = 20;

  private final Pane root;
  private final AuthoringController controller;
  private final CanvasGrid canvasGrid;
  private final EntityManager entityManager;
  private final TileHighlighter tileHighlighter;

  // Drag state
  private boolean isDragging = false;
  private boolean hasMoved = false;
  private double startDragX, startDragY;
  private double startImageX, startImageY;
  private int origRow, origCol;
  private EntityPlacement selectedEntity;
  private ImageView selectedImageView;

  public CanvasView(AuthoringController controller) {
    this.controller = controller;
    this.root = new Pane();
    root.setPrefSize(800, 600);
    root.getStyleClass().add("canvas-view");

    this.canvasGrid = new CanvasGrid(DEFAULT_ROWS, DEFAULT_COLS);
    this.entityManager = new EntityManager(
        root,
        canvasGrid,
        controller::moveEntity,
        this::handleEntityMousePressed,
        this::handleEntityMouseDragged,
        this::handleEntityMouseReleased
    );

    this.tileHighlighter = new TileHighlighter(root);

    resizeGrid(DEFAULT_COLS, DEFAULT_ROWS);


    setupDragAndDropHandlers();
  }

  public Pane getNode() {
    return root;
  }

  public void resizeGrid(int newCols, int newRows) {
    // Resize grid layout
    canvasGrid.resizeGrid(root.getPrefWidth(), root.getPrefHeight(), newCols, newRows);

    // Clear and reset view
    root.getChildren().clear();
    entityManager.clear();

    // Resize and re-add highlights
    tileHighlighter.resizeTo(canvasGrid.getTileWidth(), canvasGrid.getTileHeight());
    root.getChildren().addAll(tileHighlighter.getHoverRectangle(), tileHighlighter.getSelectionRectangle());

    // Reattach event handlers
    setupDragAndDropHandlers();

    // Reload visuals
    reloadFromPlacements(controller.getModel().getCurrentLevel().getEntityPlacements());

  }


  public void reloadFromPlacements(List<EntityPlacement> placements) {
    entityManager.reloadEntities(placements);
    tileHighlighter.hideSelection();
    tileHighlighter.hideHover();
  }

  private void setupDragAndDropHandlers() {
    root.setOnDragOver(this::handleDragOver);
    root.setOnDragDropped(this::handleDragDropped);
    root.setOnDragEntered(e -> {
      isDragging = true;
      tileHighlighter.showHover();
      e.consume();
    });
    root.setOnDragExited(e -> {
      isDragging = false;
      tileHighlighter.hideHover();
      e.consume();
    });
  }

  private void handleDragOver(DragEvent e) {
    if (!e.getDragboard().hasString()) return;
    e.acceptTransferModes(TransferMode.COPY);

    if (isDragging) {
      int col = canvasGrid.getColFromX(e.getX());
      int row = canvasGrid.getRowFromY(e.getY());

      if (canvasGrid.isValidCell(row, col)) {
        tileHighlighter.moveHoverTo(canvasGrid.getXFromCol(col), canvasGrid.getYFromRow(row));
        tileHighlighter.showHover();
      } else {
        tileHighlighter.hideHover();
      }
    }
    e.consume();
  }

  private void handleDragDropped(DragEvent e) {
    Dragboard db = e.getDragboard();
    if (!db.hasString()) return;

    int col = canvasGrid.getColFromX(e.getX());
    int row = canvasGrid.getRowFromY(e.getY());

    if (canvasGrid.isValidCell(row, col) &&
        entityManager.isCellAvailable(row, col, null)) {

      double snappedX = canvasGrid.getXFromCol(col);
      double snappedY = canvasGrid.getYFromRow(row);
      controller.placeEntity(db.getString(), snappedX, snappedY);
      e.setDropCompleted(true);
    } else {
      e.setDropCompleted(false);
    }

    tileHighlighter.hideHover();
    e.consume();
  }

  public void handleEntityMousePressed(MouseEvent e) {
    if (!(e.getSource() instanceof ImageView imageView)) return;

    Map<ImageView, EntityPlacement> entityViews = entityManager.getEntityViews();
    selectedImageView = imageView;
    selectedEntity = entityViews.get(imageView);

    origCol = canvasGrid.getColFromX(selectedEntity.getX());
    origRow = canvasGrid.getRowFromY(selectedEntity.getY());

    controller.selectEntityPlacement(selectedEntity);


    startDragX = e.getSceneX();
    startDragY = e.getSceneY();
    startImageX = imageView.getX();
    startImageY = imageView.getY();
    hasMoved = false;

    tileHighlighter.moveSelectionTo(imageView.getX(), imageView.getY());
    tileHighlighter.showSelection();
    imageView.toFront();
    e.consume();
  }

  public void handleEntityMouseDragged(MouseEvent e) {
    if (selectedImageView == null || selectedEntity == null) return;

    if (!hasMoved && exceededDragThreshold(e)) {
      hasMoved = true;
    }

    if (hasMoved) {
      int[] tile = calculateDraggedTile(e);
      int row = tile[0], col = tile[1];

      if (entityManager.isCellAvailable(row, col, selectedEntity)) {
        double x = canvasGrid.getXFromCol(col);
        double y = canvasGrid.getYFromRow(row);
        selectedImageView.setX(x);
        selectedImageView.setY(y);
        tileHighlighter.moveSelectionTo(x, y);
      }
    }

    e.consume();
  }

  public void handleEntityMouseReleased(MouseEvent e) {
    if (selectedEntity == null) {
      e.consume();
      return;
    }

    if (hasMoved) {
      int newCol = canvasGrid.getColFromX(selectedImageView.getX());
      int newRow = canvasGrid.getRowFromY(selectedImageView.getY());

      boolean valid = entityManager.isCellAvailable(newRow, newCol, selectedEntity);
      boolean moved = newRow != origRow || newCol != origCol;

      if (valid && moved) {
        entityManager.updateEntityPosition(selectedEntity, newRow, newCol);
      } else {
        double oldX = canvasGrid.getXFromCol(origCol);
        double oldY = canvasGrid.getYFromRow(origRow);
        selectedImageView.setX(oldX);
        selectedImageView.setY(oldY);
        tileHighlighter.moveSelectionTo(oldX, oldY);
      }
    }

    selectedImageView = null;
    selectedEntity = null;
    tileHighlighter.hideSelection();
    e.consume();
  }

  private boolean exceededDragThreshold(MouseEvent e) {
    double dx = e.getSceneX() - startDragX;
    double dy = e.getSceneY() - startDragY;
    return Math.abs(dx) > DRAG_THRESHOLD || Math.abs(dy) > DRAG_THRESHOLD;
  }

  private int[] calculateDraggedTile(MouseEvent e) {
    double dx = e.getSceneX() - startDragX;
    double dy = e.getSceneY() - startDragY;
    double newX = startImageX + dx;
    double newY = startImageY + dy;
    return new int[]{canvasGrid.getRowFromY(newY), canvasGrid.getColFromX(newX)};
  }


  public void placeEntity(EntityPlacement placement) {
    entityManager.addEntityVisual(placement);
  }

  public void removeEntityVisual(EntityPlacement placement) {
    entityManager.removeEntity(placement);

    if (selectedEntity == placement) {
      tileHighlighter.hideSelection();
      selectedEntity = null;
      selectedImageView = null;
    }
  }


}
