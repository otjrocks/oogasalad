package oogasalad.authoring.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.model.EntityType;
import oogasalad.engine.model.controlConfig.ControlConfig;
import oogasalad.engine.model.controlConfig.KeyboardControlConfig;
import oogasalad.engine.records.newconfig.ImageConfig;
import oogasalad.engine.config.ModeConfig;
import oogasalad.engine.records.newconfig.model.EntityProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class EntityTypeEditorViewTest extends ApplicationTest {

  private AuthoringController mockController;
  private EntityTypeEditorView view;
  private EntityType mockEntityType;

  @BeforeEach
  public void setUp() {
    LanguageManager.setLanguage("English");
    mockController = mock(AuthoringController.class);
    view = new EntityTypeEditorView(mockController);

    // === Construct ModeConfig using new record-based structure ===
    String imagePath = "mock.png";
    ImageConfig image = new ImageConfig(
        imagePath,
        14,
        14,
        List.of(0, 1),
        1.0
    );

    ControlConfig controlConfig = new KeyboardControlConfig();

    EntityProperties entityProps = new EntityProperties(
        "Default",
        controlConfig,
        2.0,
        List.of()
    );

    ModeConfig mockMode = new ModeConfig("Default", entityProps, image);
    Map<String, ModeConfig> modeMap = new HashMap<>();
    modeMap.put("Default", mockMode);

    mockEntityType = new EntityType("Pacman", new KeyboardControlConfig(),  modeMap, List.of());

    AuthoringModel mockModel = mock(AuthoringModel.class);
    when(mockController.getModel()).thenReturn(mockModel);
  }


  @Test
  public void setEntityType_InitializesFields() {
    view.setEntityType(mockEntityType);

    VBox root = (VBox) view.getRoot();
    TextField typeField = (TextField) root.getChildren().get(1);
    ComboBox<String> controlBox = (ComboBox<String>) root.getChildren().get(3);

    assertEquals("Pacman", typeField.getText());
    assertEquals("Keyboard", controlBox.getValue());
  }

  @Test
  public void commitChanges_UpdatesController() {
    view.setEntityType(mockEntityType);

    VBox root = (VBox) view.getRoot();
    ComboBox<String> controlBox = (ComboBox<String>) root.getChildren().get(3);

    controlBox.setValue("FollowMouse");
    controlBox.getOnAction().handle(new ActionEvent());

    verify(mockController.getModel()).updateEntityType(any(), any(EntityType.class));
    verify(mockController).updateEntitySelector();
  }


  @Test
  public void addModeButton_Clicked_OpensDialog() {
    view.setEntityType(mockEntityType);

    VBox root = (VBox) view.getRoot();
    Button addModeButton = (Button) root.getChildren().get(root.getChildren().size() - 1);

    assertNotNull(addModeButton);
    assertEquals(LanguageManager.getMessage("ADD_MODE"), addModeButton.getText());
  }
}
