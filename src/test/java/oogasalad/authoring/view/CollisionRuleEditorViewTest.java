package oogasalad.authoring.view;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.function.Predicate;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.model.CollisionRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import java.util.List;
import java.util.Map;

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
}
