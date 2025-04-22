package oogasalad.player.view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.VBox;
import oogasalad.engine.controller.MainController;
import oogasalad.player.model.GameStateInterface;
import oogasalad.player.view.GameScreenView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameScreenViewTest {

  private MainController mockMainController;
  private GameStateInterface mockGameState;

  private GameScreenView gameScreenView;

  // JavaFX init
  static {
    new JFXPanel(); // Initializes JavaFX Toolkit
  }

  @BeforeEach
  void setup() {
    mockMainController = mock(MainController.class);
    mockGameState = mock(GameStateInterface.class);

    when(mockGameState.getScore()).thenReturn(100);
    when(mockGameState.getLives()).thenReturn(3);

    // UI creation must run on JavaFX thread
    Platform.runLater(() -> {
      gameScreenView = new GameScreenView(mockMainController, mockGameState, "MockGame", false,
          "src/test/resources/");
    });

    try {
      Thread.sleep(100); // Allow JavaFX thread to finish init
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  @Test
  void testRootIsNotNullAndHasChildren() {
    VBox root = gameScreenView.getRoot();
    assertNotNull(root, "Root VBox should not be null");
    assertTrue(root.getChildren().size() >= 2, "Root VBox should contain HUD and GamePane");
  }

  @Test
  void testHudUpdatesWhenScoreChanges() {
    Platform.runLater(() -> {
      // Simulate a score change
      when(mockGameState.getScore()).thenReturn(200);

      // Manually trigger HUD update check
      gameScreenView.getRoot().requestLayout(); // optional for visuals
    });

    // Wait a bit for the JavaFX thread to process
    try {
      Thread.sleep(150);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    // No direct HUD assertion unless you expose the HudView or use a spy
    // To properly test `hudView.update()`, you may need to spy on HudView or expose it for testing
    // This test is mostly structural unless internals are refactored
  }
}

