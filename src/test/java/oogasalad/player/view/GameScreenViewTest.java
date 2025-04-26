package oogasalad.player.view;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import oogasalad.engine.controller.MainController;
import oogasalad.player.controller.GameInputManager;
import oogasalad.player.model.GameStateInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameScreenViewTest {

  private MainController mockMainController;
  private GameStateInterface mockGameState;
  private GameScreenView gameScreenView;

  // Static init for JavaFX
  static {
    new JFXPanel(); // Initializes JavaFX Toolkit
  }

  // wrote latch with chatGPT help
  @BeforeEach
  void setup() throws Exception {
    mockMainController = mock(MainController.class);
    mockGameState = mock(GameStateInterface.class);

    when(mockGameState.getScore()).thenReturn(100);
    when(mockGameState.getLives()).thenReturn(3);

    CountDownLatch latch = new CountDownLatch(1);

    Platform.runLater(() -> {
      gameScreenView = new GameScreenView(
          mockMainController,
          mockGameState,
          "src/test/resources/MockGame",
          false
      );
      latch.countDown(); // signal that init is complete
    });

    // Wait up to 2 seconds for JavaFX thread to finish setup
    if (!latch.await(2, TimeUnit.SECONDS)) {
      fail("Timed out waiting for JavaFX thread");
    }
  }

  @Test
  void testRootIsNotNullAndHasExpectedStyle() {
    VBox root = gameScreenView.getRoot();
    assertNotNull(root, "Root VBox should not be null");
    assertTrue(root.getStyleClass().contains("root"),
        "Root should have correct CSS class");
    assertTrue(root.getChildren().size() >= 2, "Root should contain HudView and GamePane");
  }

  // chatGpt'd
  @Test
  void checkAndUpdateHud_callbackInHub_updateScoreAndLives() {
    Platform.runLater(() -> {
      when(mockGameState.getScore()).thenReturn(150);
      when(mockGameState.getLives()).thenReturn(2);
    });

    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  @Test
  void handleReturnToMainMenu_callbackInHub_showsSplashScreen() throws Exception {
    Group mockRoot = mock(Group.class);
    ObservableList<Node> mockChildren = FXCollections.observableArrayList();
    when(mockRoot.getChildren()).thenReturn(mockChildren);

    GameInputManager mockInputManager = mock(GameInputManager.class);
    when(mockInputManager.getRoot()).thenReturn(mockRoot);

    MainController mockMainController = mock(MainController.class);
    when(mockMainController.getInputManager()).thenReturn(mockInputManager);

    CountDownLatch latch = new CountDownLatch(1);

    Platform.runLater(() -> {
      gameScreenView = new GameScreenView(
          mockMainController,
          mockGameState,
          "src/test/resources/MockGame",
          false
      );

      // Reflectively call the private method
      try {
        var method = GameScreenView.class.getDeclaredMethod("handleReturnToMainMenu");
        method.setAccessible(true);
        method.invoke(gameScreenView);
      } catch (Exception e) {
        fail("Reflection failed: " + e.getMessage());
      }

      latch.countDown();
    });

    if (!latch.await(2, TimeUnit.SECONDS)) {
      fail("Timeout waiting for JavaFX to process");
    }

    verify(mockMainController).showSplashScreen();
    assertFalse(mockRoot.getChildren().contains(gameScreenView.getRoot()));
  }


}
