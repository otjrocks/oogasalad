package oogasalad.authoring.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.model.EntityType;

import java.util.Map;
import oogasalad.engine.config.ModeConfig;
import oogasalad.engine.model.controlConfig.ConditionalControlConfig;
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.controlConfig.KeyboardControlConfig;
import oogasalad.engine.model.controlConfig.NoneControlConfig;
import oogasalad.engine.model.controlConfig.TargetControlConfig;
import oogasalad.engine.model.controlConfig.targetStrategy.TargetAheadOfEntityConfig;
import oogasalad.engine.model.controlConfig.targetStrategy.TargetCalculationConfig;
import oogasalad.engine.model.controlConfig.targetStrategy.TargetEntityConfig;
import oogasalad.engine.model.controlConfig.targetStrategy.TargetEntityWithTrapConfig;
import oogasalad.engine.model.controlConfig.targetStrategy.TargetLocationConfig;
import oogasalad.player.model.control.ControlManager;

/**
 * View for editing a selected EntityType.
 *
 * @author Will He, Ishan Madan
 */
public class EntityTypeEditorView {

  private final VBox root;
  private final TextField typeField;
  private final ComboBox<String> controlTypeBox;
  private final VBox modeList;
  private final AuthoringController controller;
  private EntityType current;

  private final Button saveCollisionButton;
  private final VBox controlTypeParameters;
  private final List<TextField> controlTypeParameterFields;
  private final List<TextField> targetStrategyParameterFields;
  private final Map<String, ComboBox<String>> controlTypeComboBoxes = new HashMap<>();



  /**
   * Edit parameters for an entityType
   *
   * @param controller Wires with model
   */
  public EntityTypeEditorView(AuthoringController controller) {
    this.controller = controller;

    root = new VBox();
    root.setSpacing(10);
    root.setPadding(new Insets(10));
    root.getStyleClass().add("entity-editor-view");

    typeField = new TextField();
    controlTypeBox = new ComboBox<>();
    controlTypeBox.getItems().addAll(ControlManager.getControlStrategies());
    controlTypeBox.setOnAction(e -> updateControlParameterFields());

    controlTypeParameters = new VBox(5);
    controlTypeParameterFields = new ArrayList<>();
    targetStrategyParameterFields = new ArrayList<>();


    saveCollisionButton = new Button(LanguageManager.getMessage("SAVE_SETTINGS"));
    saveCollisionButton.setOnAction(e -> commitChanges());

    modeList = new VBox(5);

    Button addModeButton = new Button(LanguageManager.getMessage("ADD_MODE"));
    addModeButton.setOnAction(e -> openAddModeDialog());

    root.getChildren().addAll(
        new Label(LanguageManager.getMessage("ENTITY_TYPE")), typeField,
        new Label(LanguageManager.getMessage("CONTROL_STRATEGY")), controlTypeBox,
        controlTypeParameters, saveCollisionButton,
        new Label(LanguageManager.getMessage("MODES")), modeList,
        addModeButton
    );
  }

  /**
   * Set's entity type
   *
   * @param type of type EntityType
   */
  public void setEntityType(EntityType type) {
    this.current = type;
    if (type == null) {
      return;
    }

    typeField.setText(type.type());
    typeField.setOnAction(e -> commitChanges());
    typeField.focusedProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal) {
        commitChanges(); // when field loses focus
      }
    });

    // TODO: New config
