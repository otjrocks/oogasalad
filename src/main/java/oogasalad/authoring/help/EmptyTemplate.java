package oogasalad.authoring.help;

import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;

/**
 * Empty template as a fallback.
 * @author Angela Predolac
 */
public class EmptyTemplate extends GameTemplate {

    public EmptyTemplate() {
        super("Empty Project", "Start with a blank project");
    }

    @Override
    public void applyTo(AuthoringModel model) {
        // Clear everything
        model.clearAll();

        // Add a single empty level
        LevelDraft level = createDefaultLevel("Level 1", 20, 15);
        model.addLevel(level);

        // Set default game settings
        model.setGameTitle("My Game");
        model.setAuthor("Unknown Author");
        model.setGameDescription("A simple game created with the authoring environment");
    }
}
