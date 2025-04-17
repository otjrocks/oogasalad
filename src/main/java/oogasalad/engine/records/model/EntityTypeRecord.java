package oogasalad.engine.records.model;

import java.util.List;
import java.util.Map;
import oogasalad.engine.records.config.ModeConfigRecord;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;

/**
 * A record storing key information about an Entity type. This record maps the string type name of
 * an Entity to strings representing its various design strategies associated with it.
 *
 * @author Will He, Owen Jennings
 */
public record EntityTypeRecord(
    String type,
    ControlConfigInterface controlConfig,
    Map<String, ModeConfigRecord> modes,
    List<String> blocks,
    double speed
) {

}

