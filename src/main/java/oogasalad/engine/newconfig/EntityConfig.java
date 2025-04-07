package oogasalad.engine.newconfig;

import java.util.List;
import oogasalad.engine.newconfig.model.EntityProperties;

public record EntityConfig(String name, double initialX, double initialY, List<String> blocks,
                           EntityProperties entityProperties, List<ModeConfig> modes) {

}
