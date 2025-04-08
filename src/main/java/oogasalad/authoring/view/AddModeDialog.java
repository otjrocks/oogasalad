package oogasalad.authoring.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import oogasalad.engine.config.ModeConfig;

/**
 * Dialog for creating a new ModeConfig.
 * Returns a ModeConfig object via showAndWait().ifPresent(...)
 *
 * @author Will He
 */
public class AddModeDialog extends Dialog<ModeConfig> {

  /**
   * Represents the dialog to add a new mode
   */
  public AddModeDialog() {
    this.setTitle("Add New Mode");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField nameField = new TextField();
    TextField imagePathField = new TextField();
    TextField speedField = new TextField();

    grid.add(new Label("Mode Name:"), 0, 0);
    grid.add(nameField, 1, 0);
    grid.add(new Label("Image Path:"), 0, 1);
    grid.add(imagePathField, 1, 1);
    grid.add(new Label("Movement Speed:"), 0, 2);
    grid.add(speedField, 1, 2);

    this.getDialogPane().setContent(grid);
    this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    this.setResultConverter(button -> {
      if (button == ButtonType.OK) {
        try {
          ModeConfig config = new ModeConfig();
          config.setImagePath(imagePathField.getText());
          config.setMovementSpeed(Integer.parseInt(speedField.getText()));
          config.setModeName(nameField.getText());
          return config;
        } catch (NumberFormatException ex) {
          showError("Invalid speed. Please enter a number.");
          return null;
        }
      }
      return null;
    });
  }

  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.showAndWait();
  }
}
