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
  private final LanguageManager myLanguageManager;

  /**
   * Create the splash screen view.
   *
   * @author Owen Jennings
   */
  public SplashScreenView() {
    myLanguageManager = new LanguageManager();
    initializeSplashScreen();
  }

  /**
   * Initialize all the splash screen components and add them to the VBox.
   */
  private void initializeSplashScreen() {
    myLanguageSelector = new Selector(myLanguageManager.getAvailableLanguages(),
        myLanguageManager.getLanguage(),
        "language-selector", LanguageManager.getMessage("LANGUAGE_SELECTOR_TITLE"),
        e -> handleLanguageSelection());
    this.getChildren().add(new Text(LanguageManager.getMessage("TITLE")));
    this.getChildren().add(myLanguageSelector);
  }

  private void handleLanguageSelection() {
    myLanguageManager.setLanguage(myLanguageSelector.getValue());
    refresh();
  }

  private void refresh() {
    this.getChildren().clear();
    initializeSplashScreen();
  }

}
