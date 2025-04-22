package oogasalad.player.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import oogasalad.engine.controller.MainController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
  void testRootLayoutExists() {
    assertNotNull(view.getRoot());
    assertTrue(view.getRoot().getChildren().size() >= 2); // top bar, upload section, pagination
  }

  @Test
  void testBackButtonTriggersNavigation() {
    Button backButton = lookup("Back").queryButton();
    clickOn(backButton);

    verify(mockController).hideGameSelectorView();
    verify(mockController).showSplashScreen();
  }

  @Test
  void testHelpButtonIsPresent() {
    Button helpButton = lookup("Help").queryButton();
    assertNotNull(helpButton);
  }

  @Test
  void testUploadAndStartButtons() {
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
  void testResetUploadSectionRestoresState() {
    runAsJFXAction(() -> view.resetUploadSection());

    Button startButton = lookup("Start").queryButton();
    Label fileLabel = lookup(
        l -> l instanceof Label && ((Label) l).getText().contains("No file")).query();

    assertTrue(startButton.isDisabled());
    assertEquals("No file selected", fileLabel.getText());
  }

  @Test
  void testClickOnGameCardCallsController() {
    runAsJFXAction(() -> {
      view.getRoot().getChildren().clear();
      view.getRoot().getChildren().add(view.createGameCard("Fake Game"));
    });

    clickOn("Fake Game");

    // Even though gameNameToFolder is empty, we expect this to be called
    verify(mockController, atLeastOnce()).hideGameSelectorView();
  }
}
