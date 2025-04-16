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
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.controlConfig.KeyboardControlConfig;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.records.config.ImageConfig;
import oogasalad.engine.config.ModeConfig;
import oogasalad.engine.records.config.model.EntityProperties;

/**
 * Dialog for creating a new ModeConfig with a user-uploaded image. Returns a ModeConfig object via
 * showAndWait().ifPresent(...)
 *
 * @author Will He, Ishan Madan
 */
public class ModeEditorDialog {

  private TextField nameField;
  private TextField speedField;
  private TextField imagePathField;
  private TextField tileWidthField;
  private TextField tileHeightField;
  private TextField tilesToCycleField;
  private TextField animationSpeedField;

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

    tileWidthField = new TextField("28");
    tileHeightField = new TextField("28");
    tilesToCycleField = new TextField("4");
    animationSpeedField = new TextField("1.0");

    grid.add(new Label("Tile Width:"), 0, 3);
    grid.add(tileWidthField, 1, 3);
    grid.add(new Label("Tile Height:"), 0, 4);
    grid.add(tileHeightField, 1, 4);
    grid.add(new Label("Tiles to Cycle:"), 0, 5);
    grid.add(tilesToCycleField, 1, 5);
    grid.add(new Label("Animation Speed:"), 0, 6);
    grid.add(animationSpeedField, 1, 6);


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
   * @return Optional containing the list of collision rules if OK was pressed, empty Optional
   * otherwise
   */
  public Optional<ModeConfig> showAndWait() {
    return dialog.showAndWait();
  }

  /**
   * Constructor for editing an existing mode
   *
   * @param existingConfig already existing mode config
   */
  public ModeEditorDialog(ModeConfig existingConfig) {
    this(); // call default constructor first

    if (existingConfig != null) {
      nameField.setText(existingConfig.name());
      speedField.setText(String.valueOf(existingConfig.entityProperties().movementSpeed()));

      selectedImageFile = new File(URI.create(existingConfig.image().imagePath()));
      imagePathField.setText(selectedImageFile.getName());

      // Pre-fill new image config fields
      tileWidthField.setText(String.valueOf(existingConfig.image().tileWidth()));
      tileHeightField.setText(String.valueOf(existingConfig.image().tileHeight()));
      tilesToCycleField.setText(String.valueOf(existingConfig.image().tilesToCycle()));
      animationSpeedField.setText(String.valueOf(existingConfig.image().animationSpeed()));
    }
  }


  private void handleImageUpload() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(LanguageManager.getMessage("CHOOSE_IMAGE"));
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter(LanguageManager.getMessage("IMAGE_FILES"), "*.png", "*.jpg",
            "*.jpeg", "*.gif")
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

    try {
      double speed = Double.parseDouble(speedField.getText());
      ImageConfig imageConfig = getImageConfig();

      ControlConfig controlConfig = new KeyboardControlConfig(); // temp fallback

      EntityProperties entityProps = new EntityProperties(
          name,
          controlConfig,
          speed,
          List.of()
      );

      preparedResult = new ModeConfig(name, entityProps, imageConfig);
      return true;

    } catch (NumberFormatException ex) {
      showError("All numeric fields must be valid numbers.");
      return false;
    }
  }

  private ImageConfig getImageConfig() {
    int tileWidth = Integer.parseInt(tileWidthField.getText());
    int tileHeight = Integer.parseInt(tileHeightField.getText());
    int tilesToCycle = Integer.parseInt(tilesToCycleField.getText());
    double animationSpeed = Double.parseDouble(animationSpeedField.getText());

    String imagePath = selectedImageFile.toURI().toString();
    return new ImageConfig(
        imagePath,
        tileWidth,
        tileHeight,
        tilesToCycle,
        animationSpeed
    );
  }


}
