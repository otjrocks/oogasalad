package oogasalad.player.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import oogasalad.engine.controller.MainController;
import oogasalad.player.controller.GameLoopController;
import oogasalad.player.model.GameStateInterface;
import org.junit.jupiter.api.Test;
import org.testfx.util.WaitForAsyncUtils;
import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for GameView component using DukeApplicationTest to safely test JavaFX UI elements.
 */
public class GameViewTest extends DukeApplicationTest {

  private GameView gameView;

  @Override
  public void start(Stage stage) {
    MainController mockMainController = mock(MainController.class);
    GameStateInterface mockGameState = mock(GameStateInterface.class);

    GamePlayerView gamePlayerView = new GamePlayerView(
        mockMainController,
        mockGameState,
        "MockGame",
        false,
        "src/test/resources/"
    );
    gameView = gamePlayerView.getGameView();

    // 🛠 Wrap gameView.getRoot() to avoid duplicate parenting
    StackPane wrapper = new StackPane();
    wrapper.getChildren().add(gameView.getRoot());

    Scene scene = new Scene(wrapper, 1200, 800);
    stage.setScene(scene);
    stage.show();
  }



  @Test
  void getRoot_initialization_initializedRoot() {
    StackPane root = gameView.getRoot();
    assertNotNull(root);
    assertFalse(root.getChildren().isEmpty());
  }


  @Test
  void setNextLevel_setOnAction_setNextLevel() {
    Runnable mockAction = mock(Runnable.class);
    runAsJFXAction(() -> {
      gameView.setNextLevelAction(mockAction);
      simulateEndGameState(gameView, true, false);
    });

    WaitForAsyncUtils.waitForFxEvents();

    Button nextButton = lookup("#nextLevelButton").queryButton();
    assertTrue(nextButton.isVisible());

    clickOn(nextButton);
    verify(mockAction).run();
  }


  @Test
  void setResetLevel_setOnAction_setResetLevel() {
    Runnable mockAction = mock(Runnable.class);
    runAsJFXAction(() -> {
      gameView.setResetAction(mockAction);
      simulateEndGameState(gameView, false, true);
    });

    WaitForAsyncUtils.waitForFxEvents();

    Button resetButton = lookup("#resetButton").queryButton();
    assertTrue(resetButton.isVisible());

    clickOn(resetButton);
    verify(mockAction).run();
  }


  @Test
  void simulateEndGameState_setOnAction_simulateEndGameState() {
    runAsJFXAction(() -> simulateEndGameState(gameView, true, false));

    WaitForAsyncUtils.waitForFxEvents();

    Label label = lookup("#endLabel").query();
    assertTrue(label.isVisible());

    Button nextButton = lookup("#nextLevelButton").queryButton();
    assertTrue(nextButton.isVisible());

    Button resetButton = lookup("#resetButton").queryButton();
    assertFalse(resetButton.isVisible());
  }


  // with help from chatGPT
  @Test
  void pauseGame_onCall_callsLoopControllerPauseGame() {
    runAsJFXAction(() -> {
      GameLoopController controller = getGameLoopControllerViaReflection();
      GameLoopController spyController = spy(controller);
      setGameLoopControllerViaReflection(spyController);

      gameView.pauseGame();
      verify(spyController).pauseGame();
    });
  }

  @Test
  void resumeGame_onCall_callsLoopControllerResumeGame() {
    runAsJFXAction(() -> {
      GameLoopController controller = getGameLoopControllerViaReflection();
      GameLoopController spyController = spy(controller);
      setGameLoopControllerViaReflection(spyController);

      gameView.resumeGame();
      verify(spyController).resumeGame();
    });
  }

  private GameLoopController getGameLoopControllerViaReflection() {
    try {
      var field = GameView.class.getDeclaredField("myGameLoopController");
      field.setAccessible(true);
      return (GameLoopController) field.get(gameView);
    } catch (Exception e) {
      fail("Failed to access GameLoopController: " + e.getMessage());
      return null;
    }
  }

  private void setGameLoopControllerViaReflection(GameLoopController controller) {
    try {
      var field = GameView.class.getDeclaredField("myGameLoopController");
      field.setAccessible(true);
      field.set(gameView, controller);
    } catch (Exception e) {
      fail("Failed to set GameLoopController: " + e.getMessage());
    }
  }


  private void simulateEndGameState(GameView view, boolean gameWon, boolean isFinalLevel) {
    try {
      var method = GameView.class.getDeclaredMethod("showEndMessage", boolean.class, boolean.class);
      method.setAccessible(true);
      method.invoke(view, gameWon, isFinalLevel);
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}

