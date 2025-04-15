package oogasalad.engine.records.config;

import java.util.List;
import oogasalad.engine.config.ModeConfig;
import oogasalad.engine.records.config.model.EntityProperties;

public record EntityConfig(String name, EntityProperties entityProperties, List<ModeConfig> modes) {

}
