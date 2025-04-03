package oogasalad.authoring.model;

import oogasalad.engine.model.EntityPlacement;
import oogasalad.engine.model.EntityType;

import java.util.*;

/**
 * Top-level model that manages all authoring data for the game.
 * Stores entity templates, entity placements, collision rules, and game settings.
 *
 * @author Angela Predolac
 */
public class AuthoringModel {

  private List<EntityType> entityTemplates;
  private List<EntityPlacement> entityPlacements;
  // private CollisionRuleEditorModel collisionRuleModel;
  // private GameSettingsModel gameSettingsModel;

  /**
   * Creates a new AuthoringModel with empty collections and default models.
   */
  public AuthoringModel() {
    entityTemplates = new ArrayList<>();
    entityPlacements = new ArrayList<>();
    // collisionRuleModel = new CollisionRuleEditorModel();
    // gameSettingsModel = new GameSettingsModel();
  }

  /**
   * Return an unmodifiable list of all entity templates.
   *
   * @return An unmodifiable list of all entity templates
   */
  public List<EntityType> getEntityTemplates() {
    return Collections.unmodifiableList(entityTemplates);
  }

  /**
   * Adds a new entity template to the model.
   *
   * @param template The entity template to add
   * @return true if the template was added successfully
   */
  public boolean addEntityTemplate(EntityType template) {
    if (template == null || findEntityTemplateByType(template.getType()).isPresent()) {
      return false;
    }
    return entityTemplates.add(template);
  }

  /**
   * Removes an entity template from the model.
   *
   * @param template The entity template to remove
   * @return true if the template was removed successfully
   */
  public boolean removeEntityTemplate(EntityType template) {
    return entityTemplates.remove(template);
  }

  /**
   * Updates an existing entity template.
   *
   * @param oldTemplate The template to replace
   * @param newTemplate The new template data
   * @return true if the template was updated successfully
   */
  public boolean updateEntityTemplate(EntityType oldTemplate, EntityType newTemplate) {
    int index = entityTemplates.indexOf(oldTemplate);
    if (index >= 0) {
      entityTemplates.set(index, newTemplate);

      // Update any placements using this template
      for (EntityPlacement placement : entityPlacements) {
        if (placement.getType() == oldTemplate) {
          placement.setResolvedEntityType(newTemplate);
        }
      }
      return true;
    }
    return false;
  }

  /**
   * Finds an entity template by its type.
   *
   * @param type The type of the entity template to find
   * @return An Optional containing the found template, or empty if not found
   */
  public Optional<EntityType> findEntityTemplateByType(String type) {
    return entityTemplates.stream()
        .filter(template -> template.getType().equals(type))
        .findFirst();
  }

  /**
   * Returns an unmodifiable list of all entity placements.
   *
   * @return An unmodifiable list of all entity placements
   */
  public List<EntityPlacement> getEntityPlacements() {
    return Collections.unmodifiableList(entityPlacements);
  }

  /**
   * Adds a new entity placement to the model.
   *
   * @param placement The entity placement to add
   * @return true if the placement was added successfully
   */
  public boolean addEntityPlacement(EntityPlacement placement) {
    if (placement == null) {
      return false;
    }
    return entityPlacements.add(placement);
  }

  /**
   * Creates and adds a new entity placement using an existing template.
   *
   * @param template The entity template to use
   * @param x        The x coordinate for placement
   * @param y        The y coordinate for placement
   * @return The newly created EntityPlacement, or null if template was null
   */
  public EntityPlacement createAndAddEntityPlacement(EntityType template, double x, double y) {
    if (template == null) {
      return null;
    }
    EntityPlacement placement = new EntityPlacement(template, x, y, "Default");
    entityPlacements.add(placement);
    return placement;
  }

  /**
   * Removes an entity placement from the model.
   *
   * @param placement The entity placement to remove
   * @return true if the placement was removed successfully
   */
  public boolean removeEntityPlacement(EntityPlacement placement) {
    return entityPlacements.remove(placement);
  }

  /**
   * Finds an entity placement at or near the specified coordinates.
   *
   * @param x         The x coordinate to search at
   * @param y         The y coordinate to search at
   * @param threshold The maximum distance to consider for a match
   * @return An Optional containing the found placement, or empty if not found
   */
  public Optional<EntityPlacement> findEntityPlacementAt(double x, double y, double threshold) {
    return entityPlacements.stream()
        .filter(placement -> {
          double dx = placement.getX() - x;
          double dy = placement.getY() - y;
          return Math.sqrt(dx * dx + dy * dy) <= threshold;
        })
        .findFirst();
  }

  /*
  public CollisionRuleEditorModel getCollisionRuleModel() {
      return collisionRuleModel;
  }

  public GameSettingsModel getGameSettingsModel() {
      return gameSettingsModel;
  }
  */

  /**
   * Clears all entity placements from the model.
   */
  public void clearEntityPlacements() {
    entityPlacements.clear();
  }

  /**
   * Clears all data in the model, including templates, placements, and settings.
   */
  public void clearAll() {
    entityTemplates.clear();
    entityPlacements.clear();
    // collisionRuleModel = new CollisionRuleEditorModel();
    // gameSettingsModel = new GameSettingsModel();
  }
}
