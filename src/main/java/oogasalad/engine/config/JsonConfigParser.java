package oogasalad.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import oogasalad.engine.config.api.ConfigParser;

public class JsonConfigParser implements ConfigParser {

  private final ObjectMapper mapper;

  public JsonConfigParser() {
    this.mapper = new ObjectMapper();
  }

  public ConfigModel loadFromFile(String filepath) throws ConfigException {
    try {
      return mapper.readValue(new File(filepath), ConfigModel.class);
    } catch (IOException e) {
      throw new ConfigException("Failed to parse config file: " + filepath, e);
    }
  }
}
