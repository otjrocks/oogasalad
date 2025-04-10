package oogasalad.engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.file.Path;
import oogasalad.engine.config.api.ConfigSaver;

public class JsonConfigSaver implements ConfigSaver {

  private final ObjectMapper mapper;

  public JsonConfigSaver() {
    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

  @Override
  public void saveGameConfig(ObjectNode config, Path folder) {
    writeJson(config, folder.resolve("gameConfig.json"));
  }

  @Override
  public void saveLevel(String name, ObjectNode config, Path folder) {
    writeJson(config, folder.resolve(name + ".json"));
  }

  @Override
  public void saveEntityType(String name, ObjectNode config, Path folder) {
    writeJson(config, folder.resolve(name.toLowerCase() + ".json"));
  }

  private void writeJson(ObjectNode config, Path path) {
    try {
      mapper.writeValue(path.toFile(), config);
    } catch (IOException e) {
      throw new RuntimeException("Failed to write: " + path, e);
    }
  }
}
