package oogasalad.engine.newconfig.model;

/**
 * Represents a game level with its associated settings and map.
 *
 * @param settings the settings associated with the level, such as difficulty or rules
 * @param levelMap the string representation of the level's map
 * @author Jessica Chen
 */
public record Level(Settings settings, String levelMap) {

}
