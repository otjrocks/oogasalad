package oogasalad.player.model.spawnevent;

import oogasalad.engine.model.ConditionRecord;
import oogasalad.engine.model.GameMapImpl;
import oogasalad.engine.model.GameStateInterface;
import oogasalad.engine.model.GameStateImpl;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.model.SpawnEventRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBasedSpawnEventStrategyTest {
  // ChatGPT helped in generating these tests.
  private ScoreBasedSpawnEventStrategy strategy;

  private GameContextRecord contextWithScore(int score) {
    GameStateInterface state = new GameStateImpl(0);
    state.updateScore(score);
    return new GameContextRecord(new GameMapImpl(10, 10), state);
  }

  private SpawnEventRecord spawnEventWithParams(String amountValue, boolean forSpawn) {
    ConditionRecord spawnCondition = new ConditionRecord("Score", Map.of("amount", amountValue));
    ConditionRecord despawnCondition = new ConditionRecord("Score", Map.of("amount", amountValue));
    return new SpawnEventRecord(null,
        forSpawn ? spawnCondition : new ConditionRecord("Score", Map.of()),
        0, 0, "test",
        forSpawn ? new ConditionRecord("Score", Map.of()) : despawnCondition);
  }

  @BeforeEach
  void setup() {
    strategy = new ScoreBasedSpawnEventStrategy();
  }

  @Test
  void shouldSpawn_scoreAboveThreshold_returnsTrue() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("50", true);
    GameContextRecord context = contextWithScore(60);
    assertTrue(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldSpawn_scoreBelowThreshold_returnsFalse() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("100", true);
    GameContextRecord context = contextWithScore(50);
    assertFalse(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldSpawn_missingAmountParam_returnsFalse() {
    SpawnEventRecord spawnEvent = new SpawnEventRecord(null,
        new ConditionRecord("Score", Map.of()), 0, 0, "test", new ConditionRecord("Score", Map.of()));
    GameContextRecord context = contextWithScore(999);
    assertFalse(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldSpawn_invalidAmountParam_returnsFalse() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("invalid", true);
    GameContextRecord context = contextWithScore(999);
    assertFalse(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_scoreAboveThreshold_returnsTrue() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("100", false);
    GameContextRecord context = contextWithScore(150);
    assertTrue(strategy.shouldDespawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_scoreBelowThreshold_returnsFalse() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("100", false);
    GameContextRecord context = contextWithScore(50);
    assertFalse(strategy.shouldDespawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_missingAmountParam_returnsFalse() {
    SpawnEventRecord spawnEvent = new SpawnEventRecord(null,
        new ConditionRecord("Score", Map.of()), 0, 0, "test", new ConditionRecord("Score", Map.of()));
    GameContextRecord context = contextWithScore(999);
    assertFalse(strategy.shouldDespawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_invalidAmountParam_returnsFalse() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("notAnInt", false);
    GameContextRecord context = contextWithScore(999);
    assertFalse(strategy.shouldDespawn(spawnEvent, context));
  }
}
