package oogasalad.authoring.view;

import static oogasalad.engine.utility.constants.GameConfig.ELEMENT_SPACING;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import oogasalad.engine.records.config.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.records.config.model.controlConfig.targetStrategy.TargetCalculationConfigInterface;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.player.model.strategies.control.ControlManager;

public class ControlTypeEditorView {

  private static final String TARGET_CALCULATION_CONFIG = "targetCalculationConfig";
  private static final String PATH_FINDING_STRATEGY = "pathFindingStrategy";
  private final ComboBox<String> controlTypeBox;
  private final VBox controlTypeParameters;
  private final List<TextField> controlTypeParameterFields;
  private final List<TextField> targetStrategyParameterFields;
  private final Map<String, ComboBox<String>> controlTypeComboBoxes = new HashMap<>();
  private final VBox root;

  public ControlTypeEditorView(ControlConfigInterface current) {
    root = new VBox();
    root.setSpacing(ELEMENT_SPACING);
    root.setPadding(new Insets(ELEMENT_SPACING));
    root.getStyleClass().add("control-type-editor-view");

    controlTypeBox = new ComboBox<>();
    controlTypeBox.getItems().addAll(ControlManager.getControlStrategies());
    controlTypeBox.setValue("None");
    controlTypeBox.setOnAction(e -> updateControlParameterFields());

    controlTypeParameters = new VBox(ELEMENT_SPACING);
    controlTypeParameters.setPrefSize(400, 200);
    controlTypeParameters.setPadding(new Insets(ELEMENT_SPACING));
    controlTypeParameterFields = new ArrayList<>();
    targetStrategyParameterFields = new ArrayList<>();

    ScrollPane controlTypeScrollPane = new ScrollPane(controlTypeParameters);
    controlTypeScrollPane.setFitToWidth(true);

    controlTypeScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    controlTypeScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

    root.getChildren().addAll(
        new Label(LanguageManager.getMessage("CONTROL_STRATEGY")), controlTypeBox,
        controlTypeScrollPane
    );

    populateControlConfigUI(current);
  }

  public VBox getRoot() {
    return root;
  }


  public ControlConfigInterface getControlConfig() {
    String controlType = controlTypeBox.getValue();
    Map<String, Class<?>> requiredFieldTypes = ControlManager.getControlRequiredFields(controlType);
    List<String> requiredFieldOrder = ControlManager.getControlRequiredFieldsOrder(controlType);

    try {
      List<Object> constructorArgs = new ArrayList<>();
      int textFieldIndex = 0;

      for (String param : requiredFieldOrder) {
        Class<?> type = requiredFieldTypes.get(param);

        if (param.startsWith(PATH_FINDING_STRATEGY)) {
          constructorArgs.add(controlTypeComboBoxes.get(param).getValue());

        } else if (param.startsWith(TARGET_CALCULATION_CONFIG)) {
          constructorArgs.add(buildTargetStrategyFromUI());

        } else {
          String input = controlTypeParameterFields.get(textFieldIndex++).getText();
          constructorArgs.add(castToRequiredType(input, type));
        }
      }

      String fullClassName =
          "oogasalad.engine.records.config.model.controlConfig." + controlType
              + "ControlConfigRecord";
      Class<?> configClass = Class.forName(fullClassName);
      Constructor<?> constructor = configClass.getDeclaredConstructors()[0];

      return (ControlConfigInterface) constructor.newInstance(constructorArgs.toArray());

    } catch (Exception e) {
      showError("Error building control config: " + e.getMessage());
      throw new ViewException("Error building control config: ", e);
    }
  }


  public void populateControlConfigUI(ControlConfigInterface config) {
    String type = config.getClass().getSimpleName().replace("ControlConfigRecord", "");
    controlTypeBox.setValue(type);

    controlTypeParameters.getChildren().clear();
    controlTypeParameterFields.clear();
    targetStrategyParameterFields.clear();
    controlTypeComboBoxes.clear();

    ControlManager.getControlRequiredFields(type);
    List<String> fieldOrder = ControlManager.getControlRequiredFieldsOrder(type);

    for (String field : fieldOrder) {
      Label label = new Label(field + ":");

      if (field.startsWith(PATH_FINDING_STRATEGY)) {
        ComboBox<String> combo = new ComboBox<>();
        combo.setItems(
            FXCollections.observableArrayList(ControlManager.getPathFindingStrategies()));
        combo.setValue(getStrategyValueFromConfig(config, field));
        controlTypeComboBoxes.put(field, combo);
        controlTypeParameters.getChildren().add(new VBox(label, combo));

      } else if (field.startsWith(TARGET_CALCULATION_CONFIG)) {
        ComboBox<String> targetCombo = new ComboBox<>();
        targetCombo.setItems(
            FXCollections.observableArrayList(ControlManager.getTargetCalculationStrategies()));
        String selected = getStrategyValueFromConfig(config, field);
        targetCombo.setValue(selected);
        controlTypeComboBoxes.put(field, targetCombo);

        VBox targetParamsBox = new VBox(5);
        updateTargetParameterFields(targetCombo, targetParamsBox);
        targetCombo.setOnAction(e -> updateTargetParameterFields(targetCombo, targetParamsBox));

        controlTypeParameters.getChildren().add(new VBox(label, targetCombo, targetParamsBox));

      } else {
        String value = getFieldValueFromConfig(config, field);
        TextField tf = new TextField(value);
        controlTypeParameterFields.add(tf);
        controlTypeParameters.getChildren().add(new VBox(label, tf));
      }
    }
  }


