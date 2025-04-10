package oogasalad.engine.config.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.file.Path;
import java.util.List;
import oogasalad.engine.config.ConfigException;
import oogasalad.engine.config.ConfigModel;

/**
 * Interface for saving configuration data to a file. Implementations of this interface should
 * handle the process of writing configuration data given in {@link ConfigModel} to a specified file
 * format and location.
 *
 * @author Will He
 */
public interface ConfigSaver {
  void saveGameConfig(ObjectNode gameConfigJson, Path gameFolder);
  void saveLevel(String levelName, ObjectNode levelJson, Path gameFolder);
  void saveEntityType(String entityName, ObjectNode entityJson, Path gameFolder);
}
