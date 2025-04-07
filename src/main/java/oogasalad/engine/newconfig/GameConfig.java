package oogasalad.engine.newconfig;

import java.util.List;
import oogasalad.engine.newconfig.model.Level;
import oogasalad.engine.newconfig.model.Metadata;
import oogasalad.engine.newconfig.model.Settings;

/**
 * Represents the configuration for a game, containing metadata, settings, levels, 
 * and the path to the game folder.
 *
 * @param metadata       Metadata about the game, such as title, author, and description.
 * @param settings       General settings for the game, such as difficulty or preferences.
 * @param levels         A list of levels included in the game.
 * @param gameFolderPath The file path to the folder containing game resources.
 * 
 * @author Jessica Chen
 */
public record GameConfig(Metadata metadata, Settings settings, List<Level> levels,
                         String gameFolderPath) {

}
