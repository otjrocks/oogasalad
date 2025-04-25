package oogasalad.engine.records.config.model;

public record ModeChangeInfo(String originalMode, String transitionMode, double revertTime,
                             double transitionTime) {

}
