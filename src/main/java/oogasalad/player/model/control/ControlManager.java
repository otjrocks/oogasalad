package oogasalad.player.model.control;

import java.util.List;
import java.util.Map;
import oogasalad.engine.utility.FileUtility;

public class ControlManager {

  private static final String CONTROL_PATH = "src/main/java/oogasalad/player/model/control/";
  private static final String CONTROL_CONFIG_PACKAGE_PATH = "oogasalad.engine.model.controlConfig.";
  private static final String TARGET_CONFIG_PACKAGE_PATH = "oogasalad.engine.model.controlConfig.targetStrategy.";


  public static List<String> getControlStrategies() {
    return FileUtility.getFileNamesInDirectory(CONTROL_PATH, "ControlStrategy.java");
  }

  public static List<String> getTargetCalculationStrategies() {
    return FileUtility.getFileNamesInDirectory(CONTROL_PATH + "targetcalculation/",
        "Strategy.java");
  }

  public static List<String> getPathFindingStrategies() {
    return FileUtility.getFileNamesInDirectory(CONTROL_PATH + "pathfinding/",
        "PathFindingStrategy.java");
  }

  public static Map<String, Class<?>> getControlRequiredFields(String controlStrategyName) {
    return FileUtility.getRequiredFieldsForRecord(
        CONTROL_CONFIG_PACKAGE_PATH + controlStrategyName + "ControlConfig");
  }

  public static Map<String, Class<?>> getTargetRequiredFields(String targetStrategyName) {
    return FileUtility.getRequiredFieldsForRecord(
        TARGET_CONFIG_PACKAGE_PATH + targetStrategyName + "Config");
  }
}
