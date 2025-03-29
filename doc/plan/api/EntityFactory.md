## EntityFactory

### Overview

This API overall is to help instantiate game entities (ghosts, pacman, walls, power ups... for a modular entity creation process where the entities can be very extensible from the overall "entity" interface.

**SOLID / Object Oriented Design**

- S: entity initialization / knowing what entities do exist  
- O: new entities (with various strategies) can be introduced without modifying the core factory logic  
- L: since the sub entities implement the Entity interface (and any other sub interface) they can be exchanged  
- I: makes small specific interface with the entity (like separated strategies)  
- D: since the behavior depends on abstract strategy interfaces, not concrete classes

**Extensible**  **/ How to Think About Tasks**

- factory does not need to know what entity it is building  
- additionally the entities are structured in a way to create entities from configuring different properties and strategies and with these creating the more specific entities  
- additionally should allow the developers to query what entities are available, so they know how to build up the entity config types (essentially more ways of thinking of entity like a blueprint)

### Classes

definitely the entity class and then entity factory

**Entity Factory**

- List\<Enum\> getAvailableEntityTypes()  
- createEntity(enum entityType)  
  - probably create the corrected “blueprint” entity using reflection

**Custom Exceptions**

- EntityCreationException  
  - custom exceptions for error handling in createEntity (i.e. if something with the entityType does not match the reflection)

**Entity**

- getPosition()  
- setPosition()  
- get\_Strategy()  
- set\_Strategy()

### Details / Use Cases

AUTH-1: Create Dynamic Entity  
AUTH-2: Configure Dynamic Entity Behavior

- Collaborate with save entity API (i.e. showing possible dynamic entities to create, creating the entity so save API can configure the behavior)

CHARACTER-1: Load PacMan  
GHOST-1: Based on configuration, create ghost  
POWERUP-1: Based on configuration, create powerup

- collaborate with the configuration API as well as the game map API to get what entity to create, then provide the entity with the correct information based on the configuration properties

MAPGENERATION-1: Load Map

- same as above with the generation as the map generation eventually uses loading the individual entities

### Considerations

The main considerations to be addressed before implementing a complete design solution is exactly what parts of entities are shared between both the authoringEnvironment as well as the gamePlayer

