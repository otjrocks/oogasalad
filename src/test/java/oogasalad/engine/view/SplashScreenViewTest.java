package oogasalad.engine.view;

import static oogasalad.engine.utility.LanguageManager.getMessage;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.utility.constants.GameConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.matcher.base.NodeMatchers;
import util.DukeApplicationTest;
import util.TestUtils;

@ExtendWith(MockitoExtension.class)
class SplashScreenViewTest extends DukeApplicationTest {

  private TestUtils myTestUtils;

  @Override
  public void start(Stage stage) {
    Group root = new Group();
    Scene scene = new Scene(root, GameConfig.WIDTH, GameConfig.HEIGHT);
    stage.setScene(scene);
    new MainController(stage, root);
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
  void testToggle_EnsureConfigurationToggleWorks_Success() {
    assertFalse(lookup("#splash-configuration-box").query().isVisible());
    clickOn("#v-menu-button-2"); // toggle configuration box on
    assertTrue(lookup("#splash-configuration-box").query().isVisible());
  }

  private void verifyLanguageChange(String language) {
    clickOn("#v-menu-button-2");
    waitForFxEvents();
    clickOn("#languageSelector");
    waitForFxEvents();
    clickOn(language);
    waitForFxEvents();
    waitForFxEvents();

    verifyThat("#splashScreenTitle", hasText(getMessage("TITLE")));
    verifyThat("#splashScreenTitle", NodeMatchers.isVisible());
  }

}