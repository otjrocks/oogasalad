package oogasalad.engine.utility.constants;

import java.util.ResourceBundle;

/**
 * Static constant values for the game.
 *
 * @author Owen Jennings
 */
public class GameConfig {

  private static final String GAME_CONFIG_FILE_PATH = "oogasalad.config.GameConfig";
  private static final ResourceBundle GAME_CONFIG_BUNDLE = ResourceBundle.getBundle(
      GAME_CONFIG_FILE_PATH);
  public static final int WIDTH = Integer.parseInt(GAME_CONFIG_BUNDLE.getString("WIDTH"));
  public static final int HEIGHT = Integer.parseInt(GAME_CONFIG_BUNDLE.getString("HEIGHT"));
  public static final int ELEMENT_SPACING = Integer.parseInt(
      GAME_CONFIG_BUNDLE.getString("ELEMENT_SPACING"));
  public static final int MARGIN = Integer.parseInt(GAME_CONFIG_BUNDLE.getString("MARGIN"));
}
