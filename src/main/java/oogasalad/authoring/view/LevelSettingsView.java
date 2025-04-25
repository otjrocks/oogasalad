package oogasalad.authoring.view;

import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import oogasalad.authoring.controller.LevelController;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.utility.LanguageManager;

/**
 * View component that allows users to edit level settings such as width and height. Provides a
 * simple interface with spinners and a button to apply changes.
 *
 * @author Will He
 */
public class LevelSettingsView {

  private final VBox root;
  private final Spinner<Integer> widthSpinner;
  private final Spinner<Integer> heightSpinner;
  private final LevelController controller;
  private final FileChooser myFileChooser;
  private File mySelectedFile;

  /**
   * Constructs a LevelSettingsView for the given level controller.
   *
   * @param controller Controller for accessing and updating level state
   */
  public LevelSettingsView(LevelController controller) {
    this.controller = controller;
    this.root = new VBox(10);
    this.root.setPadding(new Insets(10));

    myFileChooser = new FileChooser();
    widthSpinner = new Spinner<>(5, 100, controller.getCurrentLevel().getWidth());
    heightSpinner = new Spinner<>(5, 100, controller.getCurrentLevel().getHeight());

    initializeFileChooser();
    initializeView(controller);
  }

  private void initializeFileChooser() {
    myFileChooser.setTitle(LanguageManager.getMessage("CHOOSE_BACKGROUND_IMAGE"));
    myFileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter(LanguageManager.getMessage("IMAGE_FILES"), "*.png", "*.jpg",
            "*.jpeg", "*.gif")
    );
  }

  private void initializeView(LevelController controller) {
    Label titleLabel = new Label(LanguageManager.getMessage("LEVEL_SETTINGS"));
    titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

    Button saveButton = new Button(LanguageManager.getMessage("APPLY_LEVEL_SETTINGS"));
    saveButton.setOnAction(e -> applyChanges());

    Button chooseImageButton = new Button(LanguageManager.getMessage("CHOOSE_BACKGROUND_IMAGE"));
    chooseImageButton.setOnAction(e -> {
      mySelectedFile = myFileChooser.showOpenDialog(root.getScene().getWindow());
      LevelDraft level = controller.getCurrentLevel();
      level.setBackgroundImage(mySelectedFile);
    });

    Button editModeEventsButton = new Button(LanguageManager.getMessage("EDIT_MODE_CHANGE_EVENTS"));
    editModeEventsButton.setOnAction(e -> {
      ModeChangeEventDialog dialog = new ModeChangeEventDialog(
          controller.getAvailableEntityTypes(),
          controller.getCurrentLevel()
      );
      dialog.showAndWait();
    });

    Button editSpawnEventsButton = new Button(LanguageManager.getMessage("EDIT_SPAWN_EVENTS"));
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
    grid.add(new Label(LanguageManager.getMessage("WIDTH")), 0, 0);
    grid.add(widthSpinner, 1, 0);
    grid.add(new Label(LanguageManager.getMessage("HEIGHT")), 0, 1);
    grid.add(heightSpinner, 1, 1);

    root.getChildren()
        .addAll(titleLabel, grid, chooseImageButton, saveButton,
            editModeEventsButton,
            editSpawnEventsButton);
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
