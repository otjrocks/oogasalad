package oogasalad.engine.records.newconfig.model;

import java.util.List;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.MapInfo;

public record ParsedLevel(List<EntityPlacement> placements, MapInfo mapInfo) {}

