package oogasalad.player.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateTest {

  private GameState gameState;

  @BeforeEach
  void setUp() {
    gameState = new GameState(5);
  }

  @Test
  void updateScore_addSubScore_updatesScoreCorrectly() {
    gameState.updateScore(10);
    assertEquals(10, gameState.getScore());

    gameState.updateScore(-3);
    assertEquals(7, gameState.getScore());
  }

  @Test
  void updateLives_addSubLives_updatesLivesCorrectly() {
    gameState.updateLives(-1);
    assertEquals(4, gameState.getLives());

    gameState.updateLives(2);
    assertEquals(6, gameState.getLives());
  }
}
