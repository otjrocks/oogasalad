package oogasalad.engine.view;


import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.view.components.Selector;

/**
 * The initial splash screen shown when the program is started.
 *
 * @author Owen Jennings
 */
public class SplashScreenView extends VBox {

  private Selector myLanguageSelector;

  /**
   * Create the splash screen view.
   *
   * @author Owen Jennings
   */
  public SplashScreenView() {
    initializeSplashScreen();
  }

  /**
   * Initialize all the splash screen components and add them to the VBox.
   */
  private void initializeSplashScreen() {
    myLanguageSelector = new Selector(LanguageManager.getAvailableLanguages(),
        LanguageManager.getLanguage(),
        "languageSelector", LanguageManager.getMessage("LANGUAGE_SELECTOR_TITLE"),
        e -> handleLanguageSelection());
    Text title = new Text(LanguageManager.getMessage("TITLE"));
    title.setId("splashScreenTitle");
    this.getChildren().add(title);
    this.getChildren().add(myLanguageSelector);
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
