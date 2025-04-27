package oogasalad.engine.records.config;

import java.util.List;
import java.util.Map;
import oogasalad.engine.records.config.model.LevelRecord;
import oogasalad.engine.records.config.model.MetadataRecord;
import oogasalad.engine.records.config.model.SettingsRecord;

/**
 * Represents the configuration for a game, containing metadata, settings, levels, and the path to
 * the game folder.
 *
 * @param metadata       Metadata about the game, such as title, author, and description.
 * @param settings       General settings for the game, such as difficulty or preferences.
 * @param levels         A list of levels included in the game.
 * @param collisions     A list of all collisions in this game.
 * @param gameFolderPath The file path to the folder containing game resources.
 * @author Jessica Chen
 */
public record GameConfigRecord(MetadataRecord metadata, SettingsRecord settings,
                               List<LevelRecord> levels,
                               List<CollisionConfigRecord> collisions,
                               String gameFolderPath,
                               int currentLevelIndex,
                               Map<String, Double> respawnableEntities) {

}
