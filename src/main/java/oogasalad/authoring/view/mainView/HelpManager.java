package oogasalad.authoring.view.mainView;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.help.SimpleHelpSystem;
import oogasalad.engine.utility.LanguageManager;

/**
 * Manages the setup and display of the help system within the Authoring Environment.
 *
 * <p>Handles adding a help button, help menu, and setting up keyboard shortcuts
 * that trigger context-sensitive help dialogs.</p>
 *
 * <p>Separates help functionality from the core AuthoringView for better modularity and clarity.</p>
 *
 * @author William He
 */
public class HelpManager {

  private final AuthoringView view;
  private final AuthoringController controller;
  private SimpleHelpSystem helpSystem;

  /**
   * Constructs a HelpManager for the given AuthoringView and controller.
   *
   * @param view the AuthoringView where the help components will be integrated
   * @param controller the controller coordinating the model and view
   */
  public HelpManager(AuthoringView view, AuthoringController controller) {
    this.view = view;
    this.controller = controller;
  }

  /**
   * Sets up the entire help system, including creating the help button,
   * help menu, and registering keyboard shortcuts.
   */
  public void setupHelpSystem() {
    Platform.runLater(() -> {
      helpSystem = new SimpleHelpSystem(controller, view, getStage());
      addHelpButton();
      addHelpMenu();
      setupHelpShortcuts();
    });
  }

  /**
   * Retrieves the primary stage for the AuthoringView.
   *
   * @return the Stage containing the authoring UI
   */
  private Stage getStage() {
    return (Stage) view.getNode().getScene().getWindow();
  }

  /**
   * Adds a floating help button to the canvas area, aligned to the top right.
   * Clicking the button opens the help dialog.
   */
  private void addHelpButton() {
    Button helpButton = new Button("?");
    helpButton.setId("helpButton");
    helpButton.getStyleClass().add("help-button");
    helpButton.setTooltip(new Tooltip(LanguageManager.getMessage("HELP")));
    helpButton.setOnAction(e -> helpSystem.showHelpDialog());

    VBox fullLayout = (VBox) ((BorderPane) view.getNode()).getCenter();
    BorderPane mainContent = (BorderPane) fullLayout.getChildren().get(1);
    StackPane canvasWithHelp = new StackPane(view.getCanvasView().getNode(), helpButton);
    mainContent.setCenter(canvasWithHelp);

    StackPane.setAlignment(helpButton, javafx.geometry.Pos.TOP_RIGHT);
    StackPane.setMargin(helpButton, new Insets(10));
  }

  /**
   * Adds a "Help" menu to the existing menu bar, including options for
   * viewing help contents and an "About" dialog.
   */
  private void addHelpMenu() {
    MenuBar menuBar = (MenuBar) ((VBox) ((BorderPane) view.getNode()).getCenter()).getChildren().get(0);
    Menu helpMenu = new Menu(LanguageManager.getMessage("HELP"));

    MenuItem helpContents = new MenuItem(LanguageManager.getMessage("HELP_CONTENTS"));
    helpContents.setOnAction(e -> helpSystem.showHelpDialog());
    MenuItem about = new MenuItem(LanguageManager.getMessage("ABOUT"));
    about.setOnAction(e -> showAboutDialog());

    helpMenu.getItems().addAll(helpContents, new SeparatorMenuItem(), about);
    menuBar.getMenus().add(helpMenu);
  }

  /**
   * Sets up the F1 key as a keyboard shortcut to open the help dialog.
   */
  private void setupHelpShortcuts() {
    view.getNode().getScene().getAccelerators().put(
        new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.F1),
        () -> helpSystem.showHelpDialog()
    );
  }

  /**
   * Displays the "About" dialog with information about the authoring environment.
   */
  private void showAboutDialog() {
    AlertUtil.showAlert(getStage(),
        LanguageManager.getMessage("ABOUT"),
        LanguageManager.getMessage("GAME_AUTHORING_CONTENT"),
        Alert.AlertType.INFORMATION);
  }
}
