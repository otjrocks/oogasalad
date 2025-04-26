package oogasalad.authoring.view.util;

import oogasalad.engine.utility.FileUtility;
import oogasalad.engine.utility.LoggingManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to dynamically load strategy classes via reflection.
 * <p>
 * This class searches for concrete implementations of a given interface within a specified package,
 * strips known suffixes (e.g., "SpawnEventStrategy", "ModeChangeEventStrategy", "Strategy"),
 * and returns a map from simple, cleaned strategy names to their corresponding Class objects.
 * <p>
 * Example use cases: loading spawn event strategies, mode change event strategies, etc.
 *
 * @author Will He
 */
public class StrategyLoader {

  /**
   * Loads all valid strategy classes from the specified package that implement the given interface.
   *
   * @param packagePath the Java package path (e.g., "oogasalad.player.model.strategies.spawnevent")
   * @param interfaceType the interface type all discovered classes must implement
   * @return a map of cleaned simple names (e.g., "TimeElapsed") to their corresponding Class objects
   */
  public static Map<String, Class<?>> loadStrategies(String packagePath, Class<?> interfaceType) {
    Map<String, Class<?>> classMap = new HashMap<>();
    try {
      String directoryPath = System.getProperty("user.dir") + "/target/classes/" + packagePath.replace('.', '/');
      List<String> classNames = FileUtility.getFileNamesInDirectory(directoryPath, ".class");

      for (String className : classNames) {
        tryLoadStrategyClass(packagePath, className, interfaceType, classMap);
      }
    } catch (Exception e) {
      LoggingManager.LOGGER.error("Error scanning directory for strategy classes", e);
    }
    return classMap;
  }

  /**
   * Attempts to load a single class and add it to the strategies map if it matches the required interface.
   *
   * @param packagePath the base package path
   * @param className the name of the class file (without package prefix)
   * @param interfaceType the required interface type the class must implement
   * @param classMap the map to populate with valid strategy classes
   */
  private static void tryLoadStrategyClass(
      String packagePath,
      String className,
      Class<?> interfaceType,
      Map<String, Class<?>> classMap
  ) {
    try {
      Class<?> clazz = Class.forName(packagePath + "." + className);
      if (interfaceType.isAssignableFrom(clazz)
          && !clazz.isInterface()
          && !clazz.isAnnotation()
          && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {

        String simpleName = cleanSimpleName(clazz);
        classMap.put(simpleName, clazz);
      }
    } catch (Exception e) {
      LoggingManager.LOGGER.error("Failed to load strategy class: {}", className, e);
    }
  }

  /**
   * Cleans the simple name of a strategy class by removing known suffixes.
   * <p>
   * Recognized suffixes are: "SpawnEventStrategy", "ModeChangeEventStrategy", and "Strategy".
   * If no suffix matches, the name is returned as-is.
   *
   * @param clazz the Class object whose simple name to clean
   * @return the cleaned simple name
   */
  private static String cleanSimpleName(Class<?> clazz) {
    String simpleName = clazz.getSimpleName();

    if (simpleName.endsWith("SpawnEventStrategy")) {
      simpleName = simpleName.substring(0, simpleName.length() - "SpawnEventStrategy".length());
    } else if (simpleName.endsWith("ModeChangeEventStrategy")) {
      simpleName = simpleName.substring(0, simpleName.length() - "ModeChangeEventStrategy".length());
    } else if (simpleName.endsWith("Strategy")) {
      simpleName = simpleName.substring(0, simpleName.length() - "Strategy".length());
    }

    return simpleName;
  }
}
