package oogasalad.authoring.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.LevelController;
import oogasalad.authoring.model.LevelDraft;

/**
 * View component that allows users to edit level settings such as width and height.
 * Provides a simple interface with spinners and a button to apply changes.
 *
 * @author Will He
 */
public class LevelSettingsView {

  private final VBox root;
  private final Spinner<Integer> widthSpinner;
  private final Spinner<Integer> heightSpinner;
  private final LevelController controller;

  /**
   * Constructs a LevelSettingsView for the given level controller.
   *
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

    Button editModeEventsButton = new Button("Edit Mode Change Events");
    editModeEventsButton.setOnAction(e -> {
      ModeChangeEventDialog dialog = new ModeChangeEventDialog(
          controller.getAvailableEntityTypes(),
          controller.getCurrentLevel()
      );
      dialog.showAndWait();
    });

    Button editSpawnEventsButton = new Button("Edit Spawn Events");
    editSpawnEventsButton.setOnAction(e -> {
      SpawnEventDialog dialog = new SpawnEventDialog(
          controller.getAvailableEntityTypes(),
          controller.getCurrentLevel()
      );
      dialog.showAndWait();
    });

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.add(new Label("Width:"), 0, 0);
    grid.add(widthSpinner, 1, 0);
    grid.add(new Label("Height:"), 0, 1);
    grid.add(heightSpinner, 1, 1);

    root.getChildren().addAll(titleLabel, grid, saveButton, editModeEventsButton, editSpawnEventsButton);
  }

  /**
   * Applies the current spinner values to the level and updates the canvas size.
   */
  private void applyChanges() {
    LevelDraft level = controller.getCurrentLevel();
    int newWidth = widthSpinner.getValue();
    int newHeight = heightSpinner.getValue();
    level.setWidth(newWidth);
    level.setHeight(newHeight);
    controller.updateCanvasSize(newWidth, newHeight);
  }

  /**
   * Returns the JavaFX node representing this view.
   *
   * @return the root VBox node
   */
  public Node getNode() {
    return root;
  }


  /**
   * Refresh level settings view with values of spinners as well as canvas view
   */
  public void refresh() {
    LevelDraft level = controller.getCurrentLevel();
    widthSpinner.getValueFactory().setValue(level.getWidth());
    heightSpinner.getValueFactory().setValue(level.getHeight());
    controller.updateCanvasSize(widthSpinner.getValue(), heightSpinner.getValue());

  }

}
