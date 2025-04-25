package oogasalad.player.view;

import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.StackPane;
import oogasalad.engine.controller.MainController;
import oogasalad.player.controller.LevelController;
import oogasalad.player.model.GameStateInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for GamePlayerView to ensure it initializes GameView and updates GameState correctly.
 */
class GamePlayerViewTest extends DukeApplicationTest {

  private MainController mockMainController;
  private GameStateInterface mockGameState;
  private GamePlayerView gamePlayerView;

  static {
    new JFXPanel(); // initialize JavaFX
  }

  @BeforeEach
  void setup() {
    mockMainController = mock(MainController.class);
    mockGameState = mock(GameStateInterface.class);

    runAsJFXAction(() -> gamePlayerView = new GamePlayerView(
        mockMainController,
        mockGameState,
        "src/test/resources/MockGame",
        false
    ));
  }

  @Test
  void getPane_paneWithValues_notNullWithView() {
    StackPane pane = gamePlayerView.getPane();
    assertNotNull(pane, "Root StackPane should not be null");
    assertFalse(pane.getChildren().isEmpty(), "StackPane should contain GameView root node");
    assertNotNull(gamePlayerView.getGameView(), "GameView should be initialized");
    assertNotNull(gamePlayerView.getGameView().getRoot(), "GameView root should not be null");
  }

  @Test
  void getGameView_paneWithProperValues_returnsNotNullGameView() {
    GameView gv = gamePlayerView.getGameView();
    assertNotNull(gv);
    assertInstanceOf(GameView.class, gv, "Returned object should be instance of GameView");
  }

  @Test
  void resetGame_resetsStateAndReloadsView() throws Exception {
    LevelController mockLevelController = mock(LevelController.class);

    runAsJFXAction(() -> {
      try {
        var method = GamePlayerView.class.getDeclaredMethod("handleResetGame");
        method.setAccessible(true);
        method.invoke(gamePlayerView);
      } catch (Exception e) {
        fail("Failed to call handleResetGame via reflection: " + e.getMessage());
      }
    });

    // No direct verification because GameSessionManager is used, but you can verify pane is updated
    StackPane pane = gamePlayerView.getPane();
    assertFalse(pane.getChildren().isEmpty(), "Pane should contain GameView root after reset");
  }

  @Test
  void handleNextLevel_whenHasNextLevel_shouldAdvanceAndReload() {
    when(mockGameState.getScore()).thenReturn(100); // Mock the score

    runAsJFXAction(() -> {
      try {
        var method = GamePlayerView.class.getDeclaredMethod("handleNextLevel");
        method.setAccessible(true);
        method.invoke(gamePlayerView);
      } catch (Exception e) {
        fail("Failed to call handleNextLevel via reflection: " + e.getMessage());
      }
    });

    StackPane pane = gamePlayerView.getPane();
    assertFalse(pane.getChildren().isEmpty(), "Pane should contain GameView root after next level loaded");
  }


  @Test
  void handleNextLevel_whenNoNextLevel_shouldNotCrash() {
    when(mockGameState.getScore()).thenReturn(100); // mock

    runAsJFXAction(() -> {
      try {
        var method = GamePlayerView.class.getDeclaredMethod("handleNextLevel");
        method.setAccessible(true);

        // Simulate calling handleNextLevel twice
        method.invoke(gamePlayerView);
        method.invoke(gamePlayerView); // Simulate advancing past the last level
      } catch (Exception e) {
        fail("Failed to call handleNextLevel via reflection: " + e.getMessage());
      }
    });

    StackPane pane = gamePlayerView.getPane();
    assertFalse(pane.getChildren().isEmpty(), "Pane should still have something even if no more levels");
  }

}
