package oogasalad.authoring.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.ScrollPane;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.config.ModeConfig;

import java.util.List;
import java.util.Map;

/**
 * View displaying all defined EntityTypes in a draggable grid.
 * Clicking on a tile notifies the controller to open an editor.
 *
 * @author Will He
 */
public class EntitySelectorView extends VBox {

  private final FlowPane tileGrid;
  private final AuthoringController controller;
  private VBox currentlySelectedTile = null;

  /**
   * View for selecting EntityType to drag onto Canvas
   * @param controller Wiring with model
   */
  public EntitySelectorView(AuthoringController controller) {
    this.controller = controller;
    this.getStyleClass().add("entity-selector-view");
    this.setSpacing(10);
    this.setPadding(new Insets(10));

    // Button to add new entity types
    Button addButton = new Button("+ Add Entity Type");
    addButton.setOnAction(e -> controller.createNewEntityType());

    // Grid that holds entity tiles
    tileGrid = new FlowPane();
    tileGrid.setHgap(10);
    tileGrid.setVgap(10);
    tileGrid.setPrefWrapLength(250); // wraps based on width

    // Scrollable container for grid
    ScrollPane scrollPane = new ScrollPane(tileGrid);
    scrollPane.setFitToWidth(true);
    VBox.setVgrow(scrollPane, Priority.ALWAYS);

    this.getChildren().addAll(addButton, scrollPane);
  }

  /**
   * Refresh the grid of entity type tiles.
   *
   * @param entityTypes list of types to display
   */
  public void updateEntities(List<EntityType> entityTypes) {
    tileGrid.getChildren().clear();
    for (EntityType type : entityTypes) {
      tileGrid.getChildren().add(createEntityTile(type));
    }
  }

  private VBox createEntityTile(EntityType type) {
    VBox tile = new VBox();
    tile.setSpacing(4);
    tile.getStyleClass().add("entity-tile");

    // Use first mode (usually "Default") for preview image
    ImageView imageView = new ImageView();
    imageView.setFitWidth(48);
    imageView.setFitHeight(48);
    String imagePath = getDefaultModeImage(type);
    if (imagePath != null) {
      imageView.setImage(new Image(imagePath));
    }


    tile.getChildren().addAll(imageView);

    // Click to open in EntityEditor
    tile.setOnMouseClicked(e -> {
      controller.selectEntityType(type.getType());
      highlightSelectedTile(tile);
    });

    // Drag-and-drop support
    tile.setOnDragDetected(e -> {
      Dragboard db = tile.startDragAndDrop(TransferMode.COPY);
      @SuppressWarnings("PMD.LooseCoupling")
      ClipboardContent content = new ClipboardContent();
      content.putString(type.getType());
      db.setContent(content);
      e.consume();
    });

    return tile;
  }

  private String getDefaultModeImage(EntityType type) {
    Map<String, ModeConfig> modes = type.getModes();
    if (modes == null || modes.isEmpty()) return null;
    return modes.get("Default").getImagePath();
  }

  private void highlightSelectedTile(VBox tile) {
    if (currentlySelectedTile != null) {
      currentlySelectedTile.getStyleClass().remove("selected-tile");
    }
    tile.getStyleClass().add("selected-tile");
    currentlySelectedTile = tile;
  }

}
