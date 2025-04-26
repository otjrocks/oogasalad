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

public class CheatCodeEditor {

  private final VBox root;
  private final Map<CheatType, CheckBox> cheatCheckboxes;

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

  public Node getNode() {
    return root;
  }

  public Set<CheatType> getSelectedCheats() {
    return cheatCheckboxes.entrySet().stream()
        .filter(entry -> entry.getValue().isSelected())
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
  }

  public void update(Set<CheatType> selectedCheats) {
    cheatCheckboxes.forEach((cheat, checkbox) -> {
      checkbox.setSelected(selectedCheats.contains(cheat));
    });
  }

}
