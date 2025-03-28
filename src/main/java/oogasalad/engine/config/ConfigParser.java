package oogasalad.engine.config;

public interface ConfigParser {
  ConfigModel loadFromFile(String filepath) throws ConfigParseException;
}