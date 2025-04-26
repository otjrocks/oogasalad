package oogasalad.engine.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.Scene;
import javafx.scene.text.Font;

/**
 * Singleton ThemeManager to handle the setting and switching of the program's theme across all
 * windows.
 *
 * @author Owen Jennings
 */
public class ThemeManager {

  private static final String THEME_DIRECTORY_PATH = "src/main/resources/oogasalad/themes/";
  public static final String DEFAULT_THEME = "Light";
  private static final String COMMON_STYLES_PATH = "/oogasalad/styles.css";
  private static final String FONTS_PATH = "/oogasalad/fonts/";

  private static ThemeManager instance;

  private final List<Scene> registeredScenes = new ArrayList<>();
  private String currentTheme = DEFAULT_THEME;

  private ThemeManager() {
    loadFonts();
  }

  /**
   * Get the single instance of ThemeManager.
   *
   * @return the ThemeManager instance.
   */
  public static ThemeManager getInstance() {
    if (instance == null) {
      instance = new ThemeManager();
    }
    return instance;
  }

  /**
   * Register a scene to receive theme updates.
   *
   * @param scene The scene to register.
   */
  public void registerScene(Scene scene) {
    registeredScenes.add(scene);
    applyThemeToScene(scene, currentTheme);
  }

  private void loadFonts() {
    Font.loadFont(
        Objects.requireNonNull(getClass().getResource(FONTS_PATH + "Default.ttf"))
            .toExternalForm(),
        30);
    Font.loadFont(
        Objects.requireNonNull(getClass().getResource(FONTS_PATH + "Bold.ttf"))
            .toExternalForm(),
        30);
  }

  /**
   * Set and update the theme for all registered scenes.
   *
   * @param themeName The name of the theme you wish to activate.
   */
  public void setTheme(String themeName) {
    currentTheme = themeName;
    for (Scene scene : registeredScenes) {
      applyThemeToScene(scene, themeName);
    }
  }

  /**
   * Get a list of all available themes.
   *
   * @return A list of theme names.
   */
  public List<String> getAvailableThemes() {
    return FileUtility.getFileNamesInDirectory(THEME_DIRECTORY_PATH, ".css");
  }

  private void applyThemeToScene(Scene scene, String themeName) {
    scene.getStylesheets().clear();
    scene.getStylesheets().add(getThemePath(themeName));
    scene.getStylesheets().add(COMMON_STYLES_PATH);
  }

  private String getThemePath(String themeName) {
    return Objects.requireNonNull(
            getClass().getResource("/oogasalad/themes/%s.css".formatted(themeName)))
        .toExternalForm();
  }
}
