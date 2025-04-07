package oogasalad.authoring.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameSettings;

/**
 * The central model for the Authoring Environment.
 * Stores global game settings, entity templates, and multiple level drafts.
 *
 * Manages adding, updating, and retrieving entity types and level data,
 * and synchronizes changes across levels as needed.
 *
 * @author Will He, Angela Predolac
 */
public class AuthoringModel {
  private String gameTitle;
  private String author;
  private String gameDescription;
  private GameSettings defaultSettings;

  private Map<String, EntityType> entityTypeMap;
  private List<LevelDraft> levels;
  private int currentLevelIndex;

  /**
   * Constructs a new AuthoringModel with default settings and no levels.
   */
  public AuthoringModel() {
    this.entityTypeMap = new LinkedHashMap<>();
    this.levels = new ArrayList<>();
    this.defaultSettings = new GameSettings();
  }

  /**
   * Returns the currently selected level being edited.
   *
   * @return the current LevelDraft
   */
  public LevelDraft getCurrentLevel() {
    return levels.get(currentLevelIndex);
  }

  /**
   * Adds a new level to the project and switches to it.
   *
   * @param level the LevelDraft to add
   */
  public void addLevel(LevelDraft level) {
    levels.add(level);
    currentLevelIndex = levels.size() - 1;
  }

  /**
   * Adds or replaces an entity type in the global entity type map.
   * If a type with the same name already exists, it is overwritten.
   *
   * @param type the EntityType to add
   */
  public void addEntityType(EntityType type) {
    entityTypeMap.put(type.getType(), type);
  }

  /**
   * Updates an existing entity type by name. If the name has changed, removes the old type and
   * replaces it with the new one. Also updates all entity placements in all levels to reference the
   * new type.
   *
   * @param oldTypeName the name of the type to replace
   * @param newType     the new EntityType data
   */
  public void updateEntityType(String oldTypeName, EntityType newType) {
    if (!isValidUpdate(oldTypeName, newType)) {
      return;
    }

    updateEntityTypeMap(oldTypeName, newType);
    updateEntityPlacements(oldTypeName, newType);
  }

  private boolean isValidUpdate(String oldTypeName, EntityType newType) {
    return oldTypeName != null && newType != null && entityTypeMap.containsKey(oldTypeName);
  }

  private void updateEntityTypeMap(String oldTypeName, EntityType newType) {
    if (!oldTypeName.equals(newType.getType())) {
      entityTypeMap.remove(oldTypeName);
    }
    addEntityType(newType);
  }

  private void updateEntityPlacements(String oldTypeName, EntityType newType) {

    for (LevelDraft level : levels) {
      for (EntityPlacement placement : level.getEntityPlacements()) {
        if (placement.getTypeString().equals(oldTypeName)) {
          placement.setType(newType.getType());
          placement.setResolvedEntityType(newType);
        }
      }
    }
  }

  /**
   * Returns an unmodifiable collection of all entity types defined globally.
   *
   * @return the entity types
   */
  public Collection<EntityType> getEntityTypes() {
    return Collections.unmodifiableCollection(entityTypeMap.values());
  }

  /**
   * Finds an entity type by its name.
   *
   * @param typeName the name of the entity type to look up
   * @return an Optional containing the EntityType if found; empty otherwise
   */
  public Optional<EntityType> findEntityType(String typeName) {
    return Optional.ofNullable(entityTypeMap.get(typeName));
  }

  /**
   * Returns the list of all level drafts.
   *
   * @return the list of levels
   */
  public List<LevelDraft> getLevels() {
    return levels;
  }

  /**
   * Clears all entity types and levels from the model.
   * Use with caution — this is a full reset.
   */
  public void clearAll() {
    entityTypeMap.clear();
    levels.clear();
  }

  /**
   * Set current level index
   * @param index Set value
   */
  public void setCurrentLevelIndex(int index) {
    this.currentLevelIndex = index;
  }
}
