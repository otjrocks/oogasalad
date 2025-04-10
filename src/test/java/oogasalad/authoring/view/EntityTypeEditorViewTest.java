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
import oogasalad.engine.config.ModeConfig;
import oogasalad.engine.model.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class EntityTypeEditorViewTest extends ApplicationTest {

  private AuthoringController mockController;
  private EntityTypeEditorView view;
  private EntityType mockEntityType;

  @BeforeEach
  public void setUp() {
    mockController = mock(AuthoringController.class);
    view = new EntityTypeEditorView(mockController);

    Map<String, ModeConfig> modeMap = new HashMap<>();
    ModeConfig mockMode = new ModeConfig();
    mockMode.setModeName("Default");
    mockMode.setImagePath("mock.png");
    mockMode.setMovementSpeed(2);
    modeMap.put("Default", mockMode);

    mockEntityType = new EntityType("Pacman", "Keyboard", "", modeMap, List.of(new String[]{}),
        new HashMap<>());
    when(mockController.getModel()).thenReturn(mock(AuthoringModel.class));
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
    assertEquals("+ Add Mode", addModeButton.getText());
  }
}
