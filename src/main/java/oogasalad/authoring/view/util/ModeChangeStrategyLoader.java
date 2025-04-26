package oogasalad.authoring.view.util;

import oogasalad.engine.utility.FileUtility;
import oogasalad.engine.utility.LoggingManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to load available ModeChangeStrategy classes via reflection.
 */
public class ModeChangeStrategyLoader {

  private static final String RECORD_SUFFIX = "Strategy";

  public static Map<String, Class<?>> loadModeChangeStrategies(String packagePath, Class<?> interfaceType) {
    Map<String, Class<?>> classMap = new HashMap<>();
    try {
      String directoryPath = System.getProperty("user.dir") + "/target/classes/" + packagePath.replace('.', '/');
      List<String> classNames = FileUtility.getFileNamesInDirectory(directoryPath, ".class");

      for (String className : classNames) {
        try {
          Class<?> clazz = Class.forName(packagePath + "." + className);
          if (interfaceType.isAssignableFrom(clazz) && !clazz.isInterface() && !clazz.isAnnotation() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
            String simpleName = clazz.getSimpleName();
            if (simpleName.endsWith(RECORD_SUFFIX)) {
              simpleName = simpleName.substring(0, simpleName.length() - RECORD_SUFFIX.length());
            }
            classMap.put(simpleName, clazz);
          }
        } catch (Exception e) {
          LoggingManager.LOGGER.error("Failed to load ModeChangeStrategy class: {}", className, e);
        }
      }
    } catch (Exception e) {
      LoggingManager.LOGGER.error("Error scanning directory for ModeChangeStrategy classes", e);
    }
    return classMap;
  }
}
