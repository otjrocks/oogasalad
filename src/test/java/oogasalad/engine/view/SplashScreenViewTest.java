package oogasalad.engine.view;

import static oogasalad.engine.LanguageManager.getMessage;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;
import util.TestUtils;

class SplashScreenViewTest extends DukeApplicationTest {

  private TestUtils myTestUtils;

  @Override
  public void start(Stage stage) {
    SplashScreenView splashScreenView = new SplashScreenView();
    Scene scene = new Scene(splashScreenView, 800, 600);
    stage.setScene(scene);
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
    clickOn("#languageSelector");
    clickOn(language);
    waitForFxEvents();  // Ensure that all UI events and updates are processed before assertions

    // Verify text elements and buttons change on language change
    myTestUtils.verifyText("#splashScreenTitle", getMessage("TITLE"));
    myTestUtils.verifyText("#languageSelector-label", getMessage("LANGUAGE_SELECTOR_TITLE"));
  }

}