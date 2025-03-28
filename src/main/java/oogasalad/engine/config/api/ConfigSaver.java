package oogasalad.engine.config.api;

import java.io.IOException;
import java.util.List;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.ConfigModel;

public interface ConfigSaver {
  void saveToFile(ConfigModel config, String filepath) throws ConfigException;
  List<String> getSupportedFileExtensions();
}
