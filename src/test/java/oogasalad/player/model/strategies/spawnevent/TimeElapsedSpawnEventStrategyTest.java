package oogasalad.player.model.strategies.spawnevent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.model.SpawnEventRecord;
import oogasalad.engine.records.model.ConditionRecord;
import oogasalad.player.model.GameMap;
import oogasalad.player.model.GameStateInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimeElapsedSpawnEventStrategyTest {

  private TimeElapsedSpawnEventStrategy strategy;

  private GameContextRecord contextWithTime(double timeElapsed) {
    GameStateInterface mockState = mock(GameStateInterface.class);
    when(mockState.getTimeElapsed()).thenReturn(timeElapsed);
    return new GameContextRecord(new GameMap(10, 10), mockState);
  }

  private SpawnEventRecord spawnEventWithParams(String amountValue, boolean forSpawn) {
    ConditionRecord spawnCondition = new ConditionRecord("TimeElapsed", Map.of("amount", amountValue));
    ConditionRecord despawnCondition = new ConditionRecord("TimeElapsed", Map.of("amount", amountValue));
    return new SpawnEventRecord(
        null,
        forSpawn ? spawnCondition : new ConditionRecord("TimeElapsed", Map.of()),
        0, 0, "test",
        forSpawn ? new ConditionRecord("TimeElapsed", Map.of()) : despawnCondition
    );
  }

  @BeforeEach
  void setup() {
    strategy = new TimeElapsedSpawnEventStrategy();
  }

  @Test
  void shouldSpawn_timeAboveThreshold_returnsTrue() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("5", true);
    GameContextRecord context = contextWithTime(10.0);
    assertTrue(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldSpawn_timeBelowThreshold_returnsFalse() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("10", true);
    GameContextRecord context = contextWithTime(5.0);
    assertFalse(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldSpawn_missingAmountParam_returnsFalse() {
    SpawnEventRecord spawnEvent = new SpawnEventRecord(
        null,
        new ConditionRecord("TimeElapsed", Map.of()),
        0, 0, "test",
        new ConditionRecord("TimeElapsed", Map.of())
    );
    GameContextRecord context = contextWithTime(10.0);
    assertFalse(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldSpawn_invalidAmountParam_returnsFalse() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("invalid", true);
    GameContextRecord context = contextWithTime(10.0);
    assertFalse(strategy.shouldSpawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_timeAboveThreshold_returnsTrue() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("10", false);
    GameContextRecord context = contextWithTime(15.0);
    assertTrue(strategy.shouldDespawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_timeBelowThreshold_returnsFalse() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("10", false);
    GameContextRecord context = contextWithTime(5.0);
    assertFalse(strategy.shouldDespawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_missingAmountParam_returnsFalse() {
    SpawnEventRecord spawnEvent = new SpawnEventRecord(
        null,
        new ConditionRecord("TimeElapsed", Map.of()),
        0, 0, "test",
        new ConditionRecord("TimeElapsed", Map.of())
    );
    GameContextRecord context = contextWithTime(10.0);
    assertFalse(strategy.shouldDespawn(spawnEvent, context));
  }

  @Test
  void shouldDespawn_invalidAmountParam_returnsFalse() {
    SpawnEventRecord spawnEvent = spawnEventWithParams("notAnInt", false);
    GameContextRecord context = contextWithTime(10.0);
    assertFalse(strategy.shouldDespawn(spawnEvent, context));
  }
}
