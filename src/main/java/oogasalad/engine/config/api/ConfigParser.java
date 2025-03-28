package oogasalad.engine.config.api;

import oogasalad.engine.config.ConfigModel;
import oogasalad.engine.config.ConfigException;

public interface ConfigParser {
  ConfigModel loadFromFile(String filepath) throws ConfigException;
}