package oogasalad.authoring.view.mainView;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class AlertUtil {

  public static void showAlert(Stage owner, String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type, message, ButtonType.OK);
    alert.setTitle(title);
    alert.initOwner(owner);
    alert.showAndWait();
  }
}
