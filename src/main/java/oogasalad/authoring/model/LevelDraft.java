package oogasalad.authoring.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.model.SpawnEventRecord;
import oogasalad.engine.records.model.EntityTypeRecord;
import oogasalad.engine.records.model.ModeChangeEventRecord;

/**
 * Represents a draft of a single level in the authoring environment. Stores metadata such as name,
 * file name, grid dimensions, edge policy, and all entity placements for the level.
 *
 * @author Will He
 */
public class LevelDraft {

  private String name;
  private String outputFileName;
  private int width;
  private int height;
  private String edgePolicy;

  private final List<EntityPlacement> entityPlacements;
  private List<ModeChangeEventRecord> modeChangeEvents;
  private List<SpawnEventRecord> spawnEvents;

  /**
   * Constructs a new LevelDraft with the given name and output file name.
   *
   * @param name           the display name of the level
   * @param outputFileName the file name this level will be saved to
   */
  public LevelDraft(String name, String outputFileName) {
    this.name = name;
    this.outputFileName = outputFileName;
    this.entityPlacements = new ArrayList<>();
    this.modeChangeEvents = new ArrayList<>();
    this.spawnEvents = new ArrayList<>();

    // Default init values
    this.width = 20;
    this.height = 15;
  }

  /**
   * Adds an existing entity placement to the level.
   *
   * @param placement the entity placement to add
   * @return true if the placement was added successfully; false if null
   */
  public boolean addEntityPlacement(EntityPlacement placement) {
    if (placement == null) {
      return false;
    }
    entityPlacements.add(placement);
    return true;
  }

  /**
   * Removes the specified entity placement from the level.
   *
   * @param placement the placement to remove
   * @return true if the placement existed and was removed; false otherwise
   */
  public boolean removeEntityPlacement(EntityPlacement placement) {
    return entityPlacements.remove(placement);
  }

  /**
   * Creates a new entity placement using the specified entity type and coordinates, and adds it to
   * the level using the default mode.
   *
   * @param type the entity type to place
   * @param x    the X-coordinate (in pixels)
   * @param y    the Y-coordinate (in pixels)
   * @return the created EntityPlacement, or null if the type is null
   */
  public EntityPlacement createAndAddEntityPlacement(EntityTypeRecord type, double x, double y) {
    if (type == null) {
      return null;
    }
    EntityPlacement placement = new EntityPlacement(type, x, y, "Default");
    entityPlacements.add(placement);
    return placement;
  }

  /**
   * Finds the first entity placement within a threshold distance of the given coordinates.
   *
   * @param x         the X-coordinate to check
   * @param y         the Y-coordinate to check
   * @param threshold the maximum distance from the point to consider
   * @return an Optional containing the matching placement if found; empty otherwise
   */
  public Optional<EntityPlacement> findEntityPlacementAt(double x, double y, double threshold) {
    return entityPlacements.stream()
        .filter(p -> {
          double dx = p.getX() - x;
          double dy = p.getY() - y;
          return Math.sqrt(dx * dx + dy * dy) <= threshold;
        })
        .findFirst();
  }

  /**
   * Removes all entity placements from the level.
   */
  public void clearPlacements() {
    entityPlacements.clear();
  }

  /**
   * Returns an unmodifiable list of all entity placements in the level.
   *
   * @return an unmodifiable list of placements
   */
  public List<EntityPlacement> getEntityPlacements() {
    return Collections.unmodifiableList(entityPlacements);
  }

  /**
   * Returns the display name of the level.
   *
   * @return the level name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the display name of the level.
   *
   * @param name the new name for the level
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the file name this level will be saved to.
   *
   * @return the output file name
   */
  public String getOutputFileName() {
    return outputFileName;
  }

  /**
   * Sets the file name this level should be saved to.
   *
   * @param outputFileName the new output file name
   */
  public void setOutputFileName(String outputFileName) {
    this.outputFileName = outputFileName;
  }

  /**
   * Returns the width of the level grid (in tiles).
   *
   * @return the width of the level
   */
  public int getWidth() {
    return width;
  }

  /**
   * Sets the width of the level grid (in tiles).
   *
   * @param width the new width of the level
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Returns the height of the level grid (in tiles).
   *
   * @return the height of the level
   */
  public int getHeight() {
    return height;
  }

  /**
   * Sets the height of the level grid (in tiles).
   *
   * @param height the new height of the level
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Updates the mode name for all {@link EntityPlacement}s that belong to the specified entity type
   * and currently use the old mode name. This is used when renaming a mode in the
   * {@link EntityTypeRecord} so that all existing placements referencing the old mode are updated
   * to the new mode name.
   *
   * @param entityTypeName the name of the entity type to update
   * @param oldMode        the previous name of the mode
   * @param newMode        the new name to replace the old mode
   */
  public void updateModeName(String entityTypeName, String oldMode, String newMode) {
    for (EntityPlacement placement : entityPlacements) {
      if (placement.getTypeString().equals(entityTypeName) &&
          placement.getMode().equals(oldMode)) {
        placement.setMode(newMode);
      }
    }
  }

  /**
   * Re-resolves all {@link EntityPlacement}s in the level using the given map of updated
   * {@link EntityTypeRecord}s. This should be called after replacing or modifying an entity type in
   * the global entity type map to ensure that all placements use the latest version.
   *
   * @param typeMap a map of entity type names to updated {@link EntityTypeRecord} instances
   */
  public void refreshEntityTypes(Map<String, EntityTypeRecord> typeMap) {
    for (EntityPlacement p : entityPlacements) {
      if (typeMap.containsKey(p.getTypeString())) {
        p.setResolvedEntityType(typeMap.get(p.getTypeString()));
      }
    }
  }


  /**
   * Return all modeChangeEvents
   *
   * @return ModeChangeEvents
   */
  public List<ModeChangeEventRecord> getModeChangeEvents() {
    return modeChangeEvents;
  }

  /**
   * Return all spawnEvents
   *
   * @return SpawnEvents
   */
  public List<SpawnEventRecord> getSpawnEvents() {
    return spawnEvents;
  }
}
