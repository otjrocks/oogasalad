package oogasalad.engine.view.components;

import java.util.Objects;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import oogasalad.engine.utility.ThemeManager;

/**
 * Utility class for applying consistent formatting styles to UI components.
 */
public class FormattingUtil {

  /**
   * Applies a standard style to the given Alert dialog.
   * This method customizes the appearance of the dialog by adding a specific style class
   * and stylesheet, and registers the dialog's scene with the ThemeManager for consistent
   * theming.
   *
   * @param alert the Alert dialog to which the standard style will be applied
   * @throws NullPointerException if the stylesheet resource cannot be found
   */
  public static void applyStandardDialogStyle(Alert alert) {
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.getStyleClass().add("alert");
    dialogPane.getStylesheets().add(
        Objects.requireNonNull(FormattingUtil.class.getResource("/oogasalad/styles.css"))
            .toExternalForm());
    ThemeManager.getInstance().registerScene(dialogPane.getScene());
  }

  public static Button createSmallButton(String text) {
    Button button = new Button(text);
    button.getStyleClass().add("small-button");
    return button;
  }

  public static Label createHeading(String text) {
    Label label = new Label(text);
    label.getStyleClass().add("heading");
    return label;
  }

  public static Label createTitle(String text) {
    Label label = new Label(text);
    label.getStyleClass().add("title");
    return label;
  }

  public static Label createLabel(String text) {
    Label label = new Label(text);
    label.getStyleClass().add("label");
    return label;
  }

  public static TextField createTextField() {
    return createTextField(null);
  }

  public static TextField createTextField(String text) {
    TextField textField = (text == null) ? new TextField() : new TextField(text);
    textField.getStyleClass().add("text-input");
    return textField;
  }

}
