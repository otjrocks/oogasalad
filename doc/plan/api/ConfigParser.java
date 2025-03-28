package oogasalad;

/**
 * Represents a general configuration file parser for the Pacman game creator
 */
public interface ConfigParser {
    /**
     * Parses a configuration file and returns a structured representation.
     * @param filePath: the path to the configuration file
     */
    Map<String, String> parseConfig(String filePath) throws IOException;

    /**
     * Checks if a given key exists in the configuration
     * @param key: the configuration key
     */
    boolean containsKey(String key);

    /**
     * Retrieves all keys available in the configuration
     */
    Set<String> getAllKeys();

    /**
     * Retrieves a specific setting by key.
     * @param key: the setting key
     */
    Optional<String> getSetting(String key);

    /**
     * Validates the provided configuration map
     * @param config: a map of configuration properties
     */
    void validateConfig(Map<String, String> config) throws InvalidConfigException;

    /**
     * Checks if a specific value adheres to expected constraints
     * @param key: the configuration key
     * @param value: the configuration value
     */
    boolean isValidValue(String key, String value);

    /**
     * Exception thrown when an invalid configuration is encountered.
     */
    public class InvalidConfigException extends Exception {
        public InvalidConfigException(String message) {
            super(message);
        }
    }
}