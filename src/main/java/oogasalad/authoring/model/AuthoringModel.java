package oogasalad.authoring.model;

import oogasalad.engine.model.EntityData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Top-level model that manages all authoring data for the game.
 * Stores entity templates, entity placements, collision rules, and game settings.
 */
public class AuthoringModel {

    private List<EntityData> entityTemplates;
    private List<EntityPlacement> entityPlacements;
    //private CollisionRuleEditorModel collisionRuleModel;
    //private GameSettingsModel gameSettingsModel;

    /**
     * Creates a new AuthoringModel with empty collections and default models.
     */
    public AuthoringModel() {
        entityTemplates = new ArrayList<>();
        entityPlacements = new ArrayList<>();
        //collisionRuleModel = new CollisionRuleEditorModel();
        //gameSettingsModel = new GameSettingsModel();
    }

    /**
     * @return An unmodifiable list of all entity templates
     */
    public List<EntityData> getEntityTemplates() {
        return Collections.unmodifiableList(entityTemplates);
    }

    /**
     * Adds a new entity template to the model.
     *
     * @param template The entity template to add
     * @return true if the template was added successfully
     */
    public boolean addEntityTemplate(EntityData template) {
        if (template == null) {
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
    public boolean removeEntityTemplate(EntityData template) {
        return entityTemplates.remove(template);
    }

    /**
     * Updates an existing entity template.
     *
     * @param oldTemplate The template to replace
     * @param newTemplate The new template data
     * @return true if the template was updated successfully
     */
    public boolean updateEntityTemplate(EntityData oldTemplate, EntityData newTemplate) {
        int index = entityTemplates.indexOf(oldTemplate);
        if (index >= 0) {
            entityTemplates.set(index, newTemplate);

            // Update any placements using this template
            for (EntityPlacement placement : entityPlacements) {
                if (placement.getEntityData() == oldTemplate) {
                    placement.setEntityData(newTemplate);
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
    public Optional<EntityData> findEntityTemplateByType(String type) {
        return entityTemplates.stream()
            .filter(template -> template.getType().equals(type))
            .findFirst();
    }

    /**
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
    public EntityPlacement createAndAddEntityPlacement(EntityData template, double x, double y) {
        if (template == null) {
            return null;
        }

        EntityPlacement placement = new EntityPlacement(template, x, y);
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
                double distance = Math.sqrt(
                    Math.pow(placement.getX() - x, 2) +
                        Math.pow(placement.getY() - y, 2));
                return distance <= threshold;
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
        //collisionRuleModel = new CollisionRuleEditorModel();
        //gameSettingsModel = new GameSettingsModel();\
    }
}
