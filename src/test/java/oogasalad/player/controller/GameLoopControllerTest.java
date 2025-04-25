package oogasalad.player.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.ConfigModelRecord;
import oogasalad.engine.records.config.model.ParsedLevelRecord;
import oogasalad.engine.records.config.model.losecondition.LivesBasedConditionRecord;
import oogasalad.engine.records.config.model.wincondition.EntityBasedConditionRecord;
import oogasalad.engine.records.model.GameSettingsRecord;
import oogasalad.player.model.GameMap;
import oogasalad.player.model.GameMapInterface;
import oogasalad.player.model.GameState;
import oogasalad.player.view.GameMapView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import util.DukeApplicationTest;

@ExtendWith(MockitoExtension.class)
class GameLoopControllerTest extends DukeApplicationTest {

  // I used ChatGPT to assist in writing the test methods below.
  private GameLoopController gameLoopController;
  private GameMapView gameMapView;
  private GameMapInterface gameMap;

  @BeforeEach
  void setUp() {
    gameMap = Mockito.spy(new GameMap(10, 10));
    GameState gameState = new GameState(5);
    GameContextRecord gameContext = new GameContextRecord(gameMap, gameState);

    ConfigModelRecord mockConfigModel = mock(ConfigModelRecord.class);
    when(mockConfigModel.winCondition()).thenReturn(new EntityBasedConditionRecord("dot"));
    when(mockConfigModel.loseCondition()).thenReturn(new LivesBasedConditionRecord());
    when(mockConfigModel.settings()).thenReturn(new GameSettingsRecord(1.0, 1, 1));

    gameMapView = Mockito.spy(new GameMapView(gameContext, mockConfigModel, "data/games/BasicPacMan/"));
    gameLoopController = Mockito.spy(
        new GameLoopController(mockConfigModel, gameContext, gameMapView, mock(
            ParsedLevelRecord.class)));
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
