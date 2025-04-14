package oogasalad.authoring.view;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.records.newconfig.ImageConfig;
import oogasalad.engine.config.ModeConfig;
import oogasalad.engine.records.newconfig.model.ControlType;
import oogasalad.engine.records.newconfig.model.ControlTypeConfig;
import oogasalad.engine.records.newconfig.model.EntityProperties;

/**
 * Dialog for creating a new ModeConfig with a user-uploaded image.
 * Returns a ModeConfig object via showAndWait().ifPresent(...)
 *
 * @author Will He, Ishan Madan
 */
public class ModeEditorDialog {

  private TextField nameField;
  private TextField speedField;
  private TextField imagePathField;
  private File selectedImageFile;
  private ModeConfig preparedResult;

  private final Dialog<ModeConfig> dialog;


  /**
   * Represents the dialog to add a new mode
   */
  public ModeEditorDialog() {
    // Create dialog instance
    dialog = new Dialog<>();
    dialog.setTitle(LanguageManager.getMessage("MODE_EDITOR_TITLE"));

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    nameField = new TextField();
    imagePathField = new TextField();
    imagePathField.setEditable(false);

    speedField = new TextField();

    Button uploadButton = new Button(LanguageManager.getMessage("CHOOSE_IMAGE"));
    uploadButton.setOnAction(e -> handleImageUpload());

    grid.add(new Label(LanguageManager.getMessage("MODE_NAME")), 0, 0);
    grid.add(nameField, 1, 0);
    grid.add(new Label(LanguageManager.getMessage("IMAGE_PATH")), 0, 1);
    grid.add(imagePathField, 1, 1);
    grid.add(uploadButton, 2, 1);
    grid.add(new Label(LanguageManager.getMessage("MOVEMENT_SPEED")), 0, 2);
    grid.add(speedField, 1, 2);


    ButtonType okButtonType = ButtonType.OK;

    dialog.getDialogPane().setContent(grid);
    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
    okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
      if (!validateAndPrepareResult()) {
        event.consume(); // prevent dialog from closing
      }
    });

    dialog.setResultConverter(button -> {
      if (button == okButtonType) {
        return preparedResult; // set during validation
      }
      return null;
    });
  }

  /**
   * Returns the Mode Config Dialog.
   *
   * @return the mode config dialog
   */
  public Dialog<ModeConfig> getDialog() {
    return dialog;
  }

  /**
   * Shows the dialog and waits for user input.
   *
   * @return Optional containing the list of collision rules if OK was pressed, empty Optional otherwise
   */
  public Optional<ModeConfig> showAndWait() {
    return dialog.showAndWait();
  }

  /**
   * Constructor for editing an existing mode
   * @param existingConfig already existing mode config
   */
  public ModeEditorDialog(ModeConfig existingConfig) {
    this(); // call default constructor
    if (existingConfig != null) {
      nameField.setText(existingConfig.name());
      speedField.setText(String.valueOf(existingConfig.entityProperties().movementSpeed()));
      selectedImageFile = new File(URI.create(existingConfig.image().imagePath()));
      imagePathField.setText(selectedImageFile.getName());
    }
  }

  private void handleImageUpload() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(LanguageManager.getMessage("CHOOSE_IMAGE"));
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter(LanguageManager.getMessage("IMAGE_FILES"), "*.png", "*.jpg", "*.jpeg", "*.gif")
    );

    File file = fileChooser.showOpenDialog(getOwnerWindow());
    if (file != null) {
      selectedImageFile = file;
      imagePathField.setText(file.getName()); // display filename only (or full path if you prefer)
    }
  }

  private Stage getOwnerWindow() {
    return (Stage) dialog.getDialogPane().getScene().getWindow();
  }

  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.initOwner(getOwnerWindow());
    alert.showAndWait();
  }

  private boolean validateAndPrepareResult() {
    if (selectedImageFile == null) {
      showError(LanguageManager.getMessage("MUST_SELECT_IMAGE"));
      return false;
    }

    String name = nameField.getText().trim();
    if (name.isEmpty()) {
      showError(LanguageManager.getMessage("MUST_ENTER_MODE"));
      return false;
    }

    int speed;
    try {
      speed = Integer.parseInt(speedField.getText());
    } catch (NumberFormatException ex) {
      showError(LanguageManager.getMessage("INVALID_SPEED"));
      return false;
    }

    // === Build ImageConfig ===
    String imagePath = selectedImageFile.toURI().toString();
    ImageConfig imageConfig = new ImageConfig(
        imagePath,
        14,
        14,
        List.of(0, 1, 2, 3), // Default animation cycle
        1.0
    );

    // === Build EntityProperties ===
    ControlTypeConfig controlConfig = new ControlTypeConfig("None", 0);
    ControlType controlType = new ControlType("Keyboard", controlConfig);

    EntityProperties entityProps = new EntityProperties(
        name,
        controlType,
        (double) speed,
        List.of()
    );

    // === Build ModeConfig ===
    preparedResult = new ModeConfig(name, entityProps, imageConfig);
    return true;
  }

}
