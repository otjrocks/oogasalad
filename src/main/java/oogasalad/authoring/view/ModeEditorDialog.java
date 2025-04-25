package oogasalad.authoring.view;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import oogasalad.engine.records.config.ImageConfigRecord;
import oogasalad.engine.records.config.ModeConfigRecord;
import oogasalad.engine.records.config.model.EntityPropertiesRecord;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.utility.LanguageManager;

/**
 * Dialog for creating a new ModeConfig with a user-uploaded image. Returns a ModeConfig object via
 * showAndWait().ifPresent(...)
 *
 * @author Will He, Ishan Madan, Owen Jennings
 */
public class ModeEditorDialog {

  private final TextField nameField;
  private final TextField speedField;
  private final TextField imagePathField;
  private final TextField tileWidthField;
  private final TextField tileHeightField;
  private final TextField tilesToCycleField;
  private final TextField animationSpeedField;
  private final ControlTypeEditorView controlTypeEditorView;

  private File selectedImageFile;
  private ModeConfigRecord preparedResult;

  private final Dialog<ModeConfigRecord> dialog;


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
    grid.setPrefSize(800, 600);
    grid.setAlignment(Pos.CENTER);

    nameField = new TextField();
    imagePathField = new TextField();
    imagePathField.setEditable(false);

    tileWidthField = new TextField("28");
    tileHeightField = new TextField("28");
    tilesToCycleField = new TextField("4");
    animationSpeedField = new TextField("1.0");

    controlTypeEditorView = new ControlTypeEditorView();

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
    grid.add(new Label("Tile Width:"), 0, 3);
    grid.add(tileWidthField, 1, 3);
    grid.add(new Label("Tile Height:"), 0, 4);
    grid.add(tileHeightField, 1, 4);
    grid.add(new Label("Tiles to Cycle:"), 0, 5);
    grid.add(tilesToCycleField, 1, 5);
    grid.add(new Label("Animation Speed:"), 0, 6);
    grid.add(animationSpeedField, 1, 6);
    grid.add(new Label("Control Type:"), 0, 7);
    grid.add(controlTypeEditorView.getRoot(), 1, 7);

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
  public Dialog<ModeConfigRecord> getDialog() {
    return dialog;
  }

  /**
   * Shows the dialog and waits for user input.
   *
   * @return Optional containing the list of collision rules if OK was pressed, empty Optional
   * otherwise
   */
  public Optional<ModeConfigRecord> showAndWait() {
    return dialog.showAndWait();
  }

  /**
   * Constructor for editing an existing mode
   *
   * @param existingConfig already existing mode config
   */
  public ModeEditorDialog(ModeConfigRecord existingConfig) {
    this(); // call default constructor first

    if (existingConfig != null) {
      nameField.setText(existingConfig.name());
      speedField.setText(String.valueOf(existingConfig.movementSpeed()));

      String imagePath = existingConfig.image().imagePath();
      try {
        if (imagePath.startsWith("file:/")) {
          selectedImageFile = new File(new File(new URI(imagePath)).getAbsolutePath());
        } else {
          selectedImageFile = new File(imagePath);  // Assume it's already a file path
        }
      } catch (Exception e) {
        showError(LanguageManager.getMessage("CANNOT_LOAD_IMAGE"));
        selectedImageFile = new File("");  // fallback
      }
      imagePathField.setText(selectedImageFile.getName());

      // Pre-fill new image config fields
      tileWidthField.setText(String.valueOf(existingConfig.image().tileWidth()));
      tileHeightField.setText(String.valueOf(existingConfig.image().tileHeight()));
      tilesToCycleField.setText(String.valueOf(existingConfig.image().tilesToCycle()));
      animationSpeedField.setText(String.valueOf(existingConfig.image().animationSpeed()));
      controlTypeEditorView.populateControlConfigUI(existingConfig.controlConfig());

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
      imagePathField.setText(file.getPath());
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

    try {
      double speed = Double.parseDouble(speedField.getText());
      ImageConfigRecord imageConfig = getImageConfig();

      ControlConfigInterface controlConfig = controlTypeEditorView.getControlConfig();

      EntityPropertiesRecord entityProps = new EntityPropertiesRecord(
          name,
          List.of()
      );

      preparedResult = new ModeConfigRecord(name, entityProps, controlConfig, imageConfig, speed);
      return true;

    } catch (NumberFormatException ex) {
      showError("All numeric fields must be valid numbers.");
      return false;
    }
  }

  private ImageConfigRecord getImageConfig() {
    int tileWidth = Integer.parseInt(tileWidthField.getText());
    int tileHeight = Integer.parseInt(tileHeightField.getText());
    int tilesToCycle = Integer.parseInt(tilesToCycleField.getText());
    double animationSpeed = Double.parseDouble(animationSpeedField.getText());

    String imagePath = selectedImageFile.getAbsolutePath();

    return new ImageConfigRecord(
        imagePath,
        tileWidth,
        tileHeight,
        tilesToCycle,
        animationSpeed
    );
  }



}
