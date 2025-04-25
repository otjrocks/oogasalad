package oogasalad.engine.records.config.model;

/**
 * Represents metadata information for a game. This includes the game's title, author, and a
 * description.
 *
 * @param gameTitle       the title of the game
 * @param author          the author of the game
 * @param gameDescription a brief description of the game
 * @param image           string to file path from the game folder its found in like entity images
 * @author Jessica Chen
 */
public record MetadataRecord(String gameTitle, String author, String gameDescription,
                             String image) {

}
