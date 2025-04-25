package oogasalad.authoring.view.mainView;

import javafx.geometry.Insets;
import javafx.scene.layout.*;

/**
 * Utility class for applying consistent styles and layout adjustments
 * to the Authoring Environment UI components.
 *
 * <p>Handles layout spacing, sizing of panels, and basic background and border styling.
 * Encapsulates view-specific styling logic separately from the main view-building classes.</p>
 *
 * @see AuthoringLayoutBuilder
 * @see AuthoringView
 *
 * @author William He
 */
public class StyleManager {

  /**
   * Applies styling rules to the major sections of the AuthoringView layout.
   *
   * <p>This method configures margins, preferred sizes, background colors,
   * and spacing between major UI components.</p>
   *
   * @param view the AuthoringView instance to style
   */
  public static void applyStyles(AuthoringView view) {
    VBox fullLayout = (VBox) ((BorderPane) view.getNode()).getCenter();
    BorderPane mainContent = (BorderPane) fullLayout.getChildren().get(1);
    VBox rightPanel = (VBox) mainContent.getRight();

    rightPanel.setPrefWidth(300);
    view.getLevelSelectorView().getRoot().setPrefWidth(200);

    AnchorPane editorContainer = (AnchorPane) rightPanel.getChildren().get(1);
    VBox.setVgrow(editorContainer, Priority.ALWAYS);

    view.getGameSettingsView().setPreferredHeight(200);
    view.getGameSettingsView().setMinimumHeight(180);

    VBox.setMargin(view.getGameSettingsView().getNode(), new Insets(0, 0, 20, 0));
    view.getGameSettingsView().getNode().setStyle(
        "-fx-background-color: #f4f4f4; " +
            "-fx-border-color: #cccccc; " +
            "-fx-border-width: 1px 0 0 0; " +
            "-fx-padding: 10px;");

    fullLayout.setSpacing(10);
  }
}
