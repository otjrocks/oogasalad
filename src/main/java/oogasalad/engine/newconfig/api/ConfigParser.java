package oogasalad.engine.newconfig.api;

import oogasalad.engine.newconfig.GameConfig;
import oogasalad.engine.config.ConfigException;

/**
 * Interface for parsing configuration files and converting them into a {@link GameConfig}.
 *
 * <p>Implementations of this interface are responsible for reading configuration data from a file
 * and transforming it into a usable model representation.
 *
 * @author Will He
 */
public interface ConfigParser {

  /**
   * Loads a configuration model from the specified file path.
   *
   * @param filepath the path to the configuration file to be loaded
   * @return a {@code GameConfig} object representing the loaded configuration
   * @throws ConfigException if an error occurs while loading or parsing the configuration file
   */
  GameConfig loadFromFile(String filepath) throws ConfigException;
}