package oogasalad.authoring.view;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import oogasalad.authoring.controller.LevelController;
import oogasalad.authoring.model.LevelDraft;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import java.util.List;

public class LevelSelectorViewTest extends DukeApplicationTest {

  private LevelController mockController;
  private LevelSelectorView view;

  public void start(Stage stage) {
    mockController = mock(LevelController.class);
    view = new LevelSelectorView(mockController);
    Scene scene = new Scene(view.getRoot());
    stage.setScene(scene);
    stage.show();
  }

  @Test
  public void constructor_InitializesComponents() {
    HBox root = view.getRoot();
    assertEquals(2, root.getChildren().size(), "Should have 2 children: dropdown and button");
    assertInstanceOf(ComboBox.class, root.getChildren().get(0), "First child should be a ComboBox");
    assertInstanceOf(Button.class, root.getChildren().get(1), "Second child should be a Button");
  }

  @Test
  public void updateLevels_ValidList_UpdatesDropdown() {
    LevelDraft l1 = new LevelDraft("Level 1", "");
    LevelDraft l2 = new LevelDraft("Level 2", "");

    runAsJFXAction(() -> view.updateLevels(List.of(l1, l2)));

    ComboBox<String> dropdown = (ComboBox<String>) view.getRoot().getChildren().get(0);
    assertEquals(2, dropdown.getItems().size());
    assertEquals("Level 1", dropdown.getItems().get(0));
    assertEquals("Level 2", dropdown.getItems().get(1));
  }

  @Test
  public void highlightLevel_ValidIndex_SelectsCorrectLevel() {
    LevelDraft l1 = new LevelDraft("Level 1", "");
    LevelDraft l2 = new LevelDraft("Level 2", "");

    runAsJFXAction(() -> view.updateLevels(List.of(l1, l2)));

    runAsJFXAction(() -> view.highlightLevel(1));
    ComboBox<String> dropdown = (ComboBox<String>) view.getRoot().getChildren().get(0);
    assertEquals("Level 2", dropdown.getSelectionModel().getSelectedItem());
  }

  @Test
  public void addLevelButton_Click_InvokesController() {
    doNothing().when(mockController).addNewLevel();

    Button addButton = (Button) view.getRoot().getChildren().get(1);
    clickOn(addButton);

    verify(mockController, times(1)).addNewLevel();
  }



  @Test
  public void levelDropdown_ChangeSelection_CallsSwitchToLevel() {
    LevelDraft l1 = new LevelDraft("Level 1", "");
    LevelDraft l2 = new LevelDraft("Level 2", "");

    runAsJFXAction(() -> view.updateLevels(List.of(l1, l2)));

    ComboBox<String> dropdown = (ComboBox<String>) view.getRoot().getChildren().get(0);
    runAsJFXAction(() -> dropdown.getSelectionModel().select(1));
    verify(mockController).switchToLevel(1);
  }
}
