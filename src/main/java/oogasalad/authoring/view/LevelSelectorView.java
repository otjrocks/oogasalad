package oogasalad.authoring.view;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import oogasalad.authoring.controller.LevelController;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.utility.LanguageManager;

/**
 * A view component that displays a dropdown for selecting levels, and includes a "+ Add Level"
 * button to create new levels. Notifies the controller when a level is selected or added.
 * <p>
 * Should be placed in the top toolbar or header of the authoring UI.
 * <p>
 * Example: Used by AuthoringView to allow switching between levels.
 *
 * @author Will He, Ishan Madan
 */
public class LevelSelectorView {

  private final HBox root;
  private final ComboBox<String> levelDropdown = new ComboBox<>();

  /**
   * Constructs the LevelSelectorView and wires up interactions.
   *
   * @param controller the controller responsible for managing level logic
   */
  public LevelSelectorView(LevelController controller) {

    root = new HBox();
    levelDropdown.setPromptText(LanguageManager.getMessage("SELECT_LEVEL"));

    levelDropdown.setPrefWidth(130);
    Button addLevelButton = new Button(LanguageManager.getMessage("ADD_LEVEL_LABEL"));
    addLevelButton.setPrefWidth(130);
    root.setPrefWidth(280);

    levelDropdown.setOnAction(e -> {
      int selectedIndex = levelDropdown.getSelectionModel().getSelectedIndex();
      if (selectedIndex >= 0) {
        controller.switchToLevel(selectedIndex);
      }
    });

    addLevelButton.setOnAction(e -> controller.addNewLevel());

    root.setSpacing(10);
    root.getChildren().addAll(levelDropdown, addLevelButton);
  }

  /**
   * Returns the root JavaFX node of this view
   *
   * @return the root node
   */
  public HBox getRoot() {
    return root;
  }

  /**
   * Updates the dropdown list of levels based on the current list of LevelDrafts.
   *
   * @param levels the list of levels to display
   */
  public void updateLevels(List<LevelDraft> levels) {
    levelDropdown.getItems().clear();
    for (LevelDraft level : levels) {
      levelDropdown.getItems().add(level.getName());
    }
  }

  /**
   * Highlights the selected level in the dropdown.
   *
   * @param index the index to select
   */
  public void highlightLevel(int index) {
    levelDropdown.getSelectionModel().select(index);
  }

}
