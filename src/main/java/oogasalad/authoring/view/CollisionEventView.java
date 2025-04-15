package oogasalad.authoring.view;

import static oogasalad.engine.config.GameConfig.ELEMENT_SPACING;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.LoggingManager;
import oogasalad.engine.records.config.model.CollisionEvent;
import oogasalad.engine.utility.FileUtility;
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
  private final List<TextField> myParameterFields;

  /**
   * Create a collision event view with the provided label text.
   *
   * @param labelText The label text for this view.
   */
  public CollisionEventView(String labelText) {
    myRoot = new VBox();
    myParameters = new VBox();
    myRoot.setSpacing(ELEMENT_SPACING);
    myParameterFields = new ArrayList<>();

    mySelector = new Selector(getCollisionEventClassNames(),
        getCollisionEventClassNames().getFirst(), "collision-rule-selector",
        LanguageManager.getMessage("COLLISION_RULE_SELECTOR"), e -> updateParameterFields());
    myRoot.getChildren().add(new Label(labelText));
    myRoot.getChildren().add(mySelector);
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
  public CollisionEvent getCollisionEvent() throws IllegalArgumentException {
    try {
      String collisionName = mySelector.getValue();
      String fullClassName = COLLISION_EVENTS_PACKAGE_PATH + collisionName + "CollisionEvent";
      Class<?> collisionEventClass = Class.forName(fullClassName);

      Map<String, Class<?>> requiredFields = getCollisionRequiredFields(collisionName);
      Object[] constructorArgs = new Object[requiredFields.size()];

      int index = 0;
      for (Class<?> paramType : requiredFields.values()) {
        String userInput = myParameterFields.get(index).getText();
        constructorArgs[index] = parseInput(userInput, paramType);
        index++;
      }

      Constructor<?> constructor = collisionEventClass.getDeclaredConstructors()[0];
      Object eventInstance = constructor.newInstance(constructorArgs);

      return (CollisionEvent) eventInstance;

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
    myParameterFields.clear();
    HBox parameterBox = new HBox();
    parameterBox.setSpacing(ELEMENT_SPACING);
    for (String parameter : getCollisionRequiredFields(mySelector.getValue()).keySet()) {
      Label parameterLabel = new Label(parameter + ": ");
      TextField parameterField = new TextField();
      parameterBox.getChildren().addAll(parameterLabel, parameterField);
      myParameterFields.add(parameterField);
    }
    myParameters.getChildren().add(parameterBox);
  }

  private static List<String> getCollisionEventClassNames() {
    return FileUtility.getFileNamesInDirectory(COLLISION_EVENTS_PATH, "CollisionEvent.java");
  }

  private static Map<String, Class<?>> getCollisionRequiredFields(String collisionName) {
    return FileUtility.getRequiredFieldsForRecord(
        COLLISION_EVENTS_PACKAGE_PATH + collisionName + "CollisionEvent");
  }
}
