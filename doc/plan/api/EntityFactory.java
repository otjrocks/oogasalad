package plan.api;

/**
 * The {@code GameMap} interface provides methods for managing creating the game Entities.
 *
 * @author Jessica Chen
 */
public interface EntityFactory {

    Entity createEntity(EntityData entityData);

    /**
     * Exception thrown to indicate an error occurred during the creation of an entity.
     * This exception can be used to signal issues such as invalid parameters
     * or other conditions that prevent successful entity creation.
     */
    class EntityCreationException extends Exception {

    }
}
