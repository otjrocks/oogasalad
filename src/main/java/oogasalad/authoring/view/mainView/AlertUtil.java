package oogasalad.authoring.view.mainView;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import oogasalad.engine.view.components.FormattingUtil;

/**
 * Utility class for displaying standardized, themed alert dialogs in the Authoring Environment.
 */
public class AlertUtil {

  /**
   * Displays an alert dialog with the given parameters, styled consistently with the application
   * theme.
   *
   * @param owner   the stage or window that owns the alert
   * @param title   the title of the alert window
   * @param message the message content displayed in the alert
   * @param type    the type of alert (e.g., INFORMATION, ERROR, CONFIRMATION)
   */
  public static void showAlert(Stage owner, String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type, message, ButtonType.OK);
    alert.setTitle(title);
    alert.initOwner(owner);

    FormattingUtil.applyStandardDialogStyle(alert);

    alert.showAndWait();
  }
}
