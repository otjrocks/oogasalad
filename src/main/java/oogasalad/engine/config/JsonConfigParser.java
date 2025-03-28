package oogasalad.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonConfigParser implements ConfigParser {

  private final ObjectMapper mapper;

  public JsonConfigParser() {
    this.mapper = new ObjectMapper();
  }

  public ConfigModel loadFromFile(String filepath) throws ConfigParseException {
    try {
      return mapper.readValue(new File(filepath), ConfigModel.class);
    } catch (IOException e) {
      throw new ConfigParseException("Failed to parse config file: " + filepath, e);
    }
  }
}
