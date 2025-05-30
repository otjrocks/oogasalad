package oogasalad.authoring.view.mainView;

import javafx.scene.layout.*;

/**
 * Utility class for applying consistent styles and layout adjustments to the Authoring Environment
 * UI components.
 *
 * <p>Handles layout spacing, sizing of panels, and basic background and border styling.
 * Encapsulates view-specific styling logic separately from the main view-building classes.</p>
 *
 * @author William He
 * @see AuthoringLayoutBuilder
 * @see AuthoringView
 */
public class StyleManager {

  private static final String ROOT = "root";

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

    fullLayout.getStyleClass().add(ROOT);
    mainContent.getStyleClass().add(ROOT);
    rightPanel.getStyleClass().add("side-panel");
    view.getLevelSelectorView().getRoot().getStyleClass().add("side-panel");
    view.getGameSettingsView().getNode().getStyleClass().add(ROOT);

    VBox.setVgrow(rightPanel, Priority.ALWAYS);
    VBox.setVgrow(view.getGameSettingsView().getNode(), Priority.NEVER);
  }
}
