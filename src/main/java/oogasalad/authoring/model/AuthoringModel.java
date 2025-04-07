package oogasalad.authoring.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.GameSettings;

public class AuthoringModel {
  private String gameTitle;
  private String author;
  private String gameDescription;
  private GameSettings defaultSettings;


  private Map<String, EntityType> entityTypeMap;
  private List<LevelDraft> levels;
  private int currentLevelIndex;

  public AuthoringModel() {
    this.entityTypeMap = new LinkedHashMap<>();
    this.levels = new ArrayList<>();
    this.defaultSettings = new GameSettings();
  }

  public LevelDraft getCurrentLevel() {
    return levels.get(currentLevelIndex);
  }

  public void addLevel(LevelDraft level) {
    levels.add(level);
    currentLevelIndex = levels.size() - 1;
  }

  public void addEntityType(EntityType type) {
    entityTypeMap.put(type.getType(), type);
  }

  public boolean updateEntityType(String oldTypeName, EntityType newType) {
    EntityType oldType = entityTypeMap.get(oldTypeName);
    if (oldType == null || newType == null) {
      return false;
    }

    if (!oldTypeName.equals(newType.getType())) {
      entityTypeMap.remove(oldTypeName);
    }

    entityTypeMap.put(newType.getType(), newType);

    for (LevelDraft level : levels) {
      for (EntityPlacement placement : level.getEntityPlacements()) {
        if (placement.getType() == oldType) {
          placement.setType(newType.getType());
          placement.setResolvedEntityType(newType);
        }
      }
    }
    return true;
  }


  public Collection<EntityType> getEntityTypes() {
    return Collections.unmodifiableCollection(entityTypeMap.values());
  }

  public Optional<EntityType> findEntityType(String typeName) {
    return Optional.ofNullable(entityTypeMap.get(typeName));
  }

  public List<LevelDraft> getLevels() {
    return levels;
  }

  public void clearAll() {
    entityTypeMap.clear();
    levels.clear();
  }
}
