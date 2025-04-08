package oogasalad.engine.newconfig;

import java.util.List;
import oogasalad.engine.newconfig.model.EntityProperties;

public record EntityConfig(String name, EntityProperties entityProperties, List<ModeConfig> modes) {

}
