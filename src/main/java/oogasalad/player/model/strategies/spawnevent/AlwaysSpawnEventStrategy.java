package oogasalad.player.model.strategies.spawnevent;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.config.model.SpawnEventRecord;

/**
 * Always return false strategy if you never want an entity to spawn or despawn.
 *
 * @author Owen Jennings
 */
public class AlwaysSpawnEventStrategy implements SpawnEventStrategyInterface {

  // Always return false spawn event strategy.
  @Override
  public boolean shouldSpawn(SpawnEventRecord spawnEvent, GameContextRecord gameContextRecord) {
    return false;
  }

  @Override
  public boolean shouldDespawn(SpawnEventRecord spawnEvent, GameContextRecord gameContextRecord) {
    return false;
  }
}
