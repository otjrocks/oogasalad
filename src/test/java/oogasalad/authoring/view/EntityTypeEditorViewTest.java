package oogasalad.authoring.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.engine.LanguageManager;
import oogasalad.engine.model.EntityTypeRecord;
import oogasalad.engine.model.controlConfig.ControlConfigInterface;
import oogasalad.engine.model.controlConfig.KeyboardControlConfigRecord;
import oogasalad.engine.records.config.ImageConfigRecord;
import oogasalad.engine.config.ModeConfigRecord;
import oogasalad.engine.records.config.model.EntityPropertiesRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class EntityTypeEditorViewTest extends ApplicationTest {

  private EntityTypeEditorView view;
  private EntityTypeRecord mockEntityType;

  @BeforeEach
  public void setUp() {
    LanguageManager.setLanguage("English");
    AuthoringController mockController = mock(AuthoringController.class);
    view = new EntityTypeEditorView(mockController);

    // === Construct ModeConfig using new record-based structure ===
    String imagePath = "mock.png";
    ImageConfigRecord image = new ImageConfigRecord(
        imagePath,
        14,
        14,
        2,
        1.0
    );

    ControlConfigInterface controlConfig = new KeyboardControlConfigRecord();

    EntityPropertiesRecord entityProps = new EntityPropertiesRecord(
        "Default",
        controlConfig,
        2.0,
        List.of()
    );

    ModeConfigRecord mockMode = new ModeConfigRecord("Default", entityProps, image);
    Map<String, ModeConfigRecord> modeMap = new HashMap<>();
    modeMap.put("Default", mockMode);

    mockEntityType = new EntityTypeRecord("Pacman", new KeyboardControlConfigRecord(), modeMap, List.of(), 1.0);

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
  }

}
