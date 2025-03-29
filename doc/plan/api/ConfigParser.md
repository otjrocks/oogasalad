# Config Parser

Overview  
The Configuration Parser API is designed to provide an extensible mechanism for reading and interpreting configuration files that define various aspects of the generated PacMan game such as metadata, initial states and entities and outcome strategies.

**SOLID / Object Oriented Design**

- Single Responsibility Principle  
  - Each interface will be responsible for a single aspect of configuration parsing  
- Open/Closed Principle  
  - Allow for new configuration files to be add without modifying existing code  
- Interface Segregation Principle  
  - Split functionality into multiple smaller methods

Classes  
**public interface ConfigParser**

- Map\<String, String\> parseConfig(String filePath) throws IOException  
- boolean containsElement(String key)  
- boolean getAllKeys()  
- Optional\<String\> getSetting(String key)  
- void validateConfig(Map\<String, String\> config) throws InvalidConfigException  
- boolean isValidValue(String key, String value);

**Custom Exception**

- public class InvalidConfigException extends Exception

### Details / Use Cases

This API will interact directly with both the Authoring Environment and the Game Player for loading in game state representations for either editing or playing configuration files. 

General Use Cases:

* Parsing the configuration file that defines maze structure, ghost AI strategies and initial location of various dynamic entities like power-ups  
* Validating configuration data before game initialization  
* Providing a flexible framework for adding new configuration types

Some specific potential use cases this API would interact with are:

* CHARACTER-1: Load PacMan  
* GHOST-1: Based on configuration, create ghost  
* GAMESTATISTICS-GAMESTATE-2: Save and load GameState  
* MAPGENERATION-1: Load Map  
* POWERUP-1: Based on configuration, create powerup

Considerations

We want to create a flexible parsing structure since we don’t know exactly what elements we will need/want to customize in the future. Creating generic parsing and validating methods will allow us to include as many potential elements as possible without having to implement major changes within the code structure. This design assumes the user has a well structured JSON file but more robust format error handling could be included.

