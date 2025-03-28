package oogasalad;

import java.io.IOException;
import java.util.Optional;

/**
 * Manages the saving and loading of custom game entities.
 * Provides a flexible interface for persistent storage of game entities.
 */
public interface EntitySaveManager<T extends GameEntity> {
    /**
     * Saves a game entity to a persistent storage medium.
     *
     * @param entity The game entity to be saved
     * @return A unique identifier for the saved entity
     * @throws EntitySaveException If the entity cannot be saved
     */
    String saveEntity(T entity) throws EntitySaveException;

    /**
     * Retrieves a previously saved entity by its unique identifier.
     *
     * @param entityId The unique identifier of the entity
     * @return An optional containing the retrieved entity, or empty if not found
     * @throws EntityLoadException If there's an error loading the entity
     */
    Optional<T> loadEntity(String entityId) throws EntityLoadException;

    /**
     * Updates an existing entity in the storage medium.
     *
     * @param entityId The unique identifier of the entity to update
     * @param updatedEntity The updated entity data
     * @return True if the update was successful, false otherwise
     * @throws EntityUpdateException If the entity cannot be updated
     */
    boolean updateEntity(String entityId, T updatedEntity) throws EntityUpdateException;

    /**
     * Deletes an entity from the storage medium.
     *
     * @param entityId The unique identifier of the entity to delete
     * @return True if the deletion was successful, false otherwise
     * @throws EntityDeletionException If the entity cannot be deleted
     */
    boolean deleteEntity(String entityId) throws EntityDeletionException;
}