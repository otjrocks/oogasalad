package oogasalad.authoring.view;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.engine.model.CollisionRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import util.DukeApplicationTest;

import java.util.List;
import java.util.Map;

public class CollisionRuleEditorViewTest extends DukeApplicationTest {

  private CollisionRuleEditorView view;
  private AuthoringController mockController;
  private AuthoringModel mockModel;

  @Override
  public void start(Stage stage) {
    mockController = mock(AuthoringController.class);
    mockModel = mock(AuthoringModel.class);

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
  public void addRule_ValidInputs_AddsToList() {
    // Get all ComboBoxes in the order they appear in the dialog
    ComboBox<String> entityABox = lookup(".combo-box").nth(0).queryComboBox();
    ComboBox<String> modeABox = lookup(".combo-box").nth(1).queryComboBox();
    ComboBox<String> entityBBox = lookup(".combo-box").nth(2).queryComboBox();
    ComboBox<String> modeBBox = lookup(".combo-box").nth(3).queryComboBox();

    runAsJFXAction(() -> {
      entityABox.getSelectionModel().select("Pacman");
      modeABox.getSelectionModel().select("Default");
      entityBBox.getSelectionModel().select("Ghost");
      modeBBox.getSelectionModel().select("Default");
    });

    // Select actions from both lists
    ListView<String> actionAList = from(lookup(".label").lookup(
            (Predicate<Node>) l -> ((Label) l).getText().equals("Actions for Entity A:"))
        .query().getParent().lookup(".list-view")).queryListView();

    ListView<String> actionBList = from(lookup(".label").lookup(
            (Predicate<Node>) l -> ((Label) l).getText().equals("Actions for Entity B:"))
        .query().getParent().lookup(".list-view")).queryListView();


    interact(() -> {
      actionAList.getSelectionModel().select("DIE");
      actionBList.getSelectionModel().select("REMOVE");
    });

    clickOn("Add Rule");

    Label rulesLabel = lookup(".label")
        .match(l -> ((Label) l).getText().equals("Defined Rules:"))
        .query();

    VBox container = (VBox) rulesLabel.getParent();
    int index = container.getChildren().indexOf(rulesLabel);
    ScrollPane scroll = (ScrollPane) container.getChildren().get(index + 1);
    ListView<CollisionRule> ruleList = (ListView<CollisionRule>) scroll.getContent();

    assertEquals(1, ruleList.getItems().size());
    CollisionRule rule = ruleList.getItems().get(0);
    assertEquals("Pacman", rule.getEntityTypeA());
    assertEquals("Ghost", rule.getEntityTypeB());
    assertTrue(rule.getEventsA().contains("DIE"));
    assertTrue(rule.getEventsB().contains("REMOVE"));
  }

  @Test
  public void addRule_MissingInputs_ShowsError() {
    clickOn("Add Rule");
    DialogPane errorDialog = lookup(".dialog-pane").query();
    assertTrue(errorDialog.getContentText().contains("Please fill out"));
    clickOn("OK");
  }

  @Test
  public void deleteRule_RemovesSelectedRule() {
    addRule_ValidInputs_AddsToList(); // Add one rule first

    Label rulesLabel = lookup(".label")
        .match(l -> ((Label) l).getText().equals("Defined Rules:"))
        .query();

    VBox container = (VBox) rulesLabel.getParent();
    int index = container.getChildren().indexOf(rulesLabel);
    ScrollPane scroll = (ScrollPane) container.getChildren().get(index + 1);
    ListView<CollisionRule> ruleList = (ListView<CollisionRule>) scroll.getContent();

    interact(() -> ruleList.getSelectionModel().select(0));
    clickOn("Delete Selected Rule");

    assertEquals(0, ruleList.getItems().size());
  }
}
