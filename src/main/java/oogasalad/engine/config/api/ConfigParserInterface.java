package oogasalad.engine.config.api;

import oogasalad.engine.exceptions.ConfigException;
import oogasalad.engine.records.config.ConfigModelRecord;

/**
 * Interface for parsing configuration files and converting them into a {@link ConfigModelRecord}.
 *
 * <p>Implementations of this interface are responsible for reading configuration data from a file
 * and transforming it into a usable model representation.
 *
 * @author Will He
 */
public interface ConfigParserInterface {

  /**
   * Loads a configuration model from the specified file path.
   *
   * @param filepath the path to the configuration file to be loaded
   * @return a {@code ConfigModel} object representing the loaded configuration
   * @throws ConfigException if an error occurs while loading or parsing the configuration file
   */
  ConfigModelRecord loadFromFile(String filepath) throws ConfigException;
}