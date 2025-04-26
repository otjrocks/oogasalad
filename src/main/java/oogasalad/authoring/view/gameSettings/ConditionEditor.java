package oogasalad.authoring.view.gameSettings;

import java.lang.reflect.Constructor;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import oogasalad.engine.records.config.model.losecondition.LoseConditionInterface;
import oogasalad.engine.records.config.model.wincondition.WinConditionInterface;
import oogasalad.engine.utility.LanguageManager;

import java.util.Map;

public class ConditionEditor {

  private final ComboBox<String> winConditionDropdown;
  private final ComboBox<String> loseConditionDropdown;
  private final TextField winValueField;
  private final TextField loseValueField;
  private final GridPane root;

  private final Map<String, Class<?>> winConditions;
  private final Map<String, Class<?>> loseConditions;

  public ConditionEditor(Map<String, Class<?>> winConditions, Map<String, Class<?>> loseConditions) {
    this.winConditions = winConditions;
    this.loseConditions = loseConditions;

    root = new GridPane();
    root.setHgap(10);
    root.setVgap(10);
    root.setPadding(new Insets(10));

    winConditionDropdown = new ComboBox<>(FXCollections.observableArrayList(winConditions.keySet()));
    loseConditionDropdown = new ComboBox<>(FXCollections.observableArrayList(loseConditions.keySet()));

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

  public Node getNode() {
    return root;
  }

  public String getSelectedWinCondition() {
    return winConditionDropdown.getValue();
  }

  public String getWinValue() {
    return winValueField.getText();
  }

  public String getSelectedLoseCondition() {
    return loseConditionDropdown.getValue();
  }

  public String getLoseValue() {
    return loseValueField.getText();
  }

  public void update(String winType, String winValue, String loseType, String loseValue) {
    winConditionDropdown.setValue(winType);
    winValueField.setText(winValue);
    loseConditionDropdown.setValue(loseType);
    loseValueField.setText(loseValue);
  }

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
      // maybe it's a String constructor instead
      try {
        Constructor<?> stringConstructor = clazz.getConstructor(String.class);
        return (WinConditionInterface) stringConstructor.newInstance(value);
      } catch (Exception ex) {
        ex.printStackTrace();
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Creates the selected LoseCondition instance using reflection.
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
      e.printStackTrace();
      return null;
    }
  }

}
