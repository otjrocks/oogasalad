package oogasalad.authoring.help;

import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.config.CollisionRule;
import oogasalad.engine.records.model.EntityTypeRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Pacman-style template with mazes, collectibles, and enemies.
 * @author Angela Predolac
 */
public class PacmanTemplate extends GameTemplate {

    public PacmanTemplate() {
        super("Pacman-style Game", "A maze game with collectibles and enemies");
    }

    @Override
    public void applyTo(AuthoringModel model) {
        // Clear old data
        model.clearAll();

        // Configure game settings
        model.setGameTitle("Maze Runner");
        model.setAuthor("Game Author");
        model.setGameDescription("Collect all the dots while avoiding the ghosts!");

        // Create entity types
        EntityTypeRecord playerEntity = createEntityType(
                "Player",
                "data/games/BasicPacMan/core/assets/pacman.png",
                100.0,
                true
        );

        EntityTypeRecord dotEntity = createEntityType(
                "Dot",
                "data/games/BasicPacMan/core/assets/dot.png",
                0.0,
                false
        );

        EntityTypeRecord ghostEntity = createEntityType(
                "Ghost",
                "data/games/BasicPacMan/core/assets/ghost.png",
                80.0,
                false
        );

        EntityTypeRecord wallEntity = createEntityType(
                "Wall",
                "data/games/BasicPacMan/core/assets/wall.png",
                0.0,
                false
        );

        // Add entity types to model
        model.addEntityType(playerEntity);
        model.addEntityType(dotEntity);
        model.addEntityType(ghostEntity);
        model.addEntityType(wallEntity);

        // Create level
        LevelDraft level = createDefaultLevel("Level 1", 20, 15);

        // Place entities
        // Player
        placeEntity(level, playerEntity, 100, 100);

        // Dots (just a few examples - a real template would place more)
        placeEntity(level, dotEntity, 150, 100);
        placeEntity(level, dotEntity, 200, 100);
        placeEntity(level, dotEntity, 250, 100);

        // Ghosts
        placeEntity(level, ghostEntity, 300, 300);
        placeEntity(level, ghostEntity, 400, 200);

        // Walls (just a few examples - a real template would place more)
        placeEntity(level, wallEntity, 50, 50);
        placeEntity(level, wallEntity, 50, 100);
        placeEntity(level, wallEntity, 50, 150);

        // Add level to model
        model.addLevel(level);

        // Create collision rules
        List<CollisionRule> rules = new ArrayList<>();

        // Player collects dot
        //rules.add(createCollisionRule("Player", "Dot", false, true, 10));

        // Player hits ghost
       // rules.add(createCollisionRule("Player", "Ghost", true, false, 0));

        // Add collision rules to model
        model.setCollisionRules(rules);
    }
}
