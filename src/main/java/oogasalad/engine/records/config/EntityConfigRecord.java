package oogasalad.engine.records.config;

import java.util.List;
import oogasalad.engine.records.config.model.EntityPropertiesRecord;

public record EntityConfigRecord(String name, EntityPropertiesRecord entityProperties,
                                 List<ModeConfigRecord> modes) {

}
