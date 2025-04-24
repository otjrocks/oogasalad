package oogasalad.engine.utility;

import java.io.File;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A class containing utility methods pertaining to files. From Cell Society project.
 *
 * @author Owen Jennings
 */
public class FileUtility {

  /**
   * Get a list of all the files in the provided directory that end with a specific string or file
   * extension.
   *
   * @param directoryPath The directory you are querying
   * @param ending        The ending of the files you which to query for
   * @return A list of all the file names in the directory provided that end with the provided
   * ending
   */
  public static List<String> getFileNamesInDirectory(String directoryPath, String ending) {
    List<String> fileNames = new ArrayList<>();
    File directory = new File(directoryPath);
    File[] files = directory.listFiles((file, name) -> name.endsWith(ending));

    if (files != null) {
      for (File file : files) {
        fileNames.add(file.getName().replace(ending, ""));
      }
    }
    Collections.sort(fileNames);
    return fileNames;
  }

  /**
   * Get a list of all the folder names in the provided directory.
   *
   * @param directoryPath The directory you are querying
   * @return A list of all the folder names in the directory provided
   */
  public static List<String> getFolderNamesInDirectory(String directoryPath) {
    List<String> folderNames = new ArrayList<>();
    File directory = new File(directoryPath);
    File[] folders = directory.listFiles(File::isDirectory);

    if (folders != null) {
      for (File folder : folders) {
        folderNames.add(folder.getName());
      }
    }
    Collections.sort(folderNames);
    return folderNames;
  }


  /**
   * Using reflection API, get a list of the required fields and their type for a given class.
   *
   * @param recordPath The path for the class you are querying for
   * @return A map with the key being the field name and the value being the type class for the
   * field
   */
  public static Map<String, Class<?>> getRequiredFieldsForRecord(String recordPath) {
    // ChatGPT assisted in generating this method.
    Map<String, Class<?>> fields = new HashMap<>();
    try {
      Class<?> clazz = Class.forName(recordPath);
      if (clazz.isRecord()) {
        for (RecordComponent component : clazz.getRecordComponents()) {
          fields.put(component.getName(), component.getType());
        }
      } else {
        LoggingManager.LOGGER.warn("Class {} is not a record.", recordPath);
      }
    } catch (ClassNotFoundException e) {
      LoggingManager.LOGGER.warn("Class not found: {}", recordPath);
    }
    return fields;
  }

  /**
   * Using reflection API, get a list of the required fields and their type for a given class.
   *
   * @param recordPath The path for the class you are querying for
   * @return A map with the key being the field name and the value being the type class for the
   * field
   */
  public static List<String> getRequiredFieldsForRecordOrder(String recordPath) {
    // ChatGPT assisted in generating this method.
    List<String> fields = new ArrayList<>();
    try {
      Class<?> clazz = Class.forName(recordPath);
      if (clazz.isRecord()) {
        for (RecordComponent component : clazz.getRecordComponents()) {
          fields.add(component.getName());
        }
      } else {
        LoggingManager.LOGGER.warn("Class {} is not a record.", recordPath);
      }
    } catch (ClassNotFoundException e) {
      LoggingManager.LOGGER.warn("Class not found: {}", recordPath);
    }
    return fields;
  }

  private static final Map<Class<?>, Function<String, Object>> PARSERS = Map.of(
      int.class, Integer::parseInt,
      Integer.class, Integer::parseInt,
      double.class, Double::parseDouble,
      Double.class, Double::parseDouble,
      boolean.class, Boolean::parseBoolean,
      Boolean.class, Boolean::parseBoolean
  );


  /**
   * Cast a string object to the correct object type.
   *
   * @param input      The string input
   * @param targetType The type you want to cast the string to.
   * @return The correctly object from the parsed/cast string.
   */
  public static Object castInputToCorrectType(String input, Class<?> targetType) {
    Function<String, Object> parser = PARSERS.get(targetType);
    if (parser == null) { // Fall back to just returning the string for unknown types (optional)
      return input;
    }
    try {
      return parser.apply(input);
    } catch (Exception e) {
      LoggingManager.LOGGER.warn("Unable to parse input into correct parameter format", e);
      throw new IllegalArgumentException("Unable to parse input into correct parameter format");
    }
  }
}