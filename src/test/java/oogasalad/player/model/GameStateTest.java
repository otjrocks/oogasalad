package oogasalad.player.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import oogasalad.engine.records.model.GameSettingsRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateTest {

  private GameState gameState;

  @BeforeEach
  void setUp() {
    GameSettingsRecord gameSettings = new GameSettingsRecord(1.0, 5, 5);
    gameState = new GameState(gameSettings);
  }

  @Test
  void updateScore_addSubScore_updatesScoreCorrectly() {
    gameState.updateScore(10);
    assertEquals(15, gameState.getScore());

    gameState.updateScore(-3);
    assertEquals(12, gameState.getScore());
  }

  @Test
  void updateLives_addSubLives_updatesLivesCorrectly() {
    gameState.updateLives(-1);
    assertEquals(4, gameState.getLives());

    gameState.updateLives(2);
    assertEquals(6, gameState.getLives());
  }
}
