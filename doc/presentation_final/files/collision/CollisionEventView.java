package oogasalad.authoring.view;

import static oogasalad.engine.utility.constants.GameConfig.ELEMENT_SPACING;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import oogasalad.engine.records.config.model.CollisionEventInterface;
import oogasalad.engine.utility.FileUtility;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.LoggingManager;
import oogasalad.engine.view.components.FormattingUtil;
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
        null, "collision-rule-selector",
        LanguageManager.getMessage("COLLISION_RULE_SELECTOR"), e -> updateParameterFields());
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
      for (Class<?> paramType : requiredFields.values()) {
        String userInput = myParameterFields.get(index).getText();
        constructorArgs[index] = FileUtility.castInputToCorrectType(userInput, paramType);
        index++;
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


  private void updateParameterFields() {
    myParameters.getChildren().clear();
    myParameterFields.clear();

    VBox parameterBox = new VBox();
    parameterBox.setSpacing(ELEMENT_SPACING); // vertical spacing between rows

    for (String parameter : getCollisionRequiredFields(mySelector.getValue()).keySet()) {
      Label parameterLabel = new Label(parameter + ": ");
      TextField parameterField = FormattingUtil.createTextField();
      parameterField.setId("parameter-" + parameter);
      VBox fieldGroup = new VBox(4); // small gap between label and text field
      fieldGroup.getChildren().addAll(parameterLabel, parameterField);
      parameterBox.getChildren().add(fieldGroup);
      myParameterFields.add(parameterField);
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
