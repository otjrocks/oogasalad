package oogasalad.engine.view;


import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.ThemeManager;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.view.components.Selector;
import oogasalad.engine.view.components.Vmenu;

/**
 * The initial splash screen shown when the program is started.
 *
 * @author Owen Jennings
 */
public class SplashScreenView extends VBox {

  private Selector myLanguageSelector;
  private final ThemeManager myThemeManager;
  private Selector myThemeSelector;
  private final MainController myMainController;

  /**
   * Create a splash screen view.
   *
   * @param mainController The main controller of the program.
   */
  public SplashScreenView(MainController mainController) {
    super();
    myThemeManager = new ThemeManager(mainController.getStage());
    myMainController = mainController;
    this.getStyleClass().add("splash-screen-view");
    this.setPrefSize(GameConfig.WIDTH, GameConfig.HEIGHT);
    initializeSplashScreen();
  }

  /**
   * Initialize all the splash screen components and add them to the VBox.
   */
  private void initializeSplashScreen() {
    initializeTitle();
    initializeLanguageSelector();
    initializeThemeSelector();
    initializeSplashMenu();
  }

  private void initializeSplashMenu() {
    List<String> options = List.of(LanguageManager.getMessage("GAME_PLAYER"),
        LanguageManager.getMessage("AUTHORING_ENVIRONMENT"),
        LanguageManager.getMessage("CONFIGURATION"));
    List<EventHandler<ActionEvent>> actions = List.of(
        e -> activateGamePlayerMode(),
        e -> {
        },
        e -> {
        }
    );
    Vmenu splashMenu = new Vmenu(options, actions);
    this.getChildren().add(splashMenu);
  }

  private void activateGamePlayerMode() {
    myMainController.hideSplashScreen();
    myMainController.showGamePlayerView();
  }


  private void initializeThemeSelector() {
    myThemeSelector = new Selector(myThemeManager.getAvailableThemes(),
        ThemeManager.DEFAULT_THEME, "themeSelector",
        LanguageManager.getMessage("THEME_SELECTOR_TITLE"), e -> switchTheme());
    this.getChildren().add(myThemeSelector);
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

  private void switchTheme() {
    myThemeManager.setTheme(myThemeSelector.getValue());
  }


  private void refresh() {
    this.getChildren().clear();
    initializeSplashScreen();
  }
}
