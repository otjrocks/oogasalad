package oogasalad.authoring.view;

import java.net.URI;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import oogasalad.engine.config.ModeConfig;

import java.io.File;

/**
 * Dialog for creating a new ModeConfig with a user-uploaded image.
 * Returns a ModeConfig object via showAndWait().ifPresent(...)
 *
 * @author Will He
 */
public class AddModeDialog extends Dialog<ModeConfig> {

  private TextField nameField;
  private TextField speedField;
  private TextField imagePathField;
  private File selectedImageFile;
  private ModeConfig preparedResult;


  /**
   * Represents the dialog to add a new mode
   */
  public AddModeDialog() {
    this.setTitle("Add New Mode");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    nameField = new TextField();
    imagePathField = new TextField();
    imagePathField.setEditable(false);

    speedField = new TextField();

    Button uploadButton = new Button("Choose Image");
    uploadButton.setOnAction(e -> handleImageUpload());

    grid.add(new Label("Mode Name:"), 0, 0);
    grid.add(nameField, 1, 0);
    grid.add(new Label("Image Path:"), 0, 1);
    grid.add(imagePathField, 1, 1);
    grid.add(uploadButton, 2, 1);
    grid.add(new Label("Movement Speed:"), 0, 2);
    grid.add(speedField, 1, 2);


    ButtonType okButtonType = ButtonType.OK;

    this.getDialogPane().setContent(grid);
    this.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    Button okButton = (Button) this.getDialogPane().lookupButton(okButtonType);
    okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
      if (!validateAndPrepareResult()) {
        event.consume(); // prevent dialog from closing
      }
    });

    this.setResultConverter(button -> {
      if (button == okButtonType) {
        return preparedResult; // set during validation
      }
      return null;
    });
  }

  public AddModeDialog(ModeConfig existingConfig) {
    this(); // call default constructor
    if (existingConfig != null) {
      nameField.setText(existingConfig.getModeName());
      speedField.setText(String.valueOf(existingConfig.getMovementSpeed()));
      selectedImageFile = new File(URI.create(existingConfig.getImagePath()));
      imagePathField.setText(selectedImageFile.getName());
    }
  }

  private void handleImageUpload() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose Mode Image");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
    );

    File file = fileChooser.showOpenDialog(getOwnerWindow());
    if (file != null) {
      selectedImageFile = file;
      imagePathField.setText(file.getName()); // display filename only (or full path if you prefer)
    }
  }

  private Stage getOwnerWindow() {
    return (Stage) this.getDialogPane().getScene().getWindow();
  }

  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.initOwner(getOwnerWindow());
    alert.showAndWait();
  }

  private boolean validateAndPrepareResult() {
    try {
      if (selectedImageFile == null) {
        showError("You must choose an image.");
        return false;
      }

      int speed = Integer.parseInt(speedField.getText());
      String name = nameField.getText().trim();
      if (name.isEmpty()) {
        showError("Mode name cannot be empty.");
        return false;
      }

      ModeConfig config = new ModeConfig();
      config.setImagePath(selectedImageFile.toURI().toString());
      config.setMovementSpeed(speed);
      config.setModeName(name);
      preparedResult = config;
      return true;
    } catch (NumberFormatException ex) {
      showError("Invalid speed. Please enter a number.");
      return false;
    }
  }
}
