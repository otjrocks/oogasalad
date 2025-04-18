package oogasalad.authoring.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class ModeEditorDialogTest extends DukeApplicationTest {

  private ModeEditorDialog dialog;

  @Override
  public void start(Stage stage) {
    runAsJFXAction(() -> {
      // Set up a real scene so Dialog has a valid owner window
      stage.setScene(new Scene(new StackPane()));
      stage.show();
    });
  }

  @Test
  void openDialog_FieldsAreVisibleAndEditable() {
    runAsJFXAction(() -> {
      dialog = new ModeEditorDialog();
      dialog.getDialog().initOwner(Stage.getWindows().stream().findFirst().get());
      dialog.showAndWait();
    });

    List<TextField> fields = lookup(".dialog-pane .text-field").queryAllAs(TextField.class).stream().toList();

    assertEquals(7, fields.size(), "Should have 7 text fields (name, image, speed, tileW, tileH, tilesToCycle, animationSpeed)");

    TextField nameField = fields.get(0);
    TextField imageField = fields.get(1);
    TextField speedField = fields.get(2);
    TextField tileWidthField = fields.get(3);
    TextField tileHeightField = fields.get(4);
    TextField tilesToCycleField = fields.get(5);
    TextField animationSpeedField = fields.get(6);

    assertTrue(nameField.isEditable());
    assertFalse(imageField.isEditable());
    assertTrue(speedField.isEditable());
    assertTrue(tileWidthField.isEditable());
    assertTrue(tileHeightField.isEditable());
    assertTrue(tilesToCycleField.isEditable());
    assertTrue(animationSpeedField.isEditable());

    writeInputTo(nameField, "TestMode");
    writeInputTo(speedField, "5.0");
    writeInputTo(tileWidthField, "28");
    writeInputTo(tileHeightField, "28");
    writeInputTo(tilesToCycleField, "4");
    writeInputTo(animationSpeedField, "1.5");

    assertEquals("TestMode", nameField.getText());
    assertEquals("5.0", speedField.getText());
    assertEquals("28", tileWidthField.getText());
    assertEquals("28", tileHeightField.getText());
    assertEquals("4", tilesToCycleField.getText());
    assertEquals("1.5", animationSpeedField.getText());
  }



  @Test
  void clickCancel_ClosesDialogWithoutException() {
    runAsJFXAction(() -> {
      dialog = new ModeEditorDialog();
      dialog.getDialog().initOwner(Stage.getWindows().stream().findFirst().get());
      dialog.showAndWait();
    });

    clickOn(lookup("Cancel").query());
    assertTrue(dialog.getDialog().getResult() == null);
  }

  @Test
  void clickOk_WithEmptyFields_KeepsDialogOpen() {
    runAsJFXAction(() -> {
      dialog = new ModeEditorDialog();
      dialog.getDialog().initOwner(Stage.getWindows().stream().findFirst().get());
      dialog.showAndWait();
    });
    clickOn(lookup("OK").query());
    // Since required fields are empty and image isn't set, dialog should not close
    assertTrue(dialog.getDialog().isShowing()); // Still open
  }
}
