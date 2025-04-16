package oogasalad.engine.records.config;

public record ImageConfig(
    String imagePath,
    Integer tileWidth,
    Integer tileHeight,
    Integer tilesToCycle,
    Double animationSpeed
) {

}