  private void updateControlParameterFields() {
    controlTypeParameters.getChildren().clear();
    controlTypeParameterFields.clear();

    VBox parameterBox = new VBox(5);
    List<String> requiredFields =
        ControlManager.getControlRequiredFieldsOrder(controlTypeBox.getValue());
    for (String parameter : requiredFields) {
      Node parameterNode = createParameterNode(parameter);
      parameterBox.getChildren().add(parameterNode);
    }
    controlTypeParameters.getChildren().add(parameterBox);
  }


  private Node createParameterNode(String parameter) {
    Label parameterLabel = new Label(parameter + ": ");

    if (parameter.startsWith(PATH_FINDING_STRATEGY)) {
      return createPathFindingStrategyNode(parameterLabel, parameter);

    } else if (parameter.startsWith(TARGET_CALCULATION_CONFIG)) {
      return createTargetCalculationConfigNode(parameterLabel, parameter);

    } else {
      return createGenericParameterNode(parameterLabel);
    }
  }


  private Node createPathFindingStrategyNode(Label parameterLabel, String parameter) {
    ComboBox<String> pathStrategyBox = new ComboBox<>();
    pathStrategyBox.getItems().addAll(ControlManager.getPathFindingStrategies());
    controlTypeComboBoxes.put(parameter, pathStrategyBox);

    VBox container = new VBox(ELEMENT_SPACING);
    container.setPadding(new Insets(ELEMENT_SPACING));
    container.getChildren().addAll(parameterLabel, pathStrategyBox);
    return container;
  }

  private Node createTargetCalculationConfigNode(Label parameterLabel, String parameter) {
    ComboBox<String> targetStrategyDropdown = new ComboBox<>();
    targetStrategyDropdown.getItems().addAll(ControlManager.getTargetCalculationStrategies());
    controlTypeComboBoxes.put(parameter, targetStrategyDropdown);

    VBox targetParameterBox = new VBox(5);
    targetStrategyDropdown.setOnAction(
        e -> updateTargetParameterFields(targetStrategyDropdown, targetParameterBox));

    VBox container = new VBox(ELEMENT_SPACING);
    container.setPadding(new Insets(ELEMENT_SPACING));
    container.getChildren().addAll(parameterLabel, targetStrategyDropdown, targetParameterBox);
    return container;
  }

  private void updateTargetParameterFields(ComboBox<String> dropdown, VBox targetParameterBox) {
    targetParameterBox.getChildren().clear();
    String selectedStrategy = dropdown.getValue();
    List<String> targetParams = ControlManager.getTargetRequiredFieldsOrder(selectedStrategy);

    if (!targetParams.isEmpty()) {
      for (String targetParam : targetParams) {
        Label targetParamLabel = new Label(targetParam + ": ");
        TextField targetParamField = new TextField();
        targetStrategyParameterFields.add(targetParamField);
        targetParameterBox.getChildren().addAll(targetParamLabel, targetParamField);
      }
    }
  }

  private Node createGenericParameterNode(Label parameterLabel) {
    TextField parameterField = new TextField();
    controlTypeParameterFields.add(parameterField);

    VBox container = new VBox(ELEMENT_SPACING);
    container.setPadding(new Insets(ELEMENT_SPACING));
    container.getChildren().addAll(parameterLabel, parameterField);
    return container;
  }

  private String getFieldValueFromConfig(ControlConfigInterface config, String fieldName) {
    try {
      var field = config.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      Object value = field.get(config);
      return value != null ? value.toString() : "";
    } catch (Exception e) {
      return "";
    }
  }

  private String getStrategyValueFromConfig(ControlConfigInterface config, String fieldName) {
    try {
      var field = config.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      Object strategy = field.get(config);
      return strategy != null ? strategy.getClass().getSimpleName().replace("ConfigRecord", "") : "";
    } catch (Exception e) {
      return "";
    }
  }

  private TargetCalculationConfigInterface buildTargetStrategyFromUI() throws ViewException {
    try {
      String selectedStrategy = controlTypeComboBoxes.get(TARGET_CALCULATION_CONFIG).getValue();
      Map<String, Class<?>> requiredTypes = ControlManager.getTargetRequiredFields(
          selectedStrategy);
      List<String> fieldOrder = ControlManager.getTargetRequiredFieldsOrder(selectedStrategy);

      List<Object> paramValues = new ArrayList<>();
      int index = 0;
      for (String param : fieldOrder) {
        String input = targetStrategyParameterFields.get(index++).getText();
        paramValues.add(castToRequiredType(input, requiredTypes.get(param)));
      }

      String fullClassName =
          "oogasalad.engine.records.config.model.controlConfig.targetStrategy." + selectedStrategy
              + "ConfigRecord";
      Class<?> strategyClass = Class.forName(fullClassName);
      Constructor<?> constructor = strategyClass.getDeclaredConstructors()[0];

      return (TargetCalculationConfigInterface) constructor.newInstance(paramValues.toArray());
    } catch (Exception e) {
      showError("Error building target strategy: " + e.getMessage());
      throw new ViewException("Error building target strategy: ", e);
    }
  }


  private Object castToRequiredType(String value, Class<?> type) {
    if (type == int.class || type == Integer.class) {
      return Integer.parseInt(value);
    } else if (type == double.class || type == Double.class) {
      return Double.parseDouble(value);
    } else if (type == String.class) {
      return value;
    }

    throw new ViewException("Unsupported parameter type: " + type);
  }


  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.showAndWait();
  }

}
