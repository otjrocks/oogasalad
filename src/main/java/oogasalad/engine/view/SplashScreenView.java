package oogasalad.engine.view;


import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.ThemeManager;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.config.JsonConfigParser;
import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.api.GameMapFactory;
import oogasalad.engine.model.exceptions.InvalidPositionException;
import oogasalad.engine.view.components.Selector;
import oogasalad.player.view.GameView;

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
    createExampleMap();
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

  private void createExampleMap() {
    JsonConfigParser configParser = new JsonConfigParser();
    ConfigModel configModel = null;
    try {
      configModel = configParser.loadFromFile("data/basic.json");
    } catch (ConfigException e) {
      LoggingManager.LOGGER.warn(e);
    }
    GameMap gameMap = null;
    try {
      if (configModel != null) {
        gameMap = GameMapFactory.createGameMap(configModel, 20, 20);
      }
    } catch (InvalidPositionException e) {
      LoggingManager.LOGGER.warn(e);
    }
    GameView gameView = new GameView(gameMap);
    this.getChildren().add(gameView);
  }
}
