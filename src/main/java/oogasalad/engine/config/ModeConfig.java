package oogasalad.engine.config;

import oogasalad.engine.records.newconfig.ImageConfig;
import oogasalad.engine.records.newconfig.model.EntityProperties;

public record ModeConfig(String name, EntityProperties entityProperties, ImageConfig image) {

}

