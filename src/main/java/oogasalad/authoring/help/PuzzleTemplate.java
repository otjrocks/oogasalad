package oogasalad.authoring.help;

import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.config.CollisionRule;
import oogasalad.engine.records.model.EntityTypeRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Puzzle game template with grid-based mechanics.
 */
public class PuzzleTemplate extends GameTemplate {

    public PuzzleTemplate() {
        super("Puzzle Game", "A grid-based puzzle game");
    }

    @Override
    public void applyTo(AuthoringModel model) {
        // Clear old data
        model.clearAll();

        // Configure game settings
        model.setGameTitle("Grid Puzzle");
        model.setAuthor("Game Author");
        model.setGameDescription("Solve puzzles by moving blocks to the right positions!");

        // Create entity types
        EntityTypeRecord playerEntity = createEntityType(
                "Player",
                "data/games/Puzzle/core/assets/player.png",
                100.0,
                true
        );

        EntityTypeRecord blockEntity = createEntityType(
                "Block",
                "data/games/Puzzle/core/assets/block.png",
                0.0,
                false
        );

        EntityTypeRecord targetEntity = createEntityType(
                "Target",
                "data/games/Puzzle/core/assets/target.png",
                0.0,
                false
        );

        EntityTypeRecord wallEntity = createEntityType(
                "Wall",
                "data/games/Puzzle/core/assets/wall.png",
                0.0,
                false
        );

        // Add entity types to model
        model.addEntityType(playerEntity);
        model.addEntityType(blockEntity);
        model.addEntityType(targetEntity);
        model.addEntityType(wallEntity);

        // Create level
        LevelDraft level = createDefaultLevel("Level 1", 10, 10);

        // Place entities
        // Player
        placeEntity(level, playerEntity, 150, 150);

        // Blocks
        placeEntity(level, blockEntity, 200, 200);
        placeEntity(level, blockEntity, 300, 200);

        // Targets
        placeEntity(level, targetEntity, 250, 350);
        placeEntity(level, targetEntity, 350, 350);

        // Walls (creating a simple room)
        for (int i = 0; i < 10; i++) {
            placeEntity(level, wallEntity, i * 50, 0);  // Top wall
            placeEntity(level, wallEntity, i * 50, 450);  // Bottom wall
            placeEntity(level, wallEntity, 0, i * 50);  // Left wall
            placeEntity(level, wallEntity, 450, i * 50);  // Right wall
        }

        // Add level to model
        model.addLevel(level);

        // Create collision rules
        List<CollisionRule> rules = new ArrayList<>();

        // Block on target (no events, just for game logic)
        //rules.add(createCollisionRule("Block", "Target", false, false, 0));

        // Add collision rules to model
        model.setCollisionRules(rules);
    }
}
