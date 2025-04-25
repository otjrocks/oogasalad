package oogasalad.authoring.view.mainView;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Utility class for displaying standardized alert dialogs in the Authoring Environment.
 *
 * <p>Provides a simple static method to show an alert with a specified title, message,
 * and alert type, attached to a given window owner.</p>
 *
 * <p>This class promotes consistent alert handling across different parts of the system.</p>
 *
 * @author William He
 */
public class AlertUtil {

  /**
   * Displays an alert dialog with the given parameters.
   *
   * @param owner the stage or window that owns the alert
   * @param title the title of the alert window
   * @param message the message content displayed in the alert
   * @param type the type of alert (e.g., INFORMATION, ERROR, CONFIRMATION)
   */
  public static void showAlert(Stage owner, String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type, message, ButtonType.OK);
    alert.setTitle(title);
    alert.initOwner(owner);
    alert.showAndWait();
  }
}
