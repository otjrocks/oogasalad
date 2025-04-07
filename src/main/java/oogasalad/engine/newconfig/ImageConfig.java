package oogasalad.engine.newconfig;

import java.util.List;

public record ImageConfig(
    String imagePath,
    Integer tileWidth,
    Integer tileHeight,
    List<Integer> tilesToCycle,
    Double animationSpeed
) {}
