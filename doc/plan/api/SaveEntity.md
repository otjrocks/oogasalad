## SaveEntity

### Overview

The SaveEntity API is designed to provide a robust and flexible approach for saving and loading custom entities in the game’s authoring environment. Primary design goals include: 

1. Extensibility: supporting easy addition of new entity types and properties.  
2. Separation of concerns: decoupling entity saving logic from creation and rendering.  
3. Flexibility: supporting various storage mechanisms.  
4. Error handling: providing comprehensive error handling mechanisms for saving entities.

**SOLID / Object Oriented Design**

- Single Responsibility Principle: Each class is designed to have a focused and well-defined purpose.  
- Open-Closed Principle: Designed to be extensible through abstract classes and interfaces.  
- Dependency Inversion: Depends on abstractions rather than concrete classes.

### Classes

**public interface EntitySaveManager\<Type extends GameEntity\>**

- String saveEntity(T entity) throws EntitySaveException  
- Optional\<T\> loadEntity(String entityID) throws EntityLoadException  
- Boolean updateEntity(String entityID, T updatedEntity) throws EntityUpdateException  
- Boolean deleteEntity(String entityID) throws EntityDeletionException

**public interface EntityStorageProvider**

- Void write(String key, String data) throws StorageException  
- String read(String key) throws StorageException  
- Boolean delete(String key) throws StorageException

**Custom Exceptions**

- Public class EntitySaveException extends Exception  
- Public class EntityLoadException extends Exception  
- Public class EntityUpdateException extends Exception  
- Public class EntityDeletionException extends Exception  
- Public class StorageException extends Exception

### Details / Use Cases

This API will primarily interact with the Entity classes to support the saving and updating of game entities in the authoring environment. 

Here are some specific Use Cases that will need to interact with/implement this API:

* Use Case: AUTH-1: Provide a storage mechanism for newly created entities   
* Use Case: AUTH-2: Support updating existing entities  
* Use Case: AUTH-3: Support saving and retrieving layer-specific entity properties  
* Use Case: AUTH-4: Support saving and retrieving complex entity states  
* Use Case: AUTH-5: Support batch saving and updating of multiple entities

### Considerations

The API for SaveEntity is designed to be flexible and extendable to many different entities. Possible considerations and future improvements include better error recovery and logging, calling mechanisms for frequently accessed entities, and support for versioning entity templates.

