package oogasalad.engine.config.api;

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

  /**
   * Saves the given configuration model to a file at the specified filepath.
   *
   * @param config   the configuration model to be saved
   * @param filepath the path of the file where the configuration will be saved
   * @throws ConfigException if an error occurs during the saving process
   */
  void saveToFile(ConfigModel config, String filepath) throws ConfigException;

  /**
   * Retrieves a list of supported file extensions for saving configuration files.
   *
   * @return a list of strings representing the supported file extensions
   */
  List<String> getSupportedFileExtensions();
}
