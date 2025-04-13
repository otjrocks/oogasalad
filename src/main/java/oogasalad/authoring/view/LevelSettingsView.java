package oogasalad.authoring.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import oogasalad.authoring.controller.LevelController;
import oogasalad.authoring.model.LevelDraft;

public class LevelSettingsView {

  private final VBox root;
  private final Spinner<Integer> widthSpinner;
  private final Spinner<Integer> heightSpinner;
  private final LevelController controller;

  /**
   * Creates a view for editing level settings like width and height.
   * @param controller Controller for accessing and updating level state
   */
  public LevelSettingsView(LevelController controller) {
    this.controller = controller;
    this.root = new VBox(10);
    this.root.setPadding(new Insets(10));

    Label titleLabel = new Label("Level Settings");
    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

    widthSpinner = new Spinner<>(5, 100, controller.getCurrentLevel().getWidth());
    heightSpinner = new Spinner<>(5, 100, controller.getCurrentLevel().getHeight());

    Button saveButton = new Button("Apply Size");
    saveButton.setOnAction(e -> applyChanges());

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.add(new Label("Width:"), 0, 0);
    grid.add(widthSpinner, 1, 0);
    grid.add(new Label("Height:"), 0, 1);
    grid.add(heightSpinner, 1, 1);

    root.getChildren().addAll(titleLabel, grid, saveButton);
  }

  private void applyChanges() {
    LevelDraft level = controller.getCurrentLevel();
    int newWidth = widthSpinner.getValue();
    int newHeight = heightSpinner.getValue();
    level.setWidth(newWidth);
    level.setHeight(newHeight);

    controller.updateCanvasSize(newWidth, newHeight); // You’ll implement this
  }

  public Node getNode() {
    return root;
  }
}
