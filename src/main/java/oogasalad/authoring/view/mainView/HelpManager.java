package oogasalad.authoring.view.mainView;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.help.SimpleHelpSystem;
import oogasalad.engine.utility.LanguageManager;

class HelpManager {

  private final AuthoringView view;
  private final AuthoringController controller;
  private SimpleHelpSystem helpSystem;

  public HelpManager(AuthoringView view, AuthoringController controller) {
    this.view = view;
    this.controller = controller;
  }

  public void setupHelpSystem() {
    Platform.runLater(() -> {
      helpSystem = new SimpleHelpSystem(controller, view, getStage());
      addHelpButton();
      addHelpMenu();
      setupHelpShortcuts();
    });
  }

  private Stage getStage() {
    return (Stage) view.getNode().getScene().getWindow();
  }

  private void addHelpButton() {
    Button helpButton = new Button("?");
    helpButton.setId("helpButton");
    helpButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
        "-fx-background-color: #3498db; -fx-text-fill: white; " +
        "-fx-background-radius: 50%; -fx-min-width: 30px; " +
        "-fx-min-height: 30px;");
    helpButton.setTooltip(new Tooltip(LanguageManager.getMessage("HELP")));
    helpButton.setOnAction(e -> helpSystem.showHelpDialog());

    VBox fullLayout = (VBox) ((BorderPane) view.getNode()).getCenter();
    BorderPane mainContent = (BorderPane) fullLayout.getChildren().get(1);
    StackPane canvasWithHelp = new StackPane(view.getCanvasView().getNode(), helpButton);
    mainContent.setCenter(canvasWithHelp);

    StackPane.setAlignment(helpButton, javafx.geometry.Pos.TOP_RIGHT);
    StackPane.setMargin(helpButton, new Insets(10));
  }

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

  private void setupHelpShortcuts() {
    view.getNode().getScene().getAccelerators().put(
        new javafx.scene.input.KeyCodeCombination(javafx.scene.input.KeyCode.F1),
        () -> helpSystem.showHelpDialog()
    );
  }

  private void showAboutDialog() {
    AlertUtil.showAlert(getStage(),
        LanguageManager.getMessage("ABOUT"),
        LanguageManager.getMessage("GAME_AUTHORING_CONTENT"),
        Alert.AlertType.INFORMATION);
  }
}
