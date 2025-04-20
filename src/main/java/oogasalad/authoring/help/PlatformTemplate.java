package oogasalad.authoring.help;

import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.config.CollisionRule;
import oogasalad.engine.records.model.EntityTypeRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Platform game template with jumping, obstacles, and collectibles.
 * @author Angela Predolac
 */
public class PlatformTemplate extends GameTemplate {

    public PlatformTemplate() {
        super("Platform Game", "A side-scrolling platform game");
    }

    @Override
    public void applyTo(AuthoringModel model) {
        // Clear old data
        model.clearAll();

        // Configure game settings
        model.setGameTitle("Platform Adventure");
        model.setAuthor("Game Author");
        model.setGameDescription("Jump and run through the levels, collecting coins and avoiding enemies!");

        // Create entity types
        EntityTypeRecord playerEntity = createEntityType(
                "Player",
                "data/games/Platform/core/assets/player.png",
                120.0,
                true
        );

        EntityTypeRecord platformEntity = createEntityType(
                "Platform",
                "data/games/Platform/core/assets/platform.png",
                0.0,
                false
        );

        EntityTypeRecord coinEntity = createEntityType(
                "Coin",
                "data/games/Platform/core/assets/coin.png",
                0.0,
                false
        );

        EntityTypeRecord enemyEntity = createEntityType(
                "Enemy",
                "data/games/Platform/core/assets/enemy.png",
                50.0,
                false
        );

        // Add entity types to model
        model.addEntityType(playerEntity);
        model.addEntityType(platformEntity);
        model.addEntityType(coinEntity);
        model.addEntityType(enemyEntity);

        // Create level
        LevelDraft level = createDefaultLevel("Level 1", 30, 20);

        // Place entities (example placements)
        // Player
        placeEntity(level, playerEntity, 100, 300);

        // Platforms
        placeEntity(level, platformEntity, 50, 350);
        placeEntity(level, platformEntity, 100, 350);
        placeEntity(level, platformEntity, 150, 350);
        placeEntity(level, platformEntity, 200, 350);
        placeEntity(level, platformEntity, 300, 300);
        placeEntity(level, platformEntity, 400, 250);

        // Coins
        placeEntity(level, coinEntity, 150, 300);
        placeEntity(level, coinEntity, 300, 250);
        placeEntity(level, coinEntity, 400, 200);

        // Enemies
        placeEntity(level, enemyEntity, 350, 275);

        // Add level to model
        model.addLevel(level);

        // Create collision rules
        List<CollisionRule> rules = new ArrayList<>();

        // Player collects coin
        //rules.add(createCollisionRule("Player", "Coin", false, true, 10));

        // Player hits enemy
        //rules.add(createCollisionRule("Player", "Enemy", true, false, 0));

        // Add collision rules to model
        model.setCollisionRules(rules);
    }
}
