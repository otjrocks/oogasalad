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
   * Applies a standard style to the given Alert dialog. This method customizes the appearance of
   * the dialog by adding a specific style class and stylesheet, and registers the dialog's scene
   * with the ThemeManager for consistent theming.
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

  /**
   * Creates a styled button with the specified text and applies the "small-button" CSS style
   * class.
   *
   * @param text the text to display on the button
   * @return a Button instance with the specified text and "small-button" style applied
   */
  public static Button createSmallButton(String text) {
    Button button = new Button(text);
    button.getStyleClass().add("small-button");
    return button;
  }

  /**
   * Creates a Label with the specified text and applies the "heading" style class to it.
   *
   * @param text the text to be displayed in the Label
   * @return a Label styled as a heading with the specified text
   */
  public static Label createHeading(String text) {
    Label label = new Label(text);
    label.getStyleClass().add("heading");
    return label;
  }

  /**
   * Creates a styled Label with the specified text and applies the "title" style class.
   *
   * @param text the text to be displayed in the Label
   * @return a Label styled as a title
   */
  public static Label createTitle(String text) {
    Label label = new Label(text);
    label.getStyleClass().add("title");
    return label;
  }

  /**
   * Creates a new TextField with default settings. This method delegates to
   * {@link #createTextField(String)} with a null parameter.
   *
   * @return a new TextField instance with default configuration
   */
  public static TextField createTextField() {
    return createTextField(null);
  }

  /**
   * Creates a styled TextField with the specified initial text. If the provided text is null, an
   * empty TextField is created.
   *
   * @param text the initial text to set in the TextField, or null for an empty TextField
   * @return a TextField instance with the specified text and a "text-input" style class applied
   */
  public static TextField createTextField(String text) {
    TextField textField = (text == null) ? new TextField() : new TextField(text);
    textField.getStyleClass().add("text-input");
    return textField;
  }

}
