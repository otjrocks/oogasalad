package oogasalad.player.model.spawnevent;

import oogasalad.engine.records.GameContextRecord;
import oogasalad.engine.records.newconfig.model.SpawnEvent;

/**
 * An interface that defines how whether a spawn event should be spawned or despawned in current
 * iteration of game loop.
 *
 * @author Owen Jennings
 */
public interface SpawnEventStrategy {

  public boolean shouldSpawn(SpawnEvent spawnEvent, GameContextRecord gameContextRecord,
      double elapsedTime);

  public boolean shouldDespawn(SpawnEvent spawnEvent, GameContextRecord gameContextRecord,
      double elapsedTime);
}
