package oogasalad.authoring.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.view.util.SpriteSheetUtil;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.records.model.EntityTypeRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View displaying all defined EntityTypes in a draggable grid. Clicking on a tile notifies the
 * controller to open an editor. Selected tile is visually highlighted.
 *
 * @author Will He, Ishan Madan
 */
public class EntitySelectorView {

  private final VBox root;
  private final FlowPane tileGrid;
  private final AuthoringController controller;
  private final Map<String, VBox> tileMap = new HashMap<>();

  private VBox currentlySelectedTile = null;

  /**
   * View for selecting EntityType to drag onto Canvas
   *
   * @param controller Wiring with model
   */
  public EntitySelectorView(AuthoringController controller) {
    this.controller = controller;

    root = new VBox();
    root.getStyleClass().add("entity-selector-view");
    root.setSpacing(10);
    root.setPadding(new Insets(10));

    // Button to add new entity types
    Button addButton = new Button(LanguageManager.getMessage("ADD_ENTITY"));
    addButton.setOnAction(e -> controller.createNewEntityType());

    // Grid that holds entity tiles
    tileGrid = new FlowPane();
    tileGrid.getStyleClass().add("flow-pane");
    tileGrid.setHgap(10);
    tileGrid.setVgap(10);
    tileGrid.setPrefWrapLength(1000);

    // Scrollable container for grid
    ScrollPane scrollPane = new ScrollPane(tileGrid);
    scrollPane.setFitToWidth(true);
    VBox.setVgrow(scrollPane, Priority.ALWAYS);

    root.getChildren().addAll(addButton, scrollPane);
  }

  /**
   * Returns the root JavaFX node of this view
   *
   * @return the root node
   */
  public Parent getRoot() {
    return root;
  }

  /**
   * Refresh the grid of entity type tiles.
   *
   * @param entityTypes list of types to display
   */
  public void updateEntities(List<EntityTypeRecord> entityTypes) {
    tileGrid.getChildren().clear();
    tileMap.clear();

    for (EntityTypeRecord type : entityTypes) {
      VBox tile = createEntityTile(type);
      tileGrid.getChildren().add(tile);
      tileMap.put(type.type(), tile);
    }
  }

  /**
   * Highlights the given tile visually and un-highlights the previous.
   *
   * @param tile the tile to highlight
   */
  private void highlightSelectedTile(VBox tile) {
    if (currentlySelectedTile != null) {
      currentlySelectedTile.getStyleClass().remove("selected-tile");
    }
    tile.getStyleClass().add("selected-tile");
    currentlySelectedTile = tile;
  }

  /**
   * Highlights the tile corresponding to the given entity type name.
   *
   * @param typeName name of the entity type to highlight
   */
  public void highlightEntityTile(String typeName) {
    VBox tile = tileMap.get(typeName);
    if (tile != null) {
      highlightSelectedTile(tile);
    }
  }

  /**
   * Builds a draggable, clickable tile for a single EntityType.
   *
   * @param type the entity type
   * @return a visual tile node
   */
  private VBox createEntityTile(EntityTypeRecord type) {
    VBox tile = new VBox();
    tile.setSpacing(4);
    tile.getStyleClass().add("entity-tile");

    ImageView imageView = new ImageView();
    imageView.setFitWidth(48);
    imageView.setFitHeight(48);

    // Get preview from the sprite sheet
    String DEFAULT_MODE = "Default";
    if (type.modes().containsKey(DEFAULT_MODE)) {
      imageView.setImage(SpriteSheetUtil.getPreviewTile(type.modes().get(DEFAULT_MODE)));
    }

    tile.getChildren().add(imageView);

    // Click to open in EntityEditor
    tile.setOnMouseClicked(e -> controller.selectEntityType(type.type()));

    // Drag-and-drop support
    tile.setOnDragDetected(e -> {
      Dragboard db = tile.startDragAndDrop(TransferMode.COPY);
      @SuppressWarnings("PMD.LooseCoupling")
      ClipboardContent content = new ClipboardContent();
      content.putString(type.type());
      db.setContent(content);
      content.putString(type.type());
      db.setContent(content);
      e.consume();
    });

    return tile;
  }
}
