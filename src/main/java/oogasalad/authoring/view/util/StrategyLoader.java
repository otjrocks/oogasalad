package oogasalad.authoring.view.util;

import oogasalad.engine.utility.FileUtility;
import oogasalad.engine.utility.LoggingManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to load available strategy classes via reflection.
 */
public class StrategyLoader {

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
