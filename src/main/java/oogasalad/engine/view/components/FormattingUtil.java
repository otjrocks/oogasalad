package oogasalad.engine.view.components;

import java.util.Objects;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import oogasalad.engine.utility.ThemeManager;

public class FormattingUtil {

  public static void applyStandardDialogStyle(Alert alert) {
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.getStyleClass().add("alert");
    dialogPane.getStylesheets().add(
        Objects.requireNonNull(FormattingUtil.class.getResource("/oogasalad/styles.css"))
            .toExternalForm());
    ThemeManager.getInstance().registerScene(dialogPane.getScene());
  }

}
