package oogasalad.authoring.view;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javax.swing.text.View;
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
 * @author Will He, Ishan Madan, Angela Predolac
 */
public class EntityTypeEditorView {

  private final String TARGET_CALCULATION_CONFIG = "targetCalculationConfig";

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
    Map<String, Class<?>> requiredFieldTypes = ControlManager.getControlRequiredFields(controlType);
    List<String> requiredFieldOrder = ControlManager.getControlRequiredFieldsOrder(controlType);

    try {
      List<Object> constructorArgs = new ArrayList<>();
      int textFieldIndex = 0;

      for (String param : requiredFieldOrder) {
        Class<?> type = requiredFieldTypes.get(param);

        if (param.startsWith("pathFindingStrategy")) {
          constructorArgs.add(controlTypeComboBoxes.get(param).getValue());

        } else if (param.startsWith(TARGET_CALCULATION_CONFIG)) {
          constructorArgs.add(buildTargetStrategyFromUI());

        } else {
          String input = controlTypeParameterFields.get(textFieldIndex++).getText();
          constructorArgs.add(castToRequiredType(input, type));
        }
      }

      String fullClassName = "oogasalad.engine.model.controlConfig." + controlType + "ControlConfig";
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

  private void deleteEntityType() {
    if (current == null) return;

    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle(LanguageManager.getMessage("CONFIRM_DELETE"));
    confirm.setHeaderText(LanguageManager.getMessage("DELETE_ENTITY_TYPE"));
    confirm.setContentText(LanguageManager.getMessage("CONFIRM_DELETE_ENTITY_TYPE_MESSAGE"));

    if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
      controller.deleteEntityType(current.type());
    }
  }

}
