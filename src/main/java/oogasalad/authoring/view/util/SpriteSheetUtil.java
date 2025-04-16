package oogasalad.authoring.view.util;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import oogasalad.engine.config.ModeConfig;

/**
 * Utility for extracting a tile from a sprite sheet to preview in Authoring.
 */
public class SpriteSheetUtil {


  public static WritableImage getPreviewTile(ModeConfig config) {
    String imagePath = config.image().imagePath();
    int tileWidth = config.image().tileWidth();
    int tileHeight = config.image().tileHeight();

    Image fullImage = new Image(imagePath, false);



    return new WritableImage(fullImage.getPixelReader(), 0, 0, tileWidth, tileHeight);
  }

}
