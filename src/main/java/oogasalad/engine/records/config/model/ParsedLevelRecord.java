package oogasalad.engine.records.config.model;

import java.util.List;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.MapInfoRecord;
import oogasalad.engine.model.ModeChangeEventRecord;

public record ParsedLevelRecord(List<EntityPlacement> placements, MapInfoRecord mapInfo,
                                List<SpawnEventRecord> spawnEvents, List<ModeChangeEventRecord> modeChangeEvents) {

}

