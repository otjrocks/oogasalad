package oogasalad.engine.records.config;

public record ImageConfigRecord(
    String imagePath,
    Integer tileWidth,
    Integer tileHeight,
    Integer tilesToCycle,
    Double animationSpeed
) {

}
