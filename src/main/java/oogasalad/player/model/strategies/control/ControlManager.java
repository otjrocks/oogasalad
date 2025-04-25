package oogasalad.player.model.strategies.control;

import java.util.List;
import java.util.Map;
import oogasalad.engine.utility.FileUtility;

/**
 * The ControlManager class provides utility methods for retrieving and managing control strategies,
 * target calculation strategies, and pathfinding strategies. It also facilitates access to
 * configuration details such as required fields and their order for these strategies.
 *
 * <p>This class interacts with the file system to locate strategy files and uses
 * reflection to determine the required fields for configuration classes.
 *
 * <p>Key functionalities include:
 * <ul>
 *   <li>Retrieving lists of available control, target calculation, and pathfinding strategies.</li>
 *   <li>Accessing required fields and their types for specific strategy configurations.</li>
 *   <li>Determining the order of required fields for strategy configurations.</li>
 * </ul>
 *
 * <p>Constants:
 * <ul>
 *   <li><b>CONTROL_PATH:</b> The base directory path for control-related files.</li>
 *   <li><b>CONTROL_CONFIG_PACKAGE_PATH:</b> The package path for control configuration classes.</li>
 *   <li><b>TARGET_CONFIG_PACKAGE_PATH:</b> The package path for target strategy configuration classes.</li>
 * </ul>
 *
 * <p>Dependencies:
 * This class relies on the {@code FileUtility} class for file system operations
 * and reflection-based field retrieval.
 *
 * <p>Usage Example:
 * <pre>{@code
 * List<String> controlStrategies = ControlManager.getControlStrategies();
 * Map<String, Class<?>> requiredFields = ControlManager.getControlRequiredFields("SomeControlStrategy");
 * }</pre>
 *
 * @author ChatGPT for javadoc comments
 */
public class ControlManager {

  private static final String CONTROL_PATH = "src/main/java/oogasalad/player/model/strategies/control/";
  private static final String CONTROL_CONFIG_PACKAGE_PATH = "oogasalad.engine.records.config.model.controlConfig.";
  private static final String TARGET_CONFIG_PACKAGE_PATH = "oogasalad.engine.records.config.model.controlConfig.targetStrategy.";


  /**
   * Retrieves a list of control strategy file names from the specified directory. This method
   * searches for files with the suffix "ControlStrategy.java" in the directory defined by the
   * CONTROL_PATH constant.
   *
   * @return a list of file names representing control strategies.
   */
  public static List<String> getControlStrategies() {
    return FileUtility.getFileNamesInDirectory(CONTROL_PATH, "ControlStrategy.java");
  }

  /**
   * Retrieves a list of target calculation strategy class names from the specified directory. The
   * method searches for files ending with "Strategy.java" within the "targetcalculation"
   * subdirectory of the control path.
   *
   * @return a list of strings representing the names of target calculation strategy files.
   */
  public static List<String> getTargetCalculationStrategies() {
    return FileUtility.getFileNamesInDirectory(CONTROL_PATH + "targetcalculation/",
        "Strategy.java");
  }

  /**
   * Retrieves a list of pathfinding strategy file names from the specified directory. The method
   * searches for files with the extension "PathFindingStrategy.java" within the "pathfinding"
   * subdirectory of the control path.
   *
   * @return a list of file names representing available pathfinding strategies.
   */
  public static List<String> getPathFindingStrategies() {
    return FileUtility.getFileNamesInDirectory(CONTROL_PATH + "pathfinding/",
        "PathFindingStrategy.java");
  }

  /**
   * Retrieves a map of required fields and their corresponding types for a given control strategy.
   * This method uses the control strategy name to locate the associated configuration class and
   * determines the required fields for that class.
   *
   * @param controlStrategyName the name of the control strategy whose required fields are to be
   *                            retrieved
   * @return a map where the keys are the names of the required fields and the values are their
   * respective types
   */
  public static Map<String, Class<?>> getControlRequiredFields(String controlStrategyName) {
    return FileUtility.getRequiredFieldsForRecord(
        CONTROL_CONFIG_PACKAGE_PATH + controlStrategyName + "ControlConfigRecord");
  }

  /**
   * Retrieves a map of required fields and their corresponding types for a given target strategy
   * configuration.
   *
   * @param targetStrategyName the name of the target strategy whose required fields are to be
   *                           retrieved
   * @return a map where the keys are the names of the required fields and the values are their
   * respective types
   */
  public static Map<String, Class<?>> getTargetRequiredFields(String targetStrategyName) {
    return FileUtility.getRequiredFieldsForRecord(
        TARGET_CONFIG_PACKAGE_PATH + targetStrategyName + "ConfigRecord");
  }

  /**
   * Retrieves the list of required field names in the specified order for a given control
   * strategy's configuration.
   *
   * @param controlStrategyName the name of the control strategy whose configuration fields are to
   *                            be retrieved
   * @return a list of field names in the required order for the specified control strategy's
   * configuration
   */
  public static List<String> getControlRequiredFieldsOrder(String controlStrategyName) {
    return FileUtility.getRequiredFieldsForRecordOrder(
        CONTROL_CONFIG_PACKAGE_PATH + controlStrategyName + "ControlConfigRecord");
  }

  /**
   * Retrieves the required fields in the specified order for a target strategy configuration.
   *
   * @param targetStrategyName the name of the target strategy whose configuration fields are to be
   *                           retrieved
   * @return a list of strings representing the required fields in the specified order for the
   * target strategy configuration
   */
  public static List<String> getTargetRequiredFieldsOrder(String targetStrategyName) {
    return FileUtility.getRequiredFieldsForRecordOrder(
        TARGET_CONFIG_PACKAGE_PATH + targetStrategyName + "ConfigRecord");
  }
}
