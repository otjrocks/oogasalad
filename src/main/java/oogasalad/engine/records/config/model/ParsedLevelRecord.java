package oogasalad.engine.records.config.model;

import java.util.List;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.model.MapInfoRecord;
import oogasalad.engine.records.model.ModeChangeEventRecord;

public record ParsedLevelRecord(List<EntityPlacement> placements, MapInfoRecord mapInfo,
                                List<SpawnEventRecord> spawnEvents, List<ModeChangeEventRecord> modeChangeEvents) {

}

