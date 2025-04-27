package oogasalad.engine.view;

import static oogasalad.engine.utility.constants.GameConfig.ELEMENT_SPACING;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.ThemeManager;
import oogasalad.engine.utility.constants.GameConfig;
import oogasalad.engine.view.components.FormattingUtil;
import oogasalad.engine.view.components.Selector;
import oogasalad.engine.view.components.VMenu;

/**
 * The initial splash screen shown when the program is started.
 *
 * @author Owen Jennings
 */
public class SplashScreenView {

  private Selector myLanguageSelector;
  private Selector myThemeSelector;
  private final MainController myMainController;
  private final VBox myConfigurationBox;
  private final VBox myRoot;

  /**
   * Create a splash screen view.
   *
   * @param mainController The main controller of the program.
   */
  public SplashScreenView(MainController mainController) {
    myRoot = new VBox();
    myMainController = mainController;
    myConfigurationBox = new VBox(ELEMENT_SPACING);
    myConfigurationBox.setId("splash-configuration-box");
    myRoot.getStyleClass().add("center-root");
    myRoot.setPrefSize(GameConfig.WIDTH, GameConfig.HEIGHT);

    // Register the scene to the singleton ThemeManager
    ThemeManager.getInstance().registerScene(mainController.getStage().getScene());

    initializeSplashScreen();
  }

  /**
   * Get the root element where all the splash screen components are added to.
   *
   * @return A VBox JavaFX element.
   */
  public VBox getRoot() {
    return myRoot;
  }

  /**
   * Initialize all the splash screen components and add them to the VBox.
   */
  private void initializeSplashScreen() {
    initializeTitle();
    initializeLanguageSelector();
    initializeThemeSelector();
    initializeSplashMenu();
    myRoot.getChildren().add(myConfigurationBox);
    myConfigurationBox.setVisible(false);
  }

  private void initializeSplashMenu() {
    List<String> options = List.of(LanguageManager.getMessage("GAME_PLAYER"),
        LanguageManager.getMessage("AUTHORING_ENVIRONMENT"),
        LanguageManager.getMessage("CONFIGURATION"));
    List<EventHandler<ActionEvent>> actions = List.of(
        e -> loadGameSelector(),
        e -> activateAuthoringMode(),
        e -> toggleConfigurationMenu()
    );
    VMenu splashMenu = new VMenu(options, actions);
    myRoot.getChildren().add(splashMenu.getRoot());
  }

  private void toggleConfigurationMenu() {
    myConfigurationBox.setVisible(!myConfigurationBox.isVisible());
  }

  private void loadGameSelector() {
    myMainController.hideSplashScreen();
    myMainController.showGameSelectorView();
  }

  private void activateAuthoringMode() {
    myMainController.hideSplashScreen();
    myMainController.showAuthoringView();
  }

  private void initializeThemeSelector() {
    myThemeSelector = new Selector(ThemeManager.getInstance().getAvailableThemes(),
        ThemeManager.DEFAULT_THEME, "themeSelector",
        LanguageManager.getMessage("THEME_SELECTOR_TITLE"), e -> switchTheme());
    myConfigurationBox.getChildren().add(myThemeSelector.getRoot());
  }

  private void initializeLanguageSelector() {
    myLanguageSelector = new Selector(LanguageManager.getAvailableLanguages(),
        LanguageManager.getLanguage(),
        "languageSelector", LanguageManager.getMessage("LANGUAGE_SELECTOR_TITLE"),
        e -> handleLanguageSelection());
    myConfigurationBox.getChildren().add(myLanguageSelector.getRoot());
  }

  private void initializeTitle() {
    Label title = FormattingUtil.createTitle(LanguageManager.getMessage("TITLE"));
    title.setId("splashScreenTitle");
    title.getStyleClass().add("title");
    myRoot.getChildren().add(title);
  }

  private void handleLanguageSelection() {
    LanguageManager.setLanguage(myLanguageSelector.getValue());
    refresh();
  }

  private void switchTheme() {
    ThemeManager.getInstance().setTheme(myThemeSelector.getValue());
  }

  private void refresh() {
    myRoot.getChildren().clear();
    myConfigurationBox.getChildren().clear();
    initializeSplashScreen();
  }
}