package oogasalad.engine.utility;

import java.io.File;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
   * Using reflection API, get a list of the required fields and their type for a given class.
   *
   * @param recordPath The path for the class you are querying for
   * @return A map with the key being the field name and the value being the type class for the field
   */
  public static Map<String, Class<?>> getRequiredFieldsForRecord(String recordPath) {
    Map<String, Class<?>> fields = new HashMap<>();
    try {
      Class<?> clazz = Class.forName(recordPath);
      if (clazz.isRecord()) {
        for (RecordComponent component : clazz.getRecordComponents()) {
          fields.put(component.getName(), component.getType());
        }
      } else {
        System.err.println("Class " + recordPath + " is not a record.");
      }
    } catch (ClassNotFoundException e) {
      System.err.println("Class not found: " + recordPath);
    }
    return fields;
  }
}