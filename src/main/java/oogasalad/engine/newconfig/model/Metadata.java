package oogasalad.engine.newconfig.model;

/**
 * Represents metadata information for a game. This includes the game's title, author, and a
 * description.
 *
 * @param gameTitle       the title of the game
 * @param author          the author of the game
 * @param gameDescription a brief description of the game
 * @author Jessica Chen
 */
public record Metadata(String gameTitle, String author, String gameDescription) {

}
