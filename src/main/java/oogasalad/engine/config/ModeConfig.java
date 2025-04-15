package oogasalad.engine.config;

import oogasalad.engine.records.config.ImageConfig;
import oogasalad.engine.records.config.model.EntityProperties;

public record ModeConfig(String name, EntityProperties entityProperties, ImageConfig image) {

}

