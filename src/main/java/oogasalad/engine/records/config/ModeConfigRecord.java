package oogasalad.engine.records.config;

import oogasalad.engine.records.config.model.EntityPropertiesRecord;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;

public record ModeConfigRecord(String name, EntityPropertiesRecord entityProperties,
                               ControlConfigInterface controlConfig, ImageConfigRecord image,
                               Double movementSpeed) {

}

