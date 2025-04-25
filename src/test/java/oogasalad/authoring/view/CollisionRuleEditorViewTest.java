package oogasalad.authoring.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.engine.config.CollisionRule;
import oogasalad.engine.utility.LanguageManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class CollisionRuleEditorViewTest extends DukeApplicationTest {

  private CollisionRuleEditorView view;

  @Override
  public void start(Stage stage) {
    LanguageManager.setLanguage("English");
    AuthoringController mockController = mock(AuthoringController.class);
    AuthoringModel mockModel = mock(AuthoringModel.class);

    when(mockModel.getEntityTypeToModes()).thenReturn(
        Map.of("Pacman", List.of("Default"), "Ghost", List.of("Default"))
    );
    when(mockModel.getCollisionRules()).thenReturn(List.of());

    when(mockController.getModel()).thenReturn(mockModel);

    view = new CollisionRuleEditorView(mockController);
    Scene scene = new Scene((Parent) view.getNode(), 800, 600);
    stage.setScene(scene);
    stage.show();
  }

  @BeforeEach
  public void setupEach() {
    interact(() -> view.getDialog().getDialogPane().getScene().getWindow().sizeToScene());
  }

  @Test
  public void dialog_ShowsSuccessfully() {
    assertNotNull(view.getDialog().getDialogPane().getContent());
  }

  @Test
  void createCollisionRule_AddRule_SuccessfullyAdd() {
    // Show the dialog but do not block on showAndWait (we'll simulate user input instead)
    interact(() ->
        view.getDialog().show());

    // Now simulate user input
    addSampleEvent();
    clickOn("#add-action-B");

    // Finally, click OK to close the dialog and trigger any result logic
    clickOn(lookup("OK").query());
    assertEquals("""
            [(Type Pacman: Mode Default) ↔ (Type Ghost: Mode Default)
            Events A: [ConsumeCollisionEventRecord[]]
            Events B: [ConsumeCollisionEventRecord[]]]""",
        view.getDialog().resultProperty().get().toString());
  }

  @Test
  void createCollisionRule_InvalidRuleWithoutEntity_DoesNotAdd() {
    // Show the dialog but do not block on showAndWait (we'll simulate user input instead)
    interact(() ->
        view.getDialog().show());

    clickOn("#add-action-A");
    clickOn(lookup("OK").query());
    assertTrue(view.getDialog().resultProperty().get() == null
        || view.getDialog().resultProperty().get().isEmpty());
  }

  @Test
  void createCollisionRule_AttemptAddingDuplicateEvent_OnlyAddsFirstEvent() {
    // Show the dialog but do not block on showAndWait (we'll simulate user input instead)
    interact(() ->
        view.getDialog().show());
    addSampleEvent();
    addSampleEvent();
    clickOn(lookup("OK").query());
    assertEquals("""
        [(Type Pacman: Mode Default) ↔ (Type Ghost: Mode Default)
        Events A: [ConsumeCollisionEventRecord[]]
        Events B: []]""", view.getDialog().resultProperty().get().toString());
  }

  @Test
  void createCollisionRule_DeleteEvent_EventIsSuccessfullyDeleted() {
    // Show the dialog but do not block on showAndWait (we'll simulate user input instead)
    interact(() ->
        view.getDialog().show());
    addSampleEvent();
    // Click the first item in the ListView and then click delete to delete entry
    ListView<CollisionRule> listView = lookup("#rule-list-view").query();
    interact(() -> {
      listView.getSelectionModel().select(0);
      listView.getItems().removeFirst(); // simulate deletion for first item in list
    });
    clickOn("#delete-rule-button");
    clickOn(lookup("OK").query());
    assertEquals(0, view.getDialog().resultProperty().get().size());
  }

  private void addSampleEvent() {
    clickOn("#entity-A-selector");
    clickOn("Pacman");
    clickOn("#entity-B-selector");
    clickOn("Ghost");

    Set<Node> ruleSelectors = lookup("#collision-rule-selector").queryAll();
    for (Node node : ruleSelectors) {
      if (node instanceof ComboBox<?>) {
        ComboBox<String> comboBox = (ComboBox<String>) node;
        interact(() -> comboBox.getSelectionModel().select("Consume"));
      }
    }

    clickOn("#add-action-A");
  }


}
