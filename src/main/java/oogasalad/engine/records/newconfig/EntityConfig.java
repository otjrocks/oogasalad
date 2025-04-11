package oogasalad.engine.records.newconfig;

import java.util.List;
import oogasalad.engine.config.ModeConfig;
import oogasalad.engine.records.newconfig.model.EntityProperties;

public record EntityConfig(String name, EntityProperties entityProperties, List<ModeConfig> modes) {

}
