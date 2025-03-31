package oogasalad.engine.view;

import static oogasalad.engine.LanguageManager.getMessage;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oogasalad.engine.config.GameConfig;
import oogasalad.engine.controller.MainController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;
import util.TestUtils;

class SplashScreenViewTest extends DukeApplicationTest {

  private TestUtils myTestUtils;

  @Override
  public void start(Stage stage) {
    Group root = new Group();
    Scene scene = new Scene(root, GameConfig.WIDTH, GameConfig.HEIGHT);
    stage.setScene(scene);
    MainController mainController = new MainController(stage, root);
    SplashScreenView splashScreenView = new SplashScreenView(mainController);
    root.getChildren().add(splashScreenView);
    stage.show();
  }

  @BeforeEach
  void setUp() {
    myTestUtils = new TestUtils();
  }

  @Test
  void setLanguage_EnsureEnglishTest_Success() {
    verifyLanguageChange("English");
  }

  @Test
  void setLanguage_EnsureItalianTest_Success() {
    verifyLanguageChange("Italian");
  }

  @Test
  void setLanguage_TestTwoLanguageSwitch_Success() {
    verifyLanguageChange("Italian");
    verifyLanguageChange("English");
  }


  private void verifyLanguageChange(String language) {
    waitForFxEvents();
    clickOn("#languageSelector");
    clickOn(language);
    waitForFxEvents();  // Ensure that all UI events and updates are processed before assertions

    // Verify text elements and buttons change on language change
    myTestUtils.verifyText("#splashScreenTitle", getMessage("TITLE"));
    myTestUtils.verifyText("#languageSelector-label", getMessage("LANGUAGE_SELECTOR_TITLE"));
  }

}