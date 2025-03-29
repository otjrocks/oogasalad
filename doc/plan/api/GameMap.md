## GameMap

### Overview

The GameMap API is designed to facilitate the management of various static and dynamic entities within the game. The GameMap serves as a way to store the relative locations of entities in a custom Pac-Man game. Instead of using a strictly traditional grid-based approach, this API will support a hybrid model, combining a grid-based representation for game logic with coordinate-based rendering for smooth animations. Various classes will interact with the API to add and remove entities from the game.

The GameMap interface encourages extension by being easily extendable and allowing multiple ways to implement how Entities are spatially located within the game.

**Adherence to SOLID and Object-Oriented Design:**

- Single Responsibility Principle: Each method in this API handles a specific operation related to the GameMap.  
- Open-Closed Principle: New entities, movement handling, and types of entity collision can be implemented without requiring a redesign of our GameMap interface.  
- Liskov Substitution Principle: Entities stored within the GameMap data structures can be easily replaced with alternative implementations without breaking the API.  
- Interface Segregation Principle: This GameMap focuses on the core methods required across all GameMaps without including multiple unused methods.  
- Dependency Inversion Principle: High-level modules depend on abstractions, making the system adaptable. The GameMap does not define how movement and collisions on the map should be handled; rather, it relies on various Strategy design patterns.

### Classes

The game map will primarily interact with the Entity interface. See the Entity API for more information. The Entity interface will store the floating point precious location of the Entity in the game.

View the GameMap.java interface implementation in the API documentation directory.

### Details / Use Cases

This API will primarily interact with the Entity classes. Both the game player and game editor will add and remove entities from the map as necessary and the class that reads and writes the current map configuration to a save file will also have to interact with this class.

Here are some specific Use Cases that will need to interact with/implement this API:

* Use Case: GHOST-1: Based on configuration, create ghost  
* Use Case: GHOST-MOVT-1: UCS: Implementation of various path algorithms requires getting the elements in the map and determining what constitutes a path.  
* Use Case: GHOST-MOVT-2: DFS  
* Use Case: GHOST-MOVT-3: A\* Search  
* Use Case: MAPGENERATION-1: Load Map: Loading the GameMap will require interactions with the FileHandler and the configuration files  
* Use Case: MAPGENERATION-2: Level Loading: Loading of a level requires the correct placement of entities within the map  
* Use Case: Entity collisions will need to be determined based on the X and Y values of entities in the map. 

### Considerations

The API for GameMap is designed to be flexible and allow for both grid-based maps/levels and coordinate-based rendering. This hybrid approach balances structured game logic (pathfinding, movement constraints) with smooth, modern animations.

Notable, this interface does not force a grid-based implementation, allowing for future flexibility in case the program needs to support other implementations.

However, this flexibility introduces some trade-offs:

* Pathfinding & AI: Ghost movement and AI algorithms (e.g., BFS, A) will primarily rely on a grid representation for efficiency.  
* Movement & Rendering: Pac-Man and ghost entities will have precise X, Y coordinates for smooth animations but will still respect the grid for valid movement paths.  
* Performance: The additional complexity of supporting both representations may impact performance and require careful optimization.  
* This API design aims to combine the best aspects of classic grid-based game logic with modern rendering techniques to ensure an intuitive and smooth gameplay experience.

