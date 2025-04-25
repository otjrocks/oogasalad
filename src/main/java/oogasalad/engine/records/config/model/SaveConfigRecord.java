package oogasalad.engine.records.config.model;

import java.util.List;

/**
 * Record for representing a game save configuration.
 */
public record SaveConfigRecord(
    String saveName,
    int currentLevel,
    int totalScore,
    int lives,
    int highScore,
    List<Integer> levelOrder) {

}
