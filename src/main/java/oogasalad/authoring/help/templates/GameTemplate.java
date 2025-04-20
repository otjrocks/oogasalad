package oogasalad.authoring.help.templates;

import java.io.File;
import java.util.List;
import java.util.Map;

import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.ModeConfigRecord;
import oogasalad.engine.records.config.ImageConfigRecord;
import oogasalad.engine.records.config.model.EntityPropertiesRecord;
import oogasalad.engine.records.config.model.controlConfig.KeyboardControlConfigRecord;
import oogasalad.engine.records.config.model.controlConfig.NoneControlConfigRecord;
import oogasalad.engine.records.model.EntityTypeRecord;

/**
 * Abstract base class for game templates.
 * Templates provide pre-configured game setups that can be applied to the authoring model.
 *
 * @author Angela Predolac
 */
public abstract class GameTemplate {

    private final String name;
    private final String description;

    /**
     * Constructs a game template with a name and description.
     *
     * @param name the template name
     * @param description the template description
     */
    protected GameTemplate(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the template name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the template description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Applies this template to the given model, configuring entities, levels, and settings.
     *
     * @param model the authoring model to configure
     */
    public abstract void applyTo(AuthoringModel model);

    /**
     * Creates a default level with the given dimensions.
     *
     * @param name the level name
     * @param width the level width (in tiles)
     * @param height the level height (in tiles)
     * @return the created level draft
     */
    protected LevelDraft createDefaultLevel(String name, int width, int height) {
        LevelDraft level = new LevelDraft(name, "level_map.json");
        level.setWidth(width);
        level.setHeight(height);
        return level;
    }

    /**
     * Creates an entity type with default configuration.
     *
     * @param typeName the entity type name
     * @param imagePath the path to the sprite image
     * @param speed the movement speed
     * @param isPlayerControlled whether this entity is controlled by the player
     * @return the created entity type record
     */
    protected EntityTypeRecord createEntityType(String typeName, String imagePath, double speed, boolean isPlayerControlled) {
        // Create mode configuration
        File imageFile = new File(imagePath);
        String imageUri = imageFile.exists() ? imageFile.toURI().toString() : "file:data/games/BasicPacMan/core/assets/pacman.png";

        ModeConfigRecord defaultMode = getModeConfigRecord(speed, isPlayerControlled, imageUri);

        // Create the entity type with the default mode
        return new EntityTypeRecord(
                typeName,
                isPlayerControlled ? new KeyboardControlConfigRecord() : new NoneControlConfigRecord(),
                Map.of("Default", defaultMode),
                null,  // no additional properties
                speed
        );
    }

    private static ModeConfigRecord getModeConfigRecord(double speed, boolean isPlayerControlled, String imageUri) {
        ImageConfigRecord imageConfig = new ImageConfigRecord(
                imageUri,
                28,  // tile width
                28,  // tile height
                4,   // animation frames
                1.0  // animation speed
        );

        EntityPropertiesRecord entityProps = new EntityPropertiesRecord(
                "Default",
                isPlayerControlled ? new KeyboardControlConfigRecord() : new NoneControlConfigRecord(),
                speed,
                List.of()  // no blocks
        );

        return new ModeConfigRecord("Default", entityProps, imageConfig);
    }

    /**
     * Places an entity on the level at the specified coordinates.
     *
     * @param level the level to place the entity in
     * @param entityType the entity type to place
     * @param x the x-coordinate (in pixels)
     * @param y the y-coordinate (in pixels)
     * @return the created entity placement
     */
    protected EntityPlacement placeEntity(LevelDraft level, EntityTypeRecord entityType, double x, double y) {
        return level.createAndAddEntityPlacement(entityType, x, y);
    }

    /*
    protected CollisionRule createCollisionRule(String entityA, String entityB, boolean destroyA, boolean destroyB, int pointsAwarded) {
        CollisionRule rule = new CollisionRule();
        rule.setEntityA(entityA);
        rule.setEntityB(entityB);
        rule.setModeA("Default");
        rule.setModeB("Default");

        List<oogasalad.engine.records.config.model.CollisionEventInterface> eventsA = new ArrayList<>();
        List<oogasalad.engine.records.config.model.CollisionEventInterface> eventsB = new ArrayList<>();

        if (destroyA) {
            // Add destroy event for A
            oogasalad.engine.records.config.model.DestroyOnCollisionRecord destroyEvent =
                    new oogasalad.engine.records.config.model.DestroyOnCollisionRecord();
            eventsA.add(destroyEvent);
        }

        if (destroyB) {
            // Add destroy event for B
            oogasalad.engine.records.config.model.DestroyOnCollisionRecord destroyEvent =
                    new oogasalad.engine.records.config.model.DestroyOnCollisionRecord();
            eventsB.add(destroyEvent);
        }

        if (pointsAwarded > 0) {
            // Add points event for A
            oogasalad.engine.records.config.model.PointsOnCollisionRecord pointsEvent =
                    new oogasalad.engine.records.config.model.PointsOnCollisionRecord(pointsAwarded);
            eventsA.add(pointsEvent);
        }

        rule.setEventsA(eventsA);
        rule.setEventsB(eventsB);

        return rule;
    }
    */
}