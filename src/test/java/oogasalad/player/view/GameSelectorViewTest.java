package oogasalad.player.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import oogasalad.engine.controller.MainController;
import oogasalad.engine.utility.LanguageManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;
import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for GameSelectorView using DukeApplicationTest and proper stage mocking.
 */
public class GameSelectorViewTest extends DukeApplicationTest {

  private GameSelectorView view;
  private MainController mockController;

  @Override
  public void start(Stage stage) {
    LanguageManager.setLanguage("English");
    mockController = mock(MainController.class);
    when(mockController.getStage()).thenReturn(stage);

    // TEMP SCENE avoids ThemeManager crash
    Scene scene = new Scene(new javafx.scene.layout.Pane(), 1300, 1000);
    stage.setScene(scene);
    stage.show();

    view = new GameSelectorView(mockController);

    runAsJFXAction(() -> stage.getScene().setRoot(view.getRoot()));
  }


  @BeforeEach
  void resetView() {
    runAsJFXAction(() -> view.resetUploadSection());
  }

  @Test
  void getRoot_callGetRoot_getRootWithAppropriateSize() {
    assertNotNull(view.getRoot());
    assertTrue(view.getRoot().getChildren().size() >= 2); // top bar, upload section, pagination
  }

  @Test
  void backButton_setOnAction_hideGameSelectorViewShowSplashScreen() {
    Button backButton = lookup("Back").queryButton();
    clickOn(backButton);

    verify(mockController).hideGameSelectorView();
    verify(mockController).showSplashScreen();
  }

  // currently no action


  // might delete since we removing? upload
  @Test
  void uploadStartButton_setOnAction_noFileSelected() {
    Button uploadButton = lookup("Upload").queryButton();
    Button startButton = lookup("Start").queryButton();
    Label fileLabel = lookup(
        l -> l instanceof Label && ((Label) l).getText().contains("No file")).query();

    assertNotNull(uploadButton);
    assertNotNull(startButton);
    assertEquals("No file selected", fileLabel.getText());
    assertTrue(startButton.isDisabled());
  }


  @Test
  void gameCard_setOnAction_callsController() {
    runAsJFXAction(() -> {
      view.getRoot().getChildren().clear();
      view.getRoot().getChildren().add(view.createGameCard("Fake Game"));
    });

    clickOn("Fake Game");

    // Even though gameNameToFolder is empty, we expect this to be called
    verify(mockController, atLeastOnce()).showGamePlayerView(null, false);
  }

  @Test
  void randomize_setOnAction_callsController() {
    runAsJFXAction(() -> {
      view.getRoot().getChildren().clear();
      view.getRoot().getChildren().add(view.createGameCard("Fake Game"));
    });

    Button randomizeButton = lookup("Randomize Levels").queryButton();
    clickOn(randomizeButton);

    // Even though gameNameToFolder is empty, we expect this to be called
    verify(mockController, atLeastOnce()).showGamePlayerView(null, true);
  }

  @Test
  void infoButton_setOnAction_showsMetadataPopup() {
    runAsJFXAction(() -> {
      view.getRoot().getChildren().clear();
      view.getRoot().getChildren().add(view.createGameCard("Fake Game"));
    });

    Button infoButton = lookup(
        b -> b instanceof Button && ((Button) b).getText().equals("i")).queryButton();
    assertNotNull(infoButton);

    clickOn(infoButton);

    assertTrue(infoButton.isVisible());
  }

  @Test
  void infoButton_setOnAction_openMetadataPopupAndCanClose() {
    runAsJFXAction(() -> {
      view.getRoot().getChildren().clear();
      view.getRoot().getChildren().add(view.createGameCard("Fake Game"));
    });

    Button infoButton = lookup(
        b -> b instanceof Button && ((Button) b).getText().equals("i")).queryButton();
    clickOn(infoButton);

    WaitForAsyncUtils.waitForFxEvents();

    String dialogText = lookup(".dialog-pane .content").queryAs(Label.class).getText();

    // since this is a mock and there no content
    assertTrue(dialogText.contains("not found"), "Metadata dialog not shown or incorrect content");

    clickOn(lookup(DukeApplicationTest.DEFAULT_SUBMIT_BUTTON_TEXT).queryButton());
  }

}
