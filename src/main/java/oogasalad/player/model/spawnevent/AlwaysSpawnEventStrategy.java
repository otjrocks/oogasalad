package oogasalad.player.model.spawnevent;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.model.SpawnEvent;

/**
 * Always return false strategy if you never want an entity to spawn or despawn.
 *
 * @author Owen Jennings
 */
public class AlwaysSpawnEventStrategy implements SpawnEventStrategy {
  // Always return false spawn event strategy.
  @Override
  public boolean shouldSpawn(SpawnEvent spawnEvent, GameContextRecord gameContextRecord) {
    return false;
  }

  @Override
  public boolean shouldDespawn(SpawnEvent spawnEvent, GameContextRecord gameContextRecord) {
    return false;
  }
}
