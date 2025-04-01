package oogasalad.engine;

import java.util.List;
import java.util.Objects;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import oogasalad.engine.utility.FileUtility;

/**
 * A class to handle the setting and switching of the programs theme.
 *
 * @author Owen Jennings
 */
public class ThemeManager {

  private static final String THEME_DIRECTORY_PATH = "src/main/resources/oogasalad/themes/";
  public static final String DEFAULT_THEME = "Light";
  public static final String COMMON_STYLES_PATH = "/oogasalad/styles.css";
  private static final String FONTS_PATH = "/oogasalad/fonts/";

  private final Scene myScene;

  /**
   * Initialize a Theme Controller.
   *
   * @param stage The stage you want the theme to be applied to
   */
  public ThemeManager(Stage stage) {
    myScene = stage.getScene();
    loadFonts();
    myScene.getStylesheets().add(getThemePath(DEFAULT_THEME));
    myScene.getStylesheets().add(COMMON_STYLES_PATH);
  }

  private void loadFonts() {
    Font.loadFont(
        Objects.requireNonNull(getClass().getResource(FONTS_PATH + "Default.ttf")).toExternalForm(),
        30);
    Font.loadFont(
        Objects.requireNonNull(getClass().getResource(FONTS_PATH + "Bold.ttf")).toExternalForm(),
        30);
  }

  /**
   * Set and update the theme to the theme specified by the name provided. Note: the theme must be a
   * valid theme name in the resources folder theme.
   *
   * @param themeName The name of the theme you wish to activate
   */
  public void setTheme(String themeName) {
    myScene.getStylesheets().clear();
    myScene.getStylesheets().add(getThemePath(themeName)); // add theme css file
    myScene.getStylesheets().add(COMMON_STYLES_PATH); // add common css file
  }

  /**
   * Get a list of all the theme display names.
   *
   * @return A list of strings representing the available themes.
   */
  public List<String> getAvailableThemes() {
    return FileUtility.getFileNamesInDirectory(THEME_DIRECTORY_PATH, ".css");
  }

  private String getThemePath(String themeName) {
    return Objects.requireNonNull(
        getClass().getResource("/oogasalad/themes/%s.css".formatted(themeName))).toExternalForm();
  }


}
