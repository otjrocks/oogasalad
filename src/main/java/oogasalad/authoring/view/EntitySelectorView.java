package oogasalad.authoring.view;
import oogasalad.engine.model.EntityData;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class EntitySelectorView extends VBox {
    private GridPane entityGrid;
    private ScrollPane scrollPane;

    public EntitySelectorView() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        // Create a grid to display entity data
        entityGrid = new GridPane();
        entityGrid.setHgap(10);
        entityGrid.setVgap(10);
        entityGrid.setPadding(new Insets(10));

        // Create a scroll pane to contain the grid
        scrollPane = new ScrollPane(entityGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    private void setupLayout() {
        getChildren().add(new Label("Available Entities"));
        getChildren().add(scrollPane);
        setSpacing(10);
        setPadding(new Insets(10));
    }

    public void loadEntityData(List<EntityData> entities) {
        entityGrid.getChildren().clear();

        // Populate grid with entity data
        for (int i = 0; i < entities.size(); i++) {
            EntityData entity = entities.get(i);
            VBox entityBox = createEntityDataBox(entity);

            int row = i / 3;
            int col = i % 3;

            entityGrid.add(entityBox, col, row);
        }
    }

    private VBox createEntityDataBox(EntityData entity) {
        VBox entityBox = new VBox(5);
        entityBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px; -fx-padding: 5px;");

        ImageView imageView = new ImageView();
        try {
            Image image = new Image(entity.getImagePath());
            imageView.setImage(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            imageView.setImage(null);
        }

        // Create labels for entity details
        Label typeLabel = new Label("Type: " + entity.getType());
        Label controlLabel = new Label("Control: " + entity.getControlType());
        Label effectLabel = new Label("Effect: " + entity.getEffect());

        entityBox.getChildren().addAll(imageView, typeLabel, controlLabel, effectLabel);

        setupDragAndDrop(entityBox, entity);

        return entityBox;
    }

    private void setupDragAndDrop(VBox entityBox, EntityData entity) {
        entityBox.setOnDragDetected(event -> {
            entityBox.startFullDrag();
        });
    }
}