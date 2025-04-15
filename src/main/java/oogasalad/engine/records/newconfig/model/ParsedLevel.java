package oogasalad.engine.records.newconfig.model;

import java.util.List;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.MapInfo;
import oogasalad.engine.model.ModeChangeEvent;

public record ParsedLevel(List<EntityPlacement> placements, MapInfo mapInfo,
                          List<SpawnEvent> spawnEvents, List<ModeChangeEvent> modeChangeEvents) {

}

