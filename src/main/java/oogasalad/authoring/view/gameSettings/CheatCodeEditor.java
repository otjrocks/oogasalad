package oogasalad.authoring.view.gameSettings;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import oogasalad.player.model.enums.CheatType;

/**
 * CheatCodeEditor provides a set of checkboxes allowing users to select
 * available cheat codes to enable in the game.
 *
 * Cheat codes are dynamically loaded from the CheatType enum.
 *
 * Designed as a modular component of GameSettingsView.
 *
 * @author
 * William He
 */
public class CheatCodeEditor {

  private final VBox root;
  private final Map<CheatType, CheckBox> cheatCheckboxes;

  /**
   * Constructs a CheatCodeEditor and dynamically generates checkboxes
   * for each available CheatType.
   */
  public CheatCodeEditor() {
    root = new VBox(10);
    root.setPadding(new Insets(10));

    Label titleLabel = new Label("Available Cheat Codes");
    root.getChildren().add(titleLabel);

    cheatCheckboxes = new HashMap<>();

    for (CheatType cheat : CheatType.values()) {
      CheckBox checkBox = new CheckBox(cheat.name());
      cheatCheckboxes.put(cheat, checkBox);
      root.getChildren().add(checkBox);
    }
  }

  /**
   * Returns the root Node containing the cheat code checkboxes.
   *
   * @return the Node representing the editor layout
   */
  public Node getNode() {
    return root;
  }

  /**
   * Retrieves the set of cheat codes that are currently selected.
   *
   * @return a Set of selected CheatType values
   */
  public Set<CheatType> getSelectedCheats() {
    return cheatCheckboxes.entrySet().stream()
        .filter(entry -> entry.getValue().isSelected())
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }

  /**
   * Updates the selected cheat codes based on a provided set.
   *
   * @param selectedCheats the set of CheatType values to mark as selected
   */
  public void update(Set<CheatType> selectedCheats) {
    cheatCheckboxes.forEach((cheat, checkbox) -> {
      checkbox.setSelected(selectedCheats.contains(cheat));
    });
  }
}
