package oogasalad.engine.view.components;

import static oogasalad.engine.utility.constants.GameConfig.ELEMENT_SPACING;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import oogasalad.engine.utility.LoggingManager;

/**
 * A class to create a vertical menu of options.
 *
 * @author Owen Jennings
 */
public class VMenu {

  private final VBox myRoot;

  /**
   * Create a vertical menu with the values provided. The actions[i] corresponds with the menu value
   * values[i] (Parallel arrays).
   *
   * @param values  The display name of the menu options
   * @param actions The actions to execute when a given value is selected from the menu.
   */
  public VMenu(List<String> values, List<EventHandler<ActionEvent>> actions) {
    myRoot = new VBox();
    validateParameters(values, actions);
    createMenu(values, actions);
    myRoot.setSpacing(ELEMENT_SPACING);
    myRoot.getStyleClass().add("v-menu");
  }

  /**
   * Get the root element of this menu.
   *
   * @return A VBox JavaFX element containing all the elements of this menu.
   */
  public VBox getRoot() {
    return myRoot;
  }

  private static void validateParameters(List<String> values,
      List<EventHandler<ActionEvent>> actions) {
    if (actions.size() != values.size()) {
      LoggingManager.LOGGER.warn("Unable to create a VMenu with {} actions and {} values.",
          actions.size(), values.size());
      throw new IllegalArgumentException("Actions size does not match values size");
    }
  }

  private void createMenu(List<String> values, List<EventHandler<ActionEvent>> actions) {
    for (int i = 0; i < values.size(); i++) {
      Button newButton = new Button(values.get(i));
      newButton.setOnAction(actions.get(i));
      newButton.setId("v-menu-button-" + i);
      myRoot.getChildren().add(newButton);
    }
  }
}
