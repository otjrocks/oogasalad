package oogasalad.player.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import oogasalad.engine.exceptions.EntityNotFoundException;
import oogasalad.engine.exceptions.InvalidPositionException;
import oogasalad.player.model.strategies.collision.TemporaryModeChangeStrategy;

/**
 * The {@code GameMap} interface provides methods for managing the game's map, which consists of
 * various Entities.
 *
 * @author Owen Jennings
 * @see Entity
 */
public interface GameMapInterface extends Iterable<Entity> {
  // I used ChatGPT to assist in writing the methods of this interface

  /**
   * Adds an entity to the game map at a specific position.
   *
   * @param entity the entity to be added to the map. Note, the entity interface will store the x
   *               and y value where the entity is located on the map.
   * @throws InvalidPositionException if the entity's position is invalid (e.g., occupied by another
   *                                  entity or outside the map).
   */
  void addEntity(Entity entity) throws InvalidPositionException;

  /**
   * Removes an entity from the map. If the specified Entity is not found on the map, throw an
   * EntityNotFoundException
   *
   * @param entity the entity to be removed from the map.
   * @throws EntityNotFoundException if the entity is not found on the map.
   */
  void removeEntity(Entity entity) throws EntityNotFoundException;

  /**
   * Retrieves an entity from the map based on its coordinate location.
   *
   * @param x the X-coordinate of the entity.
   * @param y the Y-coordinate of the entity.
   * @return the entity located at the specified position, returns Optional empty if not found.
   */
  Optional<Entity> getEntityAt(int x, int y);

  /**
   * Returns an iterator for traversing the entities on the map. This iterator intentionally does
   * not reveal the data structure used to implement the GameMap, to allow for various structures,
   * such as Lists, 2D arrays, Maps, etc, based on the specific needs of a concrete implementation
   * of this interface.
   *
   * @return an iterator to iterate over all entities in the GameMap.
   */
  @Override
  Iterator<Entity> iterator();

  /**
   * Get the width of the game map.
   *
   * @return Int representing the game map width.
   */
  int getWidth();

  /**
   * Get the height of the game map.
   *
   * @return Int representing the game map height.
   */
  int getHeight();

  /**
   * added this for now to reflect game loop update, eventually in controller.
   */
  void update();

  /**
   * Gets the frequency of an entity in GameMap.
   *
   * @param entityType entity type to get frequency of
   * @return int count of entityType
   */
  int getEntityCount(String entityType);

  /**
   * Decrement count of entity type.
   *
   * @param entityType entity type that we want to decrement.
   */
  void decrementEntityCount(String entityType);

  /**
   * Checks if the given position (x, y) is valid within the game map.
   *
   * @param x the x-coordinate of the position to check
   * @param y the y-coordinate of the position to check
   * @return true if valid, false otherwise
   */
  boolean isValidPosition(int x, int y);

  /**
   * Checks if the entity at the specified location on the game map does not block the given entity
   * type.
   *
   * @param entityType the type of entity attempting to access the location
   * @param x          the x-coordinate of the location to check
   * @param y          the y-coordinate of the location to check
   * @return true if not blocked, false otherwise
   */
  boolean isNotBlocked(String entityType, int x, int y);

  /**
   * Determine if the game map contains the provided Entity object.
   *
   * @param entity The entity you are querying for.
   * @return true if the entity is part of the game map, false if it was not found on the map.
   */
  boolean contains(Entity entity);

  Map<Entity, TemporaryModeChangeStrategy.ModeChangeInfo> getActiveModeChanges();
}
