package oogasalad.authoring.view.gameSettings;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.view.CollisionRuleEditorView;
import oogasalad.engine.config.CollisionRule;
import oogasalad.engine.records.config.model.SettingsRecord;
import oogasalad.engine.records.config.model.losecondition.LoseConditionInterface;
import oogasalad.engine.records.config.model.wincondition.WinConditionInterface;
import oogasalad.engine.utility.LanguageManager;
import oogasalad.engine.utility.ThemeManager;

import java.util.HashSet;
import java.util.List;

public class GameSettingsView {

  private final AuthoringController controller;

  private final HBox rootNode;
  private final VBox contentBox;
  private final MetadataEditor metadataEditor;
  private final SettingsEditor settingsEditor;
  private final ConditionEditor conditionEditor;
  private final CheatCodeEditor cheatCodeEditor;

  public GameSettingsView(AuthoringController controller) {
    this.controller = controller;
    this.rootNode = new HBox();
    this.rootNode.setSpacing(15);
    this.rootNode.setPadding(new Insets(15));
    this.rootNode.setAlignment(Pos.CENTER_LEFT);

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.getStyleClass().add("game-settings-scroll-pane");

    contentBox = new VBox(10);
    contentBox.setPadding(new Insets(5));

    // Title Label
    Label titleLabel = new Label(LanguageManager.getMessage("GAME_SETTINGS"));
    titleLabel.getStyleClass().add("settings-title");

    // Metadata section
    metadataEditor = new MetadataEditor(
        controller.getModel().getGameTitle(),
        controller.getModel().getAuthor(),
        controller.getModel().getGameDescription()
    );

    // Settings section
    settingsEditor = new SettingsEditor(
        controller.getModel().getDefaultSettings().gameSpeed(),
        controller.getModel().getDefaultSettings().startingLives(),
        controller.getModel().getDefaultSettings().initialScore()
    );

    // Win/Loss condition section
    conditionEditor = new ConditionEditor(
        ConditionLoader.loadConditionClasses(
            "oogasalad.engine.records.config.model.wincondition", WinConditionInterface.class
        ),
        ConditionLoader.loadConditionClasses(
            "oogasalad.engine.records.config.model.losecondition", LoseConditionInterface.class
        )
    );

    // Cheat Codes section
    cheatCodeEditor = new CheatCodeEditor();

    // Buttons (Save Settings, Collision Rules)
    HBox buttonBox = createButtonBox();

    // Settings Grid
    GridPane settingsGrid = new GridPane();
    settingsGrid.getStyleClass().add("settings-grid");

    // Arrange fields inside the settingsGrid like before
    settingsGrid.add(metadataEditor.getNode(), 0, 0, 4, 1);
    settingsGrid.add(settingsEditor.getNode(), 0, 1, 4, 1);
    settingsGrid.add(conditionEditor.getNode(), 0, 2, 4, 1);
    settingsGrid.add(cheatCodeEditor.getNode(), 0, 3, 4, 1);

    // Assemble content
    contentBox.getChildren().addAll(
        titleLabel,
        settingsGrid,
        buttonBox
    );

    scrollPane.setContent(contentBox);
    rootNode.getChildren().add(scrollPane);
  }

  public Node getNode() {
    return rootNode;
  }

  public void updateFromModel() {
    metadataEditor.update(
        controller.getModel().getGameTitle(),
        controller.getModel().getAuthor(),
        controller.getModel().getGameDescription()
    );
    settingsEditor.update(
        controller.getModel().getDefaultSettings().gameSpeed(),
        controller.getModel().getDefaultSettings().startingLives(),
        controller.getModel().getDefaultSettings().initialScore()
    );
    conditionEditor.update(
        controller.getModel().getDefaultSettings().winCondition().getConditionType(),
        controller.getModel().getDefaultSettings().winCondition().getConditionValue().orElse(""),
        controller.getModel().getDefaultSettings().loseCondition().getConditionType(),
        String.valueOf(controller.getModel().getDefaultSettings().startingLives())
    );
    cheatCodeEditor.update(controller.getModel().getDefaultSettings().cheatTypes());
  }

  public void saveSettings() {
    controller.getModel().setGameTitle(metadataEditor.getTitle());
    controller.getModel().setAuthor(metadataEditor.getAuthor());
    controller.getModel().setGameDescription(metadataEditor.getDescription());

    SettingsRecord updatedSettings = new SettingsRecord(
        settingsEditor.getGameSpeed(),
        settingsEditor.getStartingLives(),
        settingsEditor.getInitialScore(),
        createWinCondition(),
        createLoseCondition(),
        cheatCodeEditor.getSelectedCheats()
    );
    controller.getModel().setDefaultSettings(updatedSettings);
  }

  public void showCollisionRulesPopup() {
    CollisionRuleEditorView collisionEditor = new CollisionRuleEditorView(controller);
    Dialog<List<CollisionRule>> dialog = collisionEditor.getDialog();
    ThemeManager.getInstance().registerScene(dialog.getDialogPane().getScene());

    collisionEditor.showAndWait().ifPresent(updatedRules ->
        controller.getModel().setCollisionRules(updatedRules)
    );
  }

  private HBox createButtonBox() {
    Button saveButton = new Button(LanguageManager.getMessage("SAVE_SETTINGS"));
    saveButton.setOnAction(e -> saveSettings());

    Button collisionRulesButton = new Button(LanguageManager.getMessage("COLLISION_RULES"));
    collisionRulesButton.setOnAction(e -> showCollisionRulesPopup());

    HBox buttonBox = new HBox(15, saveButton, collisionRulesButton);
    buttonBox.setAlignment(Pos.CENTER_LEFT);
    return buttonBox;
  }

  private WinConditionInterface createWinCondition() {
    return conditionEditor.createSelectedWinCondition();
  }

  private LoseConditionInterface createLoseCondition() {
    return conditionEditor.createSelectedLoseCondition();
  }
}
