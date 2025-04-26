package oogasalad.authoring.view.gameSettings;

import oogasalad.engine.utility.FileUtility;
import oogasalad.engine.utility.LoggingManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for dynamically loading condition classes
 * (win/lose conditions) from a given package using reflection.
 * <p>
 * This allows the GameSettingsView to populate dropdowns without
 * hardcoding available condition types.
 * </p>
 *
 * Classes are filtered to match a given interface type and are simplified
 * by removing the "Record" suffix from their names for display.
 *
 * @author
 * William He
 */
public class ConditionLoader {

  private static final String RECORD_SUFFIX = "Record";

  /**
   * Loads all classes in the given package that implement the specified interface type.
   *
   * @param packagePath  the fully qualified package path (e.g., "oogasalad.engine.records.config.model.wincondition")
   * @param interfaceType the interface class the loaded classes must implement
   * @return a map from simplified class name to Class object
   */
  public static Map<String, Class<?>> loadConditionClasses(String packagePath, Class<?> interfaceType) {
    Map<String, Class<?>> classMap = new HashMap<>();
    try {
      String directoryPath = System.getProperty("user.dir") + "/target/classes/" + packagePath.replace('.', '/');
      List<String> classNames = getClassNamesInDirectory(directoryPath);
      classMap.putAll(loadValidConditionClasses(packagePath, classNames, interfaceType));
    } catch (Exception e) {
      LoggingManager.LOGGER.error("Error scanning directory for conditions", e);
    }
    return classMap;
  }

  /**
   * Retrieves the names of all class files in the specified directory.
   *
   * @param directoryPath the file system path to scan
   * @return a list of class names (without file extensions)
   */
  private static List<String> getClassNamesInDirectory(String directoryPath) {
    return FileUtility.getFileNamesInDirectory(directoryPath, ".class");
  }

  /**
   * Loads and validates condition classes from a list of class names.
   *
   * @param packagePath the package to load classes from
   * @param classNames  the list of class names
   * @param interfaceType the interface the classes must implement
   * @return a map of valid condition class names to Class objects
   */
  private static Map<String, Class<?>> loadValidConditionClasses(String packagePath, List<String> classNames, Class<?> interfaceType) {
    Map<String, Class<?>> validClasses = new HashMap<>();
    for (String className : classNames) {
      try {
        Class<?> clazz = Class.forName(packagePath + "." + className);
        if (isValidConditionClass(clazz, interfaceType)) {
          String simpleName = clazz.getSimpleName();
          if (simpleName.endsWith(RECORD_SUFFIX)) {
            simpleName = simpleName.substring(0, simpleName.length() - RECORD_SUFFIX.length());
          }
          validClasses.put(simpleName, clazz);
        }
      } catch (Exception e) {
        LoggingManager.LOGGER.error("Failed to load class: {}", className, e);
      }
    }
    return validClasses;
  }

  /**
   * Checks if a class is a non-abstract implementation of the given interface.
   *
   * @param clazz         the class to check
   * @param interfaceType the required interface type
   * @return true if valid, false otherwise
   */
  private static boolean isValidConditionClass(Class<?> clazz, Class<?> interfaceType) {
    return interfaceType.isAssignableFrom(clazz)
        && !clazz.isInterface()
        && !clazz.isAnnotation()
        && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers());
  }
}
