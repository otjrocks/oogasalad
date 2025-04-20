package oogasalad.authoring.help;

import oogasalad.authoring.help.templates.*;

/**
 * Factory for creating various game templates.
 * @author Angela Predolac
 */
public class GameTemplateFactory {

    /**
     * Creates a game template by name.
     *
     * @param templateName the template identifier
     * @return the created game template
     */
    public static GameTemplate createTemplate(String templateName) {
        return switch (templateName.toLowerCase()) {
            case "pacman" -> new PacmanTemplate();
            case "platform" -> new PlatformTemplate();
            case "shooter" -> new ShooterTemplate();
            case "puzzle" -> new PuzzleTemplate();
            default -> new EmptyTemplate();
        };
    }
}
