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
    new JFXPanel();
  }

  @BeforeEach
  void setup() {
    mockMainController = mock(MainController.class);
    mockGameState = mock(GameStateInterface.class);

    // Use test resource directory
    runAsJFXAction(() -> gamePlayerView = new GamePlayerView(
        mockMainController,
        mockGameState,
        "MockGame",
        false,
        "src/test/resources/"
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
      // Call private resetGame method via reflection
      try {
        var method = GamePlayerView.class.getDeclaredMethod("resetGame", LevelController.class);
        method.setAccessible(true);
        method.invoke(gamePlayerView, mockLevelController);
      } catch (Exception e) {
        fail("Failed to call resetGame via reflection: " + e.getMessage());
      }
    });

    // Verify interactions
    verify(mockGameState).resetTimeElapsed();
    verify(mockLevelController).resetAndUpdateConfig();

    StackPane pane = gamePlayerView.getPane();
    assertFalse(pane.getChildren().isEmpty(), "Pane should contain GameView root after reset");
  }

  @Test
  void loadNextLevel_whenHasNextLevel_shouldIncrementAndReload() throws Exception {
    LevelController mockLevelController = mock(LevelController.class);
    when(mockLevelController.hasNextLevel()).thenReturn(true);

    runAsJFXAction(() -> {
      try {
        var method = GamePlayerView.class.getDeclaredMethod("loadNextLevel", LevelController.class);
        method.setAccessible(true);
        method.invoke(gamePlayerView, mockLevelController);
      } catch (Exception e) {
        fail("Failed to call loadNextLevel via reflection: " + e.getMessage());
      }
    });

    verify(mockLevelController).hasNextLevel();
    verify(mockLevelController).incrementAndUpdateConfig();
    StackPane pane = gamePlayerView.getPane();
    assertFalse(pane.getChildren().isEmpty(), "Pane should contain GameView root after loading next level");
  }

  @Test
  void loadNextLevel_whenNoNextLevel_shouldNotChangePane() throws Exception {
    LevelController mockLevelController = mock(LevelController.class);
    when(mockLevelController.hasNextLevel()).thenReturn(false);

    runAsJFXAction(() -> {
      try {
        var method = GamePlayerView.class.getDeclaredMethod("loadNextLevel", LevelController.class);
        method.setAccessible(true);
        method.invoke(gamePlayerView, mockLevelController);
      } catch (Exception e) {
        fail("Failed to call loadNextLevel via reflection: " + e.getMessage());
      }
    });

    verify(mockLevelController).hasNextLevel();
    verify(mockLevelController, never()).incrementAndUpdateConfig(); // should not increment
  }

}
