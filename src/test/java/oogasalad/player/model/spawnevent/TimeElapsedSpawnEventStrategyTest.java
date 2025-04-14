package oogasalad.player.model.spawnevent;

import oogasalad.engine.model.Condition;
import oogasalad.engine.model.GameMapImpl;
import oogasalad.engine.model.GameState;
import oogasalad.engine.model.GameStateImpl;
import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.newconfig.model.SpawnEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TimeElapsedSpawnEventStrategyTest {

  // ChatGPT helped generate these tests.
  private TimeElapsedSpawnEventStrategy strategy;

  private GameContextRecord dummyContext() {
    GameState state = new GameStateImpl(0);
    return new GameContextRecord(new GameMapImpl(10, 10), state);
  }

  private SpawnEvent spawnEventWithParams(String amountValue, boolean forSpawn) {
    Condition spawnCondition = new Condition("TimeElapsed", Map.of("amount", amountValue));
    Condition despawnCondition = new Condition("TimeElapsed", Map.of("amount", amountValue));
    return new SpawnEvent(null,
        forSpawn ? spawnCondition : new Condition("TimeElapsed", Map.of()),
        0, 0, "test",
        forSpawn ? new Condition("TimeElapsed", Map.of()) : despawnCondition);
  }

  @BeforeEach
  void setup() {
    strategy = new TimeElapsedSpawnEventStrategy();
  }

  @Test
  void shouldSpawn_timeAboveThreshold_returnsTrue() {
    SpawnEvent spawnEvent = spawnEventWithParams("5", true);
    GameContextRecord context = dummyContext();
    assertTrue(strategy.shouldSpawn(spawnEvent, context, 10.0));
  }

  @Test
  void shouldSpawn_timeBelowThreshold_returnsFalse() {
    SpawnEvent spawnEvent = spawnEventWithParams("10", true);
    GameContextRecord context = dummyContext();
    assertFalse(strategy.shouldSpawn(spawnEvent, context, 5.0));
  }

  @Test
  void shouldSpawn_missingAmountParam_returnsFalse() {
    SpawnEvent spawnEvent = new SpawnEvent(null,
        new Condition("TimeElapsed", Map.of()), 0, 0, "test",
        new Condition("TimeElapsed", Map.of()));
    GameContextRecord context = dummyContext();
    assertFalse(strategy.shouldSpawn(spawnEvent, context, 100.0));
  }

  @Test
  void shouldSpawn_invalidAmountParam_returnsFalse() {
    SpawnEvent spawnEvent = spawnEventWithParams("invalid", true);
    GameContextRecord context = dummyContext();
    assertFalse(strategy.shouldSpawn(spawnEvent, context, 100.0));
  }

  @Test
  void shouldDespawn_timeAboveThreshold_returnsTrue() {
    SpawnEvent spawnEvent = spawnEventWithParams("10", false);
    GameContextRecord context = dummyContext();
    assertTrue(strategy.shouldDespawn(spawnEvent, context, 20.0));
  }

  @Test
  void shouldDespawn_timeBelowThreshold_returnsFalse() {
    SpawnEvent spawnEvent = spawnEventWithParams("10", false);
    GameContextRecord context = dummyContext();
    assertFalse(strategy.shouldDespawn(spawnEvent, context, 5.0));
  }

  @Test
  void shouldDespawn_missingAmountParam_returnsFalse() {
    SpawnEvent spawnEvent = new SpawnEvent(null,
        new Condition("TimeElapsed", Map.of()), 0, 0, "test",
        new Condition("TimeElapsed", Map.of()));
    GameContextRecord context = dummyContext();
    assertFalse(strategy.shouldDespawn(spawnEvent, context, 100.0));
  }

  @Test
  void shouldDespawn_invalidAmountParam_returnsFalse() {
    SpawnEvent spawnEvent = spawnEventWithParams("notAnInt", false);
    GameContextRecord context = dummyContext();
    assertFalse(strategy.shouldDespawn(spawnEvent, context, 100.0));
  }
}
