package oogasalad.player.controller;

import oogasalad.engine.model.GameMap;
import oogasalad.engine.model.GameMapImpl;
import oogasalad.engine.model.GameStateImpl;
import oogasalad.engine.records.GameContext;
import oogasalad.player.view.GameMapView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameLoopControllerTest extends DukeApplicationTest {

  // I used ChatGPT to assist in writing the test methods below.
  private GameLoopController gameLoopController;
  private GameMapView gameMapView;
  private GameMap gameMap;

  @BeforeEach
  void setUp() {
    gameMap = Mockito.spy(new GameMapImpl(10, 10));
    GameContext gameContext = new GameContext(gameMap, new GameStateImpl(5));
    gameMapView = Mockito.spy(new GameMapView(gameContext));
    gameLoopController = Mockito.spy(new GameLoopController(gameContext, gameMapView));
  }

  @Test
  void pauseGame_WhenCalledTwice_DoesNotThrowException() {
    gameLoopController.pauseGame();
    assertDoesNotThrow(() -> gameLoopController.pauseGame());
  }

  @Test
  void pauseGame_EnsureNoUpdateCallsAfterPause_Success() throws InterruptedException {
    // Let the loop run for a bit to ensure it's actively updating
    Thread.sleep(100);

    // Pause the game
    gameLoopController.pauseGame();

    // Clear previous invocations to start fresh
    clearInvocations(gameMap, gameMapView);

    // Wait to ensure loop would've had time to run again if not paused
    Thread.sleep(100);

    // Verify no further updates happened after pause
    verify(gameMap, times(0)).update();
    verify(gameMapView, times(0)).update();
  }

  @Test
  void resumeGame_AfterPause_DoesNotThrowException() {
    gameLoopController.pauseGame();
    assertDoesNotThrow(() -> gameLoopController.resumeGame());
  }

  @Test
  void gameLoop_AfterShortDelay_UpdatesModelAndView() throws InterruptedException {
    Thread.sleep(100);
    verify(gameMap, atLeastOnce()).update();
    verify(gameMapView, atLeastOnce()).update();
  }

}
