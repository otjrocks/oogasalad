package oogasalad.authoring.view;

import static oogasalad.engine.utility.constants.GameConfig.ELEMENT_SPACING;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.authoring.view.util.InputField;
import oogasalad.authoring.view.util.InputFieldFactory;
import oogasalad.engine.records.config.model.CollisionEventInterface;
import oogasalad.engine.utility.FileUtility;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.engine.view.components.Selector;

/**
 * A view to create and edit a collision event
 */
public class CollisionEventView {

  private static final String COLLISION_EVENTS_PATH = "src/main/java/oogasalad/engine/records/config/model/collisionevent/";
  private static final String COLLISION_EVENTS_PACKAGE_PATH = "oogasalad.engine.records.config.model.collisionevent.";
  private final VBox myRoot;
  private final Selector mySelector;
  private final VBox myParameters;
  private final List<InputField> myParameterFields;
  private final InputFieldFactory fieldFactory;
  private final Map<String, InputField> inputFieldMap;


  public CollisionEventView(
      String labelText,
      Supplier<List<String>> entityTypeSupplier,
      Function<String, List<String>> modeSupplierForType
  ) {
    myRoot = new VBox();
    myParameters = new VBox();
    myRoot.setSpacing(ELEMENT_SPACING);
    myParameterFields = new ArrayList<>();

    mySelector = new Selector(getCollisionEventClassNames(),
        null, "collision-rule-selector",
        LanguageManager.getMessage("COLLISION_RULE_SELECTOR"), e -> updateParameterFields());

    fieldFactory = new InputFieldFactory(entityTypeSupplier, modeSupplierForType);
    inputFieldMap = new HashMap<>();

    myRoot.getChildren().add(new Label(labelText));
    myRoot.getChildren().add(mySelector.getRoot());
    myRoot.getChildren().add(myParameters);
  }


  /**
   * Get the root element of this view.
   *
   * @return A VBox that contains all of this view's elements.
   */
  public VBox getRoot() {
    return myRoot;
  }

  /**
   * Get the collision event corresponding to the currently selected values.
   *
   * @return A collision event created by the current selected values.
   * @throws IllegalArgumentException If the collision event cannot be created with the provided
   *                                  parameters.
   */
  public CollisionEventInterface getCollisionEvent() {
    try {
      String collisionName = mySelector.getValue();
      String fullClassName = COLLISION_EVENTS_PACKAGE_PATH + collisionName + "CollisionEventRecord";
      Class<?> collisionEventClass = Class.forName(fullClassName);

      Map<String, Class<?>> requiredFields = getCollisionRequiredFields(collisionName);
      Object[] constructorArgs = new Object[requiredFields.size()];

      int index = 0;
      for (String param : requiredFields.keySet()) {
        String userInput = inputFieldMap.get(param).getValue();
        Class<?> type = requiredFields.get(param);
        constructorArgs[index++] = parseInput(userInput, type);
      }

      Constructor<?> constructor = collisionEventClass.getDeclaredConstructors()[0];
      Object eventInstance = constructor.newInstance(constructorArgs);

      return (CollisionEventInterface) eventInstance;

    } catch (IllegalArgumentException e) {
      throw e;
    } catch (Exception e) {
      LoggingManager.LOGGER.warn("Error getting collision event", e);
      throw new RuntimeException("Failed to create CollisionEvent: " + e.getMessage());
    }
  }


  private static final Map<Class<?>, Function<String, Object>> PARSERS = Map.of(
      int.class, Integer::parseInt,
      Integer.class, Integer::parseInt,
      double.class, Double::parseDouble,
      Double.class, Double::parseDouble,
      boolean.class, Boolean::parseBoolean,
      Boolean.class, Boolean::parseBoolean
  );


  private Object parseInput(String input, Class<?> targetType) {
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


  private void updateParameterFields() {
    myParameters.getChildren().clear();
    inputFieldMap.clear();

    VBox parameterBox = new VBox(10);
    Map<String, Class<?>> required = getCollisionRequiredFields(mySelector.getValue());

    for (Map.Entry<String, Class<?>> entry : required.entrySet()) {
      String paramName = entry.getKey();
      Class<?> paramType = entry.getValue();

      InputField input = fieldFactory.create(paramName, paramType, inputFieldMap);
      inputFieldMap.put(paramName, input);

      VBox fieldGroup = new VBox(4, new Label(paramName + ":"), input.getNode());
      parameterBox.getChildren().add(fieldGroup);
    }

    myParameters.getChildren().add(parameterBox);
  }

  private static List<String> getCollisionEventClassNames() {
    return FileUtility.getFileNamesInDirectory(COLLISION_EVENTS_PATH, "CollisionEventRecord.java");
  }

  private static Map<String, Class<?>> getCollisionRequiredFields(String collisionName) {
    return FileUtility.getRequiredFieldsForRecord(
        COLLISION_EVENTS_PACKAGE_PATH + collisionName + "CollisionEventRecord");
  }
}

