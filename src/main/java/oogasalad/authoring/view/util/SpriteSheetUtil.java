package oogasalad.authoring.view.util;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import oogasalad.engine.records.config.ModeConfigRecord;

/**
 * Utility class for extracting a single preview tile from a sprite sheet image defined by a
 * {@link ModeConfigRecord}.
 * <p>
 * This is used in the Authoring Environment to visually represent the default frame (first tile) of
 * an entity's sprite sheet, typically for thumbnail or tile display purposes.
 * </p>
 */
public class SpriteSheetUtil {

  /**
   * Extracts the first tile (frame 0, direction row 0) from a sprite sheet defined in the given
   * {@link ModeConfigRecord}.
   * <p>
   * The method loads the full sprite sheet and extracts a subimage starting at (0, 0) with width
   * and height specified by the tile dimensions in the {@code ImageConfig} of the
   * {@code ModeConfig}.
   * </p>
   *
   * @param config the {@link ModeConfigRecord} containing image configuration metadata
   * @return a {@link WritableImage} representing the first tile of the sprite sheet
   */
  public static WritableImage getPreviewTile(ModeConfigRecord config) {
    String imagePath = config.image().imagePath();
    int tileWidth = config.image().tileWidth();
    int tileHeight = config.image().tileHeight();

    Image fullImage = new Image(imagePath, false);
    return new WritableImage(fullImage.getPixelReader(), 0, 0, tileWidth, tileHeight);
  }

}
