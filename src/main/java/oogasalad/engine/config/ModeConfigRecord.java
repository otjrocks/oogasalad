package oogasalad.engine.config;

import oogasalad.engine.records.config.ImageConfigRecord;
import oogasalad.engine.records.config.model.EntityPropertiesRecord;

public record ModeConfigRecord(String name, EntityPropertiesRecord entityProperties, ImageConfigRecord image) {

}

