package oogasalad.authoring.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;

/**
 * Represents a draft of a single level in the authoring environment.
 * Stores metadata such as name, file name, grid dimensions, edge policy,
 * and all entity placements for the level.
 *
 * @author Will He
 */
public class LevelDraft {
  private String name;
  private String outputFileName;
  private int width;
  private int height;
  private int edgePolicy;

  private List<EntityPlacement> entityPlacements;

  /**
   * Constructs a new LevelDraft with the given name and output file name.
   *
   * @param name the display name of the level
   * @param outputFileName the file name this level will be saved to
   */
  public LevelDraft(String name, String outputFileName) {
    this.name = name;
    this.outputFileName = outputFileName;
    this.entityPlacements = new ArrayList<>();
  }

  /**
   * Adds an existing entity placement to the level.
   *
   * @param placement the entity placement to add
   * @return true if the placement was added successfully; false if null
   */
  public boolean addEntityPlacement(EntityPlacement placement) {
    return placement != null && entityPlacements.add(placement);
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
   * Creates a new entity placement using the specified entity type and coordinates,
   * and adds it to the level using the default mode.
   *
   * @param type the entity type to place
   * @param x the X-coordinate (in pixels)
   * @param y the Y-coordinate (in pixels)
   * @return the created EntityPlacement, or null if the type is null
   */
  public EntityPlacement createAndAddEntityPlacement(EntityType type, double x, double y) {
    if (type == null) return null;
    EntityPlacement placement = new EntityPlacement(type, x, y, "Default");
    entityPlacements.add(placement);
    return placement;
  }

  /**
   * Finds the first entity placement within a threshold distance of the given coordinates.
   *
   * @param x the X-coordinate to check
   * @param y the Y-coordinate to check
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
   * Returns the edge policy used for the level (e.g., regular, toroidal).
   *
   * @return the edge policy as an integer
   */
  public int getEdgePolicy() {
    return edgePolicy;
  }

  /**
   * Sets the edge policy used for the level.
   *
   * @param edgePolicy the new edge policy
   */
  public void setEdgePolicy(int edgePolicy) {
    this.edgePolicy = edgePolicy;
  }
}
