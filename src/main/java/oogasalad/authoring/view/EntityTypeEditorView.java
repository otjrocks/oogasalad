package oogasalad.authoring.view;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.config.ModeConfig;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.controlConfig.targetStrategy.TargetCalculationConfig;
import oogasalad.player.model.control.ControlManager;

/**
 * View for editing a selected EntityType.
 *
 * @author Will He, Ishan Madan, Angela Predolac
 */
public class EntityTypeEditorView {

  private final String TARGET_CALCULATION_CONFIG = "targetCalculationConfig";
  private final String PATH_FINDING_STRATEGY = "pathFindingStrategy";

  private final VBox root;
  private final TextField typeField;
  private final ComboBox<String> controlTypeBox;
  private final VBox modeList;
  private final AuthoringController controller;
  private EntityType current;
  private Button deleteButton;

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
    controlTypeBox.setValue("None");
    controlTypeBox.setOnAction(e -> updateControlParameterFields());

    controlTypeParameters = new VBox(5);
    controlTypeParameterFields = new ArrayList<>();
    targetStrategyParameterFields = new ArrayList<>();

    saveCollisionButton = new Button(LanguageManager.getMessage("SAVE_SETTINGS"));
    saveCollisionButton.setOnAction(e -> commitChanges());

    modeList = new VBox(5);

    Button addModeButton = new Button(LanguageManager.getMessage("ADD_MODE"));
    addModeButton.setOnAction(e -> openAddModeDialog());

    deleteButton = new Button("Delete Entity Type");
    deleteButton.getStyleClass().add("delete-button");
    deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
    deleteButton.setOnAction(e -> deleteEntityType());

    ScrollPane controlTypeScrollPane = new ScrollPane(controlTypeParameters);
    controlTypeScrollPane.setFitToWidth(true);

    controlTypeScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    controlTypeScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    root.getChildren().addAll(
        new Label(LanguageManager.getMessage("ENTITY_TYPE")), typeField,
        new Label(LanguageManager.getMessage("CONTROL_STRATEGY")), controlTypeBox,
        controlTypeScrollPane, saveCollisionButton,
        new Label(LanguageManager.getMessage("MODES")), modeList,
        addModeButton,
        new Separator(), deleteButton

    );
  }

  /**
   * Set's entity type
   *
   * @param type of type EntityType
   */
  public void setEntityType(EntityType type) {
    this.current = type;
    if (type == null) return;

    typeField.setText(type.type());
    typeField.setOnAction(e -> commitChanges());
    typeField.focusedProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal) {
        commitChanges(); // when field loses focus
      }
    });

    populateControlConfigUI(type.controlConfig());

    // Refresh mode list UI
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

  private void populateControlConfigUI(ControlConfig config) {
    String type = config.getClass().getSimpleName().replace("ControlConfig", "");
    controlTypeBox.setValue(type);

    controlTypeParameters.getChildren().clear();
    controlTypeParameterFields.clear();
    targetStrategyParameterFields.clear();
    controlTypeComboBoxes.clear();

    Map<String, Class<?>> fieldTypes = ControlManager.getControlRequiredFields(type);
    List<String> fieldOrder = ControlManager.getControlRequiredFieldsOrder(type);

    for (String field : fieldOrder) {
      Label label = new Label(field + ":");

      if (field.startsWith(PATH_FINDING_STRATEGY)) {
        ComboBox<String> combo = new ComboBox<>();
        combo.setItems(FXCollections.observableArrayList(ControlManager.getPathFindingStrategies()));
        combo.setValue(getStrategyValueFromConfig(config, field));
        controlTypeComboBoxes.put(field, combo);
        controlTypeParameters.getChildren().add(new VBox(label, combo));

      } else if (field.startsWith(TARGET_CALCULATION_CONFIG)) {
        ComboBox<String> targetCombo = new ComboBox<>();
        targetCombo.setItems(FXCollections.observableArrayList(ControlManager.getTargetCalculationStrategies()));
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

  private String getFieldValueFromConfig(ControlConfig config, String fieldName) {
    try {
      var field = config.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      Object value = field.get(config);
      return value != null ? value.toString() : "";
    } catch (Exception e) {
      return "";
    }
  }

  private String getStrategyValueFromConfig(ControlConfig config, String fieldName) {
    try {
      var field = config.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      Object strategy = field.get(config);
      return strategy != null ? strategy.getClass().getSimpleName().replace("Config", "") : "";
    } catch (Exception e) {
      return "";
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
        current.blocks(),
        current.speed()
    );

    controller.getModel().updateEntityType(current.type(), newEntity);
    controller.updateEntitySelector(); // update visuals if needed
    current = newEntity;
  }

  private ControlConfig buildControlConfigFromUI() {
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
          "oogasalad.engine.model.controlConfig." + controlType + "ControlConfig";
      Class<?> configClass = Class.forName(fullClassName);
      Constructor<?> constructor = configClass.getDeclaredConstructors()[0];

      return (ControlConfig) constructor.newInstance(constructorArgs.toArray());

    } catch (Exception e) {
      showError("Error building control config: " + e.getMessage());
      throw new ViewException("Error building control config: ", e);
    }
  }


  private TargetCalculationConfig buildTargetStrategyFromUI() throws ViewException {
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
          "oogasalad.engine.model.controlConfig.targetStrategy." + selectedStrategy + "Config";
      Class<?> strategyClass = Class.forName(fullClassName);
      Constructor<?> constructor = strategyClass.getDeclaredConstructors()[0];

      return (TargetCalculationConfig) constructor.newInstance(paramValues.toArray());
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


  private void openAddModeDialog() {
    ModeEditorDialog dialog = new ModeEditorDialog();
    dialog.showAndWait().ifPresent(config -> {
      String modeName = config.name();

      if (modeName.isEmpty() || current.modes().containsKey(modeName)) {
        showError(LanguageManager.getMessage("INVALID_OR_DUPLICATE_MODE"));
        return;
      }

      // Create a new map with the added mode
      Map<String, ModeConfig> newModes = new HashMap<>(current.modes());
      newModes.put(modeName, config);

      // Replace current with new EntityType (workaround for record immutability)
      current = new EntityType(
          current.type(),
          current.controlConfig(),
          newModes,
          current.blocks(),
          current.speed()
      );

      setEntityType(current); // this pushes it wherever it needs to go
      controller.updateEntitySelector();
      controller.updateCanvas();
      commitChanges();
    });
  }


  private void openEditModeDialog(String oldName, ModeConfig oldConfig) {
    ModeEditorDialog dialog = new ModeEditorDialog(oldConfig);
    dialog.showAndWait().ifPresent(newConfig -> {
      Map<String, ModeConfig> newModes = new HashMap<>(current.modes());

      boolean renamed = !oldName.equals(newConfig.name());
      if (renamed) {
        newModes.remove(oldName);
      }
      newModes.put(newConfig.name(), newConfig);

      // 1. Create updated EntityType
      EntityType updated = new EntityType(
          current.type(),
          current.controlConfig(),
          newModes,
          current.blocks(),
          current.speed()
      );

      // 2. Replace in model
      controller.getModel().addEntityType(updated);
      controller.getModel().getCurrentLevel().refreshEntityTypes(controller.getModel().getEntityTypeMap());


      // 3. Update placements to reflect renamed mode
      if (renamed) {
        controller.getModel()
            .getCurrentLevel()
            .updateModeName(updated.type(), oldName, newConfig.name());
      }

      // 4. Update UI
      current = updated; // update local cache
      setEntityType(updated);
      controller.updateEntitySelector();
      controller.updateCanvas();
      commitChanges();
    });
  }


  private void showError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
    alert.showAndWait();
  }

  private void deleteEntityType() {
    if (current == null) {
      return;
    }

    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle(LanguageManager.getMessage("CONFIRM_DELETE"));
    confirm.setHeaderText(LanguageManager.getMessage("DELETE_ENTITY_TYPE"));
    confirm.setContentText(LanguageManager.getMessage("CONFIRM_DELETE_ENTITY_TYPE_MESSAGE"));

    if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
      controller.deleteEntityType(current.type());
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

    VBox container = new VBox(5);
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

    VBox container = new VBox(5);
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

    VBox container = new VBox(5);
    container.getChildren().addAll(parameterLabel, parameterField);
    return container;
  }


}
