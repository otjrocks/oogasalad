package oogasalad.engine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.model.entity.Entity;
import oogasalad.engine.model.exceptions.EntityNotFoundException;
import oogasalad.engine.model.exceptions.InvalidPositionException;

/**
 * A simple implementation of a {@code GameMap}.
 *
 * @author Owen Jennings
 */
public class GameMapImpl implements GameMap {

  private final List<Entity> myEntityList;
  private final int myWidth;
  private final int myHeight;
  private final Map<String, Integer> entityFrequencyMap;

  /**
   * The constructor for a game map.
   *
   * @param width  The width in cells of this game map.
   * @param height The height in cells of this game map.
   */
  public GameMapImpl(int width, int height) {
    myEntityList = new ArrayList<>();
    myWidth = width;
    myHeight = height;
    entityFrequencyMap = new HashMap<>();
  }

  @Override
  public void addEntity(Entity entity) throws InvalidPositionException {
    EntityPlacement data = entity.getEntityPlacement();
    checkEntryInBounds(entity, data);
    myEntityList.add(entity);
  }

  private void checkEntryInBounds(Entity entity, EntityPlacement data)
      throws InvalidPositionException {
    if (data.getX() < 0 || data.getX() >= myWidth || data.getY() < 0
        || data.getY() >= myHeight) {
      LoggingManager.LOGGER.warn("Invalid entry position: {}, {}", data.getX(), data.getY());
      LoggingManager.LOGGER.warn("Cannot add entity {} because it does not fit in the game map!",
          entity);
      throw new InvalidPositionException("The entity you provided does not fit in this game map!");
    }
  }

  @Override
  public void removeEntity(Entity entity) throws EntityNotFoundException {
    if (!myEntityList.contains(entity)) {
      LoggingManager.LOGGER.warn("Cannot remove entity {} because it does not exist!", entity);
      throw new EntityNotFoundException(
          "Cannot remove requested entity, because it does not exist in the game map!");
    } else {
      myEntityList.remove(entity);

      decrementEntityCount(entity.getEntityPlacement().getType().type().toLowerCase());
    }
  }


  @Override
  public Optional<Entity> getEntityAt(int x, int y) {
    for (Entity entity : myEntityList) {
      if (entity.getEntityPlacement().getX() == x && entity.getEntityPlacement().getY() == y) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }


  @Override
  public Iterator<Entity> iterator() {
    return myEntityList.iterator();
  }

  @Override
  public int getWidth() {
    return myWidth;
  }

  @Override
  public int getHeight() {
    return myHeight;
  }

  @Override
  public void update() {
    for (Entity entity : myEntityList) {
      entity.update();
      wrapEntity(entity);
    }
  }

  private void wrapEntity(Entity entity) {
    double newX = (entity.getEntityPlacement().getX() + myWidth) % myWidth;
    double newY = (entity.getEntityPlacement().getY() + myHeight) % myHeight;
    entity.getEntityPlacement().setX(newX);
    entity.getEntityPlacement().setY(newY);
  }

  @Override
  public int getEntityCount(String entityType) {
    if (entityFrequencyMap.containsKey(entityType)) {
      return entityFrequencyMap.get(entityType);
    }
    int count = 0;
    for (Entity entity1 : myEntityList) {
      if (entity1.getEntityPlacement().getType().type().toLowerCase().equals(entityType)) {
        count++;
      }
    }
    entityFrequencyMap.put(entityType, count);
    return count;
  }

  @Override
  public void decrementEntityCount(String entityType) {
    if (entityFrequencyMap.containsKey(entityType)) {
      entityFrequencyMap.put(entityType, entityFrequencyMap.get(entityType) - 1);
    }
  }

  @Override
  public boolean isValidPosition(int x, int y) {
    return x >= 0 && y >= 0 && x < myWidth && y < myHeight;
  }

  @Override
  public boolean isNotBlocked(String entityType, int x, int y) {
    Optional<Entity> entity = getEntityAt(x, y);

    return entity.map(value -> value.getEntityPlacement().getType().blocks() == null ||
        value.getEntityPlacement().getType().blocks().stream()
            .noneMatch(block -> block.equalsIgnoreCase(entityType))).orElse(true);

  }
}
