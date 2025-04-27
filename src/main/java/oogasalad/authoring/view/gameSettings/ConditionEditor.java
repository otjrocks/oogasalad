package oogasalad.authoring.view.gameSettings;

import java.lang.reflect.Constructor;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import oogasalad.authoring.view.ViewException;
import oogasalad.engine.records.config.model.losecondition.LoseConditionInterface;
import oogasalad.engine.records.config.model.wincondition.WinConditionInterface;
import oogasalad.engine.utility.LanguageManager;

import java.util.Map;

/**
 * ConditionEditor provides dropdowns and input fields for configuring win conditions and lose
 * conditions dynamically using reflection.
 * <p>
 * It loads available condition classes at runtime and allows the user to select types and provide
 * associated parameter values.
 * <p>
 * Designed to be used as part of the GameSettingsView.
 *
 * @author William He
 */
public class ConditionEditor {

  private final ComboBox<String> winConditionDropdown;
  private final ComboBox<String> loseConditionDropdown;
  private final TextField winValueField;
  private final TextField loseValueField;
  private final GridPane root;

  private final Map<String, Class<?>> winConditions;
  private final Map<String, Class<?>> loseConditions;

  /**
   * Constructs a ConditionEditor given mappings of available win and lose conditions.
   *
   * @param winConditions  map of win condition names to their Class types
   * @param loseConditions map of lose condition names to their Class types
   */
  public ConditionEditor(Map<String, Class<?>> winConditions,
      Map<String, Class<?>> loseConditions) {
    this.winConditions = winConditions;
    this.loseConditions = loseConditions;

    root = new GridPane();
    root.setHgap(10);
    root.setVgap(10);
    root.setPadding(new Insets(10));

    winConditionDropdown = new ComboBox<>(
        FXCollections.observableArrayList(winConditions.keySet()));
    loseConditionDropdown = new ComboBox<>(
        FXCollections.observableArrayList(loseConditions.keySet()));

    winValueField = new TextField();
    loseValueField = new TextField();

    root.add(new Label(LanguageManager.getMessage("WIN_CONDITION_TYPE")), 0, 0);
    root.add(winConditionDropdown, 1, 0);
    root.add(new Label(LanguageManager.getMessage("WIN_CONDITION_VALUE")), 2, 0);
    root.add(winValueField, 3, 0);

    root.add(new Label(LanguageManager.getMessage("LOSE_CONDITION_TYPE")), 0, 1);
    root.add(loseConditionDropdown, 1, 1);
    root.add(new Label(LanguageManager.getMessage("LOSE_CONDITION_VALUE")), 2, 1);
    root.add(loseValueField, 3, 1);
  }

  /**
   * Returns the root Node containing the condition selection UI.
   *
   * @return the Node representing the editor layout
   */
  public Node getNode() {
    return root;
  }

  /**
   * Returns the currently selected win condition type.
   *
   * @return the win condition type as a String
   */
  public String getSelectedWinCondition() {
    return winConditionDropdown.getValue();
  }

  /**
   * Returns the value entered for the selected win condition.
   *
   * @return the win condition value as a String
   */
  public String getWinValue() {
    return winValueField.getText();
  }

  /**
   * Returns the currently selected lose condition type.
   *
   * @return the lose condition type as a String
   */
  public String getSelectedLoseCondition() {
    return loseConditionDropdown.getValue();
  }

  /**
   * Updates the selected condition types and associated values.
   *
   * @param conditionState Condition state with all values
   */
  public void update(ConditionState conditionState) throws ViewException {
    winConditionDropdown.setValue(conditionState.winType());
    winValueField.setText(conditionState.winValue());
    loseConditionDropdown.setValue(conditionState.loseType());
    loseValueField.setText(conditionState.loseValue());
  }

  /**
   * Creates a WinConditionInterface instance based on the user's selection and value input.
   *
   * @return the constructed WinConditionInterface
   * @throws ViewException if reflection instantiation fails
   */
  public WinConditionInterface createSelectedWinCondition() {
    String selectedType = getSelectedWinCondition();
    String value = getWinValue();

    Class<?> clazz = winConditions.get(selectedType);
    if (clazz == null) {
      return null; // fallback
    }

    try {
      Constructor<?> intConstructor = clazz.getConstructor(int.class);
      int intValue = Integer.parseInt(value);
      return (WinConditionInterface) intConstructor.newInstance(intValue);
    } catch (NoSuchMethodException e) {
      // Maybe it's a String constructor instead
      try {
        Constructor<?> stringConstructor = clazz.getConstructor(String.class);
        return (WinConditionInterface) stringConstructor.newInstance(value);
      } catch (Exception ex) {
        throw new ViewException(ex.getMessage());
      }
    } catch (Exception e) {
      throw new ViewException(e.getMessage());
    }
  }

  /**
   * Creates a LoseConditionInterface instance based on the user's selection.
   *
   * @return the constructed LoseConditionInterface, or null if creation fails
   */
  public LoseConditionInterface createSelectedLoseCondition() {
    String selectedType = getSelectedLoseCondition();

    Class<?> clazz = loseConditions.get(selectedType);
    if (clazz == null) {
      return null; // fallback
    }

    try {
      return (LoseConditionInterface) clazz.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new ViewException(e.getMessage());
    }
  }
}
