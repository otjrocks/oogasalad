package oogasalad.authoring.view.canvas;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import oogasalad.authoring.controller.AuthoringController;
import oogasalad.authoring.model.AuthoringModel;
import oogasalad.authoring.model.LevelDraft;
import oogasalad.engine.config.EntityPlacement;
import oogasalad.engine.records.config.ImageConfigRecord;
import oogasalad.engine.records.config.ModeConfigRecord;
import oogasalad.engine.records.config.model.controlConfig.NoneControlConfigRecord;
import oogasalad.engine.records.model.EntityTypeRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class CanvasViewTest extends DukeApplicationTest {

  private CanvasView canvasView;


  @Override
  public void start(Stage stage) {
    AuthoringController controller = mock(AuthoringController.class);

    AuthoringModel model = mock(AuthoringModel.class);
    LevelDraft level = mock(LevelDraft.class);
    when(level.getEntityPlacements()).thenReturn(List.of());
    when(model.getCurrentLevel()).thenReturn(level);
    when(controller.getModel()).thenReturn(model);

    canvasView = new CanvasView(controller);
    Scene scene = new Scene(canvasView.getNode(), 800, 600);
    stage.setScene(scene);
    stage.show();
  }

  @BeforeEach
  public void clearCanvas() {
    interact(() -> canvasView.reloadFromPlacements(List.of()));
  }

  @Test
  public void testRemoveEntityVisualClearsSelection() {
    EntityPlacement placement = mockPlacement(100, 100);
    interact(() -> canvasView.placeEntity(placement));

    ImageView imageView = (ImageView) canvasView.getNode().getChildren().stream()
        .filter(n -> n instanceof ImageView)
        .findFirst().orElseThrow();

    MouseEvent press = mock(MouseEvent.class);
    when(press.getSource()).thenReturn(imageView);
    when(press.getSceneX()).thenReturn(105.0);
    when(press.getSceneY()).thenReturn(105.0);

    interact(() -> canvasView.handleEntityMousePressed(press));

    assertTrue(canvasView.getTileHighlighter().isSelectionVisible(),
        "Should show selection before removal");

    interact(() -> canvasView.removeEntityVisual(placement));

    assertFalse(canvasView.getTileHighlighter().isSelectionVisible(),
        "Selection should be hidden after removal");
  }

  @Test
  public void testSelectionHighlightAppearsOnClick() {
    EntityPlacement placement = mockPlacement(80, 80);
    interact(() -> canvasView.placeEntity(placement));

    ImageView image = (ImageView) canvasView.getNode().getChildren().stream()
        .filter(n -> n instanceof ImageView).findFirst().orElseThrow();

    MouseEvent press = mock(MouseEvent.class);
    when(press.getSource()).thenReturn(image);
    when(press.getSceneX()).thenReturn(85.0);
    when(press.getSceneY()).thenReturn(85.0);

    interact(() -> canvasView.handleEntityMousePressed(press));

    assertTrue(canvasView.getTileHighlighter().isSelectionVisible(),
        "Selection should be shown after click");
    assertEquals(80.0, canvasView.getTileHighlighter().getSelectionX(), 0.1);
    assertEquals(80.0, canvasView.getTileHighlighter().getSelectionY(), 0.1);
  }


  @Test
  public void testDragDoesNotThrowOnNullSelection() {
    assertDoesNotThrow(() -> interact(() -> canvasView.handleEntityMouseDragged(null)));
  }

  private EntityPlacement mockPlacement(double x, double y) {
    ImageConfigRecord imageConfig = new ImageConfigRecord(
        getClass().getResource("/mock.png").toExternalForm(), 28, 28, 4, 1.0
    );

    ModeConfigRecord modeConfig = new ModeConfigRecord("Default", null,
        new NoneControlConfigRecord(), imageConfig);

    Map<String, ModeConfigRecord> modes = new HashMap<>();
    modes.put("Default", modeConfig);

    EntityTypeRecord mockType = mock(EntityTypeRecord.class);
    when(mockType.type()).thenReturn("MockType");
    when(mockType.modes()).thenReturn(modes);

    EntityPlacement placement = new EntityPlacement();
    placement.setResolvedEntityType(mockType);
    placement.setX(x);
    placement.setY(y);
    placement.setMode("Default");

    return placement;
  }

}
