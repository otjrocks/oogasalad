package oogasalad.engine.view;


import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.ThemeManager;
import oogasalad.engine.view.components.Selector;

/**
 * The initial splash screen shown when the program is started.
 *
 * @author Owen Jennings
 */
public class SplashScreenView extends VBox {

  private Selector myLanguageSelector;
  private final ThemeManager myThemeManager;
  private Selector myThemeSelector;

  /**
   * Create the splash screen view.
   *
   * @author Owen Jennings
   */
  public SplashScreenView(Stage stage) {
    myThemeManager = new ThemeManager(stage);
    initializeSplashScreen();
  }

  /**
   * Initialize all the splash screen components and add them to the VBox.
   */
  private void initializeSplashScreen() {
    initializeTitle();
    initializeLanguageSelector();
    myThemeSelector = new Selector(myThemeManager.getAvailableThemes(),
        ThemeManager.DEFAULT_THEME, "themeSelector",
        LanguageManager.getMessage("THEME_SELECTOR_TITLE"), e -> switchTheme());
    this.getChildren().add(myThemeSelector);
  }

  private void switchTheme() {
    myThemeManager.setTheme(myThemeSelector.getValue());
  }

  private void initializeLanguageSelector() {
    myLanguageSelector = new Selector(LanguageManager.getAvailableLanguages(),
        LanguageManager.getLanguage(),
        "languageSelector", LanguageManager.getMessage("LANGUAGE_SELECTOR_TITLE"),
        e -> handleLanguageSelection());
    this.getChildren().add(myLanguageSelector);
  }

  private void initializeTitle() {
    Text title = new Text(LanguageManager.getMessage("TITLE"));
    title.setId("splashScreenTitle");
    title.getStyleClass().add("title");
    this.getChildren().add(title);
  }

  private void handleLanguageSelection() {
    LanguageManager.setLanguage(myLanguageSelector.getValue());
    refresh();
  }

  private void refresh() {
    this.getChildren().clear();
    initializeSplashScreen();
  }

}
