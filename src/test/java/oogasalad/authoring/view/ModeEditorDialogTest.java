package oogasalad.authoring.view;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

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
    TextField nameField = lookup(".dialog-pane .text-field").nth(0).query();
    TextField imageField = lookup(".dialog-pane .text-field").nth(1).query();
    TextField speedField = lookup(".dialog-pane .text-field").nth(2).query();

    assertNotNull(nameField);
    assertNotNull(imageField);
    assertNotNull(speedField);
    assertTrue(nameField.isEditable());
    assertFalse(imageField.isEditable());  // image field is readonly
    assertTrue(speedField.isEditable());

    writeInputTo(nameField, "TestMode");
    writeInputTo(speedField, "5");

    assertEquals("TestMode", nameField.getText());
    assertEquals("5", speedField.getText());
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