//    controlTypeBox.setValue();
//    controlTypeBox.setOnAction(e -> commitChanges());

    modeList.getChildren().clear();
    for (Map.Entry<String, ModeConfig> entry : type.modes().entrySet()) {
      String modeName = entry.getKey();
      ModeConfig config = entry.getValue();
      Label label = new Label(modeName);
      Button editButton = new Button(LanguageManager.getMessage("EDIT"));
      editButton.setOnAction(e -> openEditModeDialog(modeName, config));
      modeList.getChildren().addAll(label, editButton);
    }
  }

  /**
   * Returns the root JavaFX node of this view
   *
   * @return the root node
   */
  public Parent getRoot() {
    return root;
  }

  private void commitChanges() {
    if (current == null) {
      return;
    }

    ControlConfig controlConfig = buildControlConfigFromUI(); // dynamically builds appropriate config

    EntityType newEntity = new EntityType(
        typeField.getText(),
        controlConfig,
        current.modes(),
        current.blocks()
    );

    controller.getModel().updateEntityType(current.type(), newEntity);
    controller.updateEntitySelector(); // update visuals if needed
    current = newEntity;
  }

  private ControlConfig buildControlConfigFromUI() {
    String controlType = controlTypeBox.getValue();

    Map<String, Class<?>> requiredFields = ControlManager.getControlRequiredFields(controlType);

    try {
      if (controlType.equals("None")) {
        return new NoneControlConfig();
      } else if (controlType.equals("Keyboard")) {
        return new KeyboardControlConfig();
      } else if (controlType.equals("Target")) {
        // Get pathFindingStrategy from first field
        String pathFindingStrategy = controlTypeComboBoxes.get("pathFindingStrategy").getValue();

        // Get target strategy
        ComboBox<String> targetStrategyDropdown = controlTypeComboBoxes.get("targetCalculationConfig");
        String targetStrategy = targetStrategyDropdown.getValue();
        Map<String, Class<?>> targetParams = ControlManager.getTargetRequiredFields(targetStrategy);

        List<Object> paramValues = new ArrayList<>();
        for (int i = 0; i < targetParams.size(); i++) {
          Class<?> type = new ArrayList<>(targetParams.values()).get(i);
          String value = targetStrategyParameterFields.get(i).getText();
          paramValues.add(castToRequiredType(value, type));
        }

        TargetCalculationConfig targetConfig = switch (targetStrategy) {
          case "TargetEntity" -> new TargetEntityConfig((String) paramValues.get(0));
          case "TargetAheadOfEntity" -> new TargetAheadOfEntityConfig((String) paramValues.get(0), (Integer) paramValues.get(1));
          case "TargetEntityWithTrap" -> new TargetEntityWithTrapConfig((String) paramValues.get(0), (Integer) paramValues.get(1), (String) paramValues.get(2));
          case "TargetLocation" -> new TargetLocationConfig((Double) paramValues.get(0), (Double) paramValues.get(1));
          default -> throw new IllegalArgumentException("Unknown target strategy: " + targetStrategy);
        };

        return new TargetControlConfig(pathFindingStrategy, targetConfig);

      } else if (controlType.equals("Conditional")) {
        int radius = Integer.parseInt(controlTypeParameterFields.get(0).getText());
        String pathFindingStrategyInRadius = controlTypeComboBoxes.get("pathFindingStrategyInRadius").getValue();
        String pathFindingStrategyOutRadius = controlTypeComboBoxes.get("pathFindingStrategyOutRadius").getValue();

        // Same target config logic as above
        ComboBox<String> targetStrategyDropdown = controlTypeComboBoxes.get("targetCalculationConfig");
        String targetStrategy = targetStrategyDropdown.getValue();
        Map<String, Class<?>> targetParams = ControlManager.getTargetRequiredFields(targetStrategy);

        List<Object> paramValues = new ArrayList<>();
        for (int i = 0; i < targetParams.size(); i++) {
          Class<?> type = new ArrayList<>(targetParams.values()).get(i);
          String value = targetStrategyParameterFields.get(i).getText();
          paramValues.add(castToRequiredType(value, type));
        }

        TargetCalculationConfig targetConfig = switch (targetStrategy) {
          case "TargetEntity" -> new TargetEntityConfig((String) paramValues.get(0));
          case "TargetAheadOfEntity" -> new TargetAheadOfEntityConfig((String) paramValues.get(0), (Integer) paramValues.get(1));
          case "TargetEntityWithTrap" -> new TargetEntityWithTrapConfig((String) paramValues.get(0), (Integer) paramValues.get(1), (String) paramValues.get(2));
          case "TargetLocation" -> new TargetLocationConfig((Double) paramValues.get(0), (Double) paramValues.get(1));
          default -> throw new IllegalArgumentException("Unknown target strategy: " + targetStrategy);
        };

        return new ConditionalControlConfig(radius, pathFindingStrategyInRadius, pathFindingStrategyOutRadius, targetConfig);
      }
    } catch (Exception e) {
      showError("Failed to parse control configuration: " + e.getMessage());
      e.printStackTrace();
    }

    return null;
  }

  private Object castToRequiredType(String value, Class<?> type) {
    if (type == int.class || type == Integer.class) {
      return Integer.parseInt(value);
    } else if (type == double.class || type == Double.class) {
      return Double.parseDouble(value);
    } else if (type == String.class) {
      return value;
    }
    throw new IllegalArgumentException("Unsupported parameter type: " + type);
  }




  private void openAddModeDialog() {
    ModeEditorDialog dialog = new ModeEditorDialog();
    dialog.showAndWait().ifPresent(config -> {
      String modeName = config.name();
      if (!modeName.isEmpty() && !current.modes().containsKey(modeName)) {
        current.modes().put(modeName, config);
        setEntityType(current);
      } else {
        showError(LanguageManager.getMessage("INVALID_OR_DUPLICATE_MODE"));
      }
    });
  }

  private void openEditModeDialog(String modeName, ModeConfig oldConfig) {
    ModeEditorDialog dialog = new ModeEditorDialog(oldConfig);
    dialog.showAndWait().ifPresent(newConfig -> {
      if (!modeName.equals(newConfig.name())) {
        // Mode name changed → remove old key and insert new one
        current.modes().remove(modeName);
      }
      current.modes().put(newConfig.name(), newConfig);
      setEntityType(current);
      controller.updateEntitySelector();
      controller.updateCanvas();
    });
  }

  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.showAndWait();
  }

  private void updateControlParameterFields() {
    controlTypeParameters.getChildren().clear();
    controlTypeParameterFields.clear();
    VBox parameterBox = new VBox(5);

    Map<String, Class<?>> requiredFields =
        ControlManager.getControlRequiredFields(controlTypeBox.getValue());

    for (String parameter : requiredFields.keySet()) {
      Label parameterLabel = new Label(parameter + ": ");

      if (parameter.startsWith("pathFindingStrategy")) {
        ComboBox<String> pathStrategyBox = new ComboBox<>();
        pathStrategyBox.getItems().addAll(ControlManager.getPathFindingStrategies());
        controlTypeComboBoxes.put(parameter, pathStrategyBox);
        parameterBox.getChildren().addAll(parameterLabel, pathStrategyBox);

      } else if (parameter.startsWith("targetCalculationConfig")) {
        ComboBox<String> targetStrategyDropdown = new ComboBox<>();
        controlTypeComboBoxes.put(parameter, targetStrategyDropdown);
        targetStrategyDropdown.getItems().addAll(ControlManager.getTargetCalculationStrategies());

        VBox targetParameterBox = new VBox(5);

        // When target strategy changes, update parameter fields only if parameters are required
        targetStrategyDropdown.setOnAction(e -> {
          targetParameterBox.getChildren().clear();
          String selectedStrategy = targetStrategyDropdown.getValue();
          Map<String, Class<?>> targetParams = ControlManager.getTargetRequiredFields(selectedStrategy);

          if (!targetParams.isEmpty()) {
            for (String targetParam : targetParams.keySet()) {
              Label targetParamLabel = new Label(targetParam + ": ");
              TextField targetParamField = new TextField();
              targetStrategyParameterFields.add(targetParamField);
              targetParameterBox.getChildren().addAll(targetParamLabel, targetParamField);
            }
          }
        });

        parameterBox.getChildren().addAll(parameterLabel, targetStrategyDropdown, targetParameterBox);

      } else {
        TextField parameterField = new TextField();
        controlTypeParameterFields.add(parameterField);
        parameterBox.getChildren().addAll(parameterLabel, parameterField);
      }
    }

    controlTypeParameters.getChildren().add(parameterBox);
  }



}
