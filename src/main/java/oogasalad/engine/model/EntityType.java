package oogasalad.engine.model;

import java.util.List;
import java.util.Map;
import oogasalad.engine.config.ModeConfig;

/**
 * A record storing key information about an Entity type. This record maps the string type name of
 * an Entity to strings representing its various design strategies associated with it.
 *
 * @author Will He, Owen Jennings
 */
public record EntityType(String type,
                         String controlType,
                         String effect,
                         Map<String, ModeConfig> modes,
                         List<String> blocks,
                         Map<String, Object> strategyConfig) {

}
