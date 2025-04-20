package oogasalad.authoring.help.templates;

import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.config.CollisionRule;
import oogasalad.engine.records.model.EntityTypeRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Top-down shooter template with projectiles and enemies.
 * @author Angela Predolac
 */
public class ShooterTemplate extends GameTemplate {

    public ShooterTemplate() {
        super("Top-down Shooter", "An action game with projectiles");
    }

    @Override
    public void applyTo(AuthoringModel model) {
        // Clear old data
        model.clearAll();

        // Configure game settings
        model.setGameTitle("Space Shooter");
        model.setAuthor("Game Author");
        model.setGameDescription("Survive waves of enemies in this top-down shooter!");

        // Create entity types
        EntityTypeRecord playerEntity = createEntityType(
                "Player",
                "data/games/Shooter/core/assets/player.png",
                150.0,
                true
        );

        EntityTypeRecord bulletEntity = createEntityType(
                "Bullet",
                "data/games/Shooter/core/assets/bullet.png",
                300.0,
                false
        );

        EntityTypeRecord enemyEntity = createEntityType(
                "Enemy",
                "data/games/Shooter/core/assets/enemy.png",
                80.0,
                false
        );

        EntityTypeRecord powerupEntity = createEntityType(
                "Powerup",
                "data/games/Shooter/core/assets/powerup.png",
                0.0,
                false
        );

        // Add entity types to model
        model.addEntityType(playerEntity);
        model.addEntityType(bulletEntity);
        model.addEntityType(enemyEntity);
        model.addEntityType(powerupEntity);

        // Create level
        LevelDraft level = createDefaultLevel("Level 1", 25, 25);

        // Place entities
        // Player
        placeEntity(level, playerEntity, 300, 400);

        // Enemies (example placements)
        placeEntity(level, enemyEntity, 100, 100);
        placeEntity(level, enemyEntity, 200, 150);
        placeEntity(level, enemyEntity, 400, 100);
        placeEntity(level, enemyEntity, 500, 200);

        // Powerup
        placeEntity(level, powerupEntity, 300, 200);

        // Add level to model
        model.addLevel(level);

        // Create collision rules
        List<CollisionRule> rules = new ArrayList<>();

        // Bullet hits enemy
        //rules.add(createCollisionRule("Bullet", "Enemy", true, true, 10));

        // Player hits enemy
        //rules.add(createCollisionRule("Player", "Enemy", true, true, 0));

        // Player collects powerup
        //rules.add(createCollisionRule("Player", "Powerup", false, true, 5));

        // Add collision rules to model
        model.setCollisionRules(rules);
    }
}
