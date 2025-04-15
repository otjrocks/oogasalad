package oogasalad.authoring.view;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javafx.scene.Scene;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.controller.LevelController;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.engine.records.config.model.Settings;
import oogasalad.engine.records.config.model.wincondition.SurviveForTimeCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class AuthoringViewTest extends DukeApplicationTest {

  private AuthoringView authoringView;
  private AuthoringController mockController;
  private LevelController mockLevelController;

  @Override
  public void start(Stage stage) {
    mockController = mock(AuthoringController.class);
    mockLevelController = mock(LevelController.class);
    AuthoringModel mockModel = mock(AuthoringModel.class);

    when(mockController.getLevelController()).thenReturn(mockLevelController);
    when(mockController.getModel()).thenReturn(mockModel);
    when(mockModel.getDefaultSettings()).thenReturn(new Settings(2.0, 2, 2, "", new SurviveForTimeCondition(5)));

    authoringView = new AuthoringView();
    authoringView.setController(mockController);

    Scene scene = new Scene(authoringView.getNode(), 1200, 800);
    stage.setScene(scene);
    stage.show();
  }


  @BeforeEach
  public void setup() {
    reset(mockController, mockLevelController);
  }

  @Test
  public void setController_ValidController_SubViewsInitialized() {
    AuthoringModel mockModel = mock(AuthoringModel.class);
    when(mockModel.getDefaultSettings()).thenReturn(new Settings(2.0, 2, 2, "", new SurviveForTimeCondition(5)));

    when(mockController.getModel()).thenReturn(mockModel);
    when(mockController.getLevelController()).thenReturn(mockLevelController);

    interact(() -> authoringView.setController(mockController));

    assertNotNull(authoringView.getCanvasView(), "CanvasView should be initialized");
    assertNotNull(authoringView.getEntitySelectorView(), "EntitySelectorView should be initialized");
    assertNotNull(authoringView.getEntityEditorView(), "EntityTypeEditorView should be initialized");
    assertNotNull(authoringView.getGameSettingsView(), "GameSettingsView should be initialized");
    assertNotNull(authoringView.getLevelSelectorView(), "LevelSelectorView should be initialized");

    verify(mockController, atLeastOnce()).getLevelController();
    verify(mockLevelController).initDefaultLevelIfEmpty();
  }


  @Test
  public void getEntityEditorView_DefaultVisibility_NotVisible() {
    assertFalse(authoringView.getEntityEditorView().getRoot().isVisible(), "EntityTypeEditorView should be hidden by default");
  }

  @Test
  public void getGameSettingsView_StyleApplied_HasExpectedStyle() {
    String style = authoringView.getGameSettingsView().getNode().getStyle();
    assertTrue(style.contains("-fx-background-color"), "GameSettingsView should have background style");
  }

  @Test
  public void setController_WindowMaximized_PlatformRunLaterExecuted() {
    assertNotNull(authoringView.getNode().getScene(), "Scene should be set when shown");
  }
}
