package oogasalad.player.view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import oogasalad.engine.controller.MainController;
import oogasalad.player.model.GameStateInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameViewTest {

  private GamePlayerView gamePlayerView;
  private GameView gameView;

  private MainController mockMainController;
  private GameStateInterface mockGameState;

  static {
    new JFXPanel(); // JavaFX init
  }

  @BeforeEach
  void setup() {
    mockMainController = mock(MainController.class);
    mockGameState = mock(GameStateInterface.class);

    // Uses test config file in mock directory
    gamePlayerView = new GamePlayerView(mockMainController, mockGameState, "MockGame", false, "src/test/resources/");
    gameView = gamePlayerView.getGameView();
  }

  @Test
  void testRootIsInitializedCorrectly() {
    StackPane root = gameView.getRoot();
    assertNotNull(root, "GameView root should not be null");
    assertFalse(root.getChildren().isEmpty(), "GameView root should contain game map and controls");
    assertTrue(root.getStyleClass().contains("game-view"), "GameView root should have correct style class");
  }

  @Test
  void testPauseAndResumeDoNotThrow() {
    assertDoesNotThrow(() -> {
      gameView.pauseGame();
      gameView.resumeGame();
    }, "Pause and resume should not throw exceptions");
  }

  @Test
  void testSetNextLevelActionAndClick() {
    Runnable mockAction = mock(Runnable.class);
    gameView.setNextLevelAction(mockAction);

    // Simulate next level trigger manually (if visible)
    gameView.getRoot().getChildren().stream()
        .filter(node -> node instanceof Button && ((Button) node).getText().contains("Next"))
        .findFirst()
        .ifPresent(node -> {
          Button button = (Button) node;
          button.setVisible(true);
          button.fire();
          verify(mockAction).run();
        });
  }

  @Test
  void testSetResetActionAndClick() {
    Runnable mockAction = mock(Runnable.class);
    gameView.setResetAction(mockAction);

    // Simulate reset trigger manually (if visible)
    gameView.getRoot().getChildren().stream()
        .filter(node -> node instanceof Button && ((Button) node).getText().contains("Reset"))
        .findFirst()
        .ifPresent(node -> {
          Button button = (Button) node;
          button.setVisible(true);
          button.fire();
          verify(mockAction).run();
        });
  }

  @Test
  void testShowEndMessageDisplaysLabelAndButtons() throws Exception {
    // Access the private method showEndMessage(boolean, boolean)
    var method = GameView.class.getDeclaredMethod("showEndMessage", boolean.class, boolean.class);
    method.setAccessible(true);

    // Simulate a non-final level win
    method.invoke(gameView, true, false); // gameWon = true, isFinalLevel = false

    // Now check the label and buttons
    boolean labelVisible = false;
    boolean nextButtonVisible = false;
    boolean resetButtonHidden = false;

    for (var node : gameView.getRoot().getChildren()) {
      if (node instanceof Label label && label.isVisible()) {
        labelVisible = true;
        assertFalse(label.getText().isEmpty(), "End message should be set");
      } else if (node instanceof Button btn) {
        if (btn.getText().contains("Next")) {
          nextButtonVisible = btn.isVisible();
        } else if (btn.getText().contains("Reset")) {
          resetButtonHidden = !btn.isVisible();
        }
      }
    }

    assertTrue(labelVisible, "End label should be visible");
    assertTrue(nextButtonVisible, "Next Level button should be visible on win");
    assertTrue(resetButtonHidden, "Reset button should be hidden on non-final level win");
  }

}
